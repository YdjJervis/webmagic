package com.eccang.spider.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchTop100;
import com.eccang.spider.amazon.pojo.crawl.Department;
import com.eccang.spider.amazon.pojo.top100.SellingProduct;
import com.eccang.spider.amazon.service.batch.BatchTop100Service;
import com.eccang.spider.amazon.service.crawl.DepartmentService;
import com.eccang.spider.amazon.service.top100.SellingProductService;
import com.eccang.spider.base.monitor.ScheduledTask;
import com.eccang.spider.base.util.UrlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 *          2017/1/16 14:36
 */
@Service
public class Top100Processor extends BasePageProcessor implements ScheduledTask {

    @Autowired
    private DepartmentService mDepartmentService;
    @Autowired
    private SellingProductService mSellingProductService;
    @Autowired
    private BatchTop100Service mBatchTop100Service;

    @Override
    protected void dealOtherPage(Page page) {
        Url url = getUrl(page);

        /*解析当前选中品类名称*/
        Department department = findDepartment(page);

        if (url.type == R.CrawlType.TOP_100_DEPARTMENT) {
            /*
             *如果当前选中品类级数小于3，则解析子品类
             */
            if (department != null) {
                if (department.depLevel < 4) {
                    /*解析商品listing,并入库*/
                    extractTop100ProductListing(page, url);
                    /*解析商品页的第一页，并入库*/
                    extractTop100ProductInfo(page, url);

                    if(department.depLevel < 3) {
                        /*查询子品类*/
                        List<Department> childDepList = mDepartmentService.findByParentUrl(department.depUrl);

                        if (CollectionUtils.isEmpty(childDepList)) {
                            /*解析当前选中品类下的子品类*/
                            List<Department> departments = extractDepartment(page, department);
                            /*入库品类信息*/
                            if (CollectionUtils.isNotEmpty(departments)) {
                                storageDepartmentInfo(departments, department, url);
                            }
                        }
                    }
                }
            }
        } else if (url.type == R.CrawlType.TOP_100_CLASSIFY) {
            extractTop100ProductListing(page, url);
            extractTop100ProductInfo(page, url);
        } else if (url.type == R.CrawlType.TOP_100_PRODUCT) {
            extractTop100ProductInfo(page, url);
        }

        /*更新详单总单信息*/
        updateBatchStatus(url);
    }

    /**
     * 解析或查询当前选中品类信息
     */
    private Department findDepartment(Page page) {
        /*解析当前选中品类名*/
        String parentDepName = extractDepartmentName(page);

        if (StringUtils.isNotEmpty(parentDepName)) {
            parentDepName = parentDepName.trim();
        }

        Department parentDep;
        if (isRootDepartment(page)) {
            parentDep = new Department();
            parentDep.depName = parentDepName;
            parentDep.depLevel = 0;
            /*解析一级导航*/
            parentDep.depTab = extractDepartmentTag(page);
        } else {
            parentDep = mDepartmentService.findByNameAndUrlMD5(parentDepName, getUrl(page).urlMD5);
        }
        return parentDep;
    }

    /**
     * 解析品类名称与url
     */
    private List<Department> extractDepartment(Page page, Department dep) {

        List<Department> departments = new ArrayList<>();

        try {
            TagNode tagNode = new HtmlCleaner().clean(page.getHtml().get());
            Document document = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodeList;
            if (dep.depLevel > 0) {
                nodeList = (NodeList) xPath.evaluate("//*[@id='zg_browseRoot']//*[@class='zg_selected']/parent::li/parent::ul/ul/li/a", document, XPathConstants.NODESET);
            } else {
                nodeList = (NodeList) xPath.evaluate("//*[@id='zg_browseRoot']/ul/li/a", document, XPathConstants.NODESET);
            }

            Department department;
            String url;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.println(node.getTextContent() + ":" + xPath.evaluate("@href", node, XPathConstants.STRING));
                department = new Department();
                department.batchNum = getUrl(page).batchNum;
                department.depName = node.getTextContent();

                if (department.depName.contains("&amp;")) {
                    department.depName = department.depName.replace("&amp;", "&");
                }

                url = (String) xPath.evaluate("@href", node, XPathConstants.STRING);
                department.depUrl = url;
                department.urlMD5 = UrlUtils.md5(url);
                departments.add(department);
            }

        } catch (ParserConfigurationException | XPathExpressionException e) {
            sLogger.warn(e);
            return null;
        }

        return departments;
    }

    /**
     * 解析一级导航
     */
    private String extractDepartmentTag(Page page) {
        return page.getHtml().xpath("//*[@id='zg_tabs']/ul/li[@class='zg_selected']//a/text()").get();
    }

    /**
     * 解析当前选中品类名
     */
    private String extractDepartmentName(Page page) {
        return page.getHtml().xpath("//*[@id='zg_browseRoot']//span[@class='zg_selected']/text()").get();
    }

    /**
     * 解析商品分类名称
     */
    private String extractProClassify(Page page) {
        return page.getHtml().xpath("//li[@id='zg_tabTitle' and @class='active']/h3/text()").get();
    }

    /**
     * 将商品分类url入库(没有被选中的)
     */
    private void storageProClassifyUrl(Page page, Url url) {
        Url proClassifyUrl = new Url();
        String classifyUrl = page.getHtml().xpath("//li[@id='zg_tabTitle']/h3/a/@href").get();
        proClassifyUrl.batchNum = url.batchNum;
        proClassifyUrl.url = classifyUrl;
        proClassifyUrl.urlMD5 = UrlUtils.md5(classifyUrl);
        proClassifyUrl.parentUrl = url.url;
        proClassifyUrl.type = R.CrawlType.TOP_100_CLASSIFY;
        proClassifyUrl.siteCode = url.siteCode;
        mUrlService.add(proClassifyUrl);
    }

    /**
     * 判断当前选中品类是不是根品类
     */
    private boolean isRootDepartment(Page page) {
        return StringUtils.isEmpty(page.getHtml().xpath("//*[@id='zg_browseRoot']/li[@class='zg_browseUp']").get());
    }

    /**
     * 入库品类信息（1.入库品类表；2.入库url表）
     */
    private void storageDepartmentInfo(List<Department> departments, Department parentDep, Url url) {
        List<Url> urls = new ArrayList<>();
        Url depUrl;
        for (Department department : departments) {
            depUrl = new Url();
            department.depLevel = parentDep.depLevel + 1;
            department.parentDepName = parentDep.depName;
            department.urlMD5 = UrlUtils.md5(department.depUrl);
            department.pDepUrl = parentDep.depUrl;
            department.depTab = parentDep.depTab;
            department.syncTime = new Date();

            /*品类URL入库到URL总表中*/
            depUrl.batchNum = url.batchNum;
            depUrl.url = department.depUrl;
            depUrl.urlMD5 = UrlUtils.md5(department.depUrl);
            depUrl.type = R.CrawlType.TOP_100_DEPARTMENT;
            depUrl.siteCode = url.siteCode;
            urls.add(depUrl);
        }
        mDepartmentService.addAll(departments);
        mUrlService.addAll(urls);
    }

    /**
     * 解析商品listing,并入库
     */
    private void extractTop100ProductListing(Page page, Url depUrl) {

        List<Url> urls = new ArrayList<>();
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='zg_paginationWrapper']/ol/li").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            Url url;
            for (int i = 1; i < nodes.size(); i++) {
                url = new Url();
                String urlStr = nodes.get(i).xpath("/li/a/@href").get();
                url.batchNum = depUrl.batchNum;
                url.urlMD5 = UrlUtils.md5(urlStr);
                url.batchNum = depUrl.batchNum;
                url.siteCode = depUrl.siteCode;
                url.parentUrl = depUrl.url;
                url.url = urlStr;
                url.type = R.CrawlType.TOP_100_PRODUCT;
                urls.add(url);
            }
        }

        if (CollectionUtils.isNotEmpty(urls)) {
            mUrlService.addAll(urls);
        }
    }

    /**
     * 解析top100热销商品信息,并商品信息入库
     */
    private void extractTop100ProductInfo(Page page, Url url) {
        ArrayList<SellingProduct> products = new ArrayList<>();

        List<Selectable> nodes = page.getHtml().xpath("//*[@id='zg_centerListWrapper']/div[@class='zg_itemImmersion']").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            SellingProduct product;
            String departmentName = extractDepartmentName(page);
            String classify = extractProClassify(page);

            if (StringUtils.isNotEmpty(classify) && url.type == R.CrawlType.TOP_100_DEPARTMENT) {
                storageProClassifyUrl(page, url);
            }

            for (Selectable node : nodes) {
                product = new SellingProduct();

                Selectable selectable = node.xpath("//div[@class='zg_itemWrapper']/div/a/@href");
                product.batchNum = url.batchNum;
                product.url = selectable.get();
                product.urlMD5 = UrlUtils.md5(selectable.get());
                product.siteCode = url.siteCode;
                product.asin = selectable.regex(".*/dp/([0-9A-Za-z]*)/").get();

                product.depName = departmentName;

                if (url.type == R.CrawlType.TOP_100_DEPARTMENT) {
                    product.depUrl = url.url;
                } else {
                    product.depUrl = url.parentUrl;
                }

                product.classify = classify;
                String rankNumStr = node.xpath("//div[@class='zg_rankDiv']/span/text()").get();
                int rankNum = 0;
                if (StringUtils.isNotEmpty(rankNumStr)) {
                    rankNum = Integer.valueOf(rankNumStr.trim().replace(".", ""));
                }
                product.rankNum = rankNum;
                product.name = node.xpath("//div[@class='zg_itemWrapper']/div/a/div[@class='p13n-sc-truncate']/@title").get();
                if (StringUtils.isEmpty(product.name)) {
                    product.name = node.xpath("//div[@class='zg_itemWrapper']/div/a/div[@class='p13n-sc-truncate']/text()").get();
                }

                product.imgUrl = node.xpath("//div[@class='zg_itemWrapper']/div//img/@src").get();

                product.reviewStar = node.xpath("//div[@class='zg_itemWrapper']/div/div[contains(@class,'a-icon-row')]//span[@class='a-icon-alt']/text()").get();
                String reviewNum = node.xpath("//div[@class='zg_itemWrapper']/div/div[contains(@class,'a-icon-row')]/a[contains(@class,'a-size-small')]/text()").get();
                if (StringUtils.isNotEmpty(reviewNum)) {
                    if (reviewNum.contains(",")) {
                        reviewNum = reviewNum.replace(",", "");
                    }
                    product.reviewNum = Integer.valueOf(reviewNum);
                }

                product.price = node.xpath("//*[@class='p13n-sc-price']/text()").get();
                String amazonDelivery = node.xpath("//*[@class='a-icon-prime']/span[@class='a-icon-alt']/text()").get();
                if (StringUtils.isNotEmpty(amazonDelivery)) {
                    product.amazonDelivery = 1;
                }

                products.add(product);
            }
        }

        if (CollectionUtils.isNotEmpty(products)) {
            mSellingProductService.addAll(products);
        }
    }

    /**
     * 更新Top100详单和总单的进度（进度为动态）
     */
    private void updateBatchStatus(Url url) {

        Date currentTime = new Date();

        /*查询url所属的详单信息*/
        BatchTop100 batchTop100 = mBatchTop100Service.findByBatchNumAndSite(url.batchNum, url.siteCode);

        /*查询详单下所有的Url*/
        List<Url> urls = mUrlService.findByBatchNumAndSite(url.batchNum, url.siteCode);

        /*统计已经解析成功的url*/
        List<Url> hasCompletedUrls = new ArrayList<>();
        for (Url url1 : urls) {
            if (url1.status == 200) {
                hasCompletedUrls.add(url1);
            }
        }

        /*计算当前详单完成进度*/
        float progress = hasCompletedUrls.size() / (float) urls.size();

        /*变化，则更新新的详单进度*/
        if (progress != batchTop100.progress) {
            batchTop100.progress = progress;
            if (progress == 1) {
                batchTop100.status = 2;
            }
            mBatchTop100Service.update(batchTop100);
        }

        /*查询总单信息*/
        Batch batch = mBatchService.findByBatchNumber(url.batchNum);

        /*计算当前总单进度，有变化则更新*/
        float batchProgress = mBatchTop100Service.findAverageProgress(url.batchNum);
        if (batch.progress != batchProgress) {
            batch.progress = batchProgress;
            batch.status = 1;
        }

        /*如果进度为1，则更新完成时间及任务状态*/
        if (batch.progress == 1) {
            batch.finishTime = currentTime;
            batch.status = 2;
        }

        /*总单的开始时间为空，则更新为当前时间*/
        if (batch.startTime == null) {
            batch.startTime = currentTime;
        }

        mBatchService.update(batch);
    }

    @Override
    public void execute() {
        /*查询需要监测的关键词信息*/
        sLogger.info("开始执行Top100商品爬取任务...");
        List<Url> urls = mUrlService.findTop100();
        startToCrawl(urls);
    }
}