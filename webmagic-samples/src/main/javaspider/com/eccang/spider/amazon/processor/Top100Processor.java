package com.eccang.spider.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchTop100;
import com.eccang.spider.amazon.pojo.crawl.Department;
import com.eccang.spider.amazon.pojo.top100.DepartmentWhitelist;
import com.eccang.spider.amazon.pojo.top100.SellingProduct;
import com.eccang.spider.amazon.service.batch.BatchTop100Service;
import com.eccang.spider.amazon.service.crawl.DepartmentService;
import com.eccang.spider.amazon.service.top100.DepartmentBlacklistService;
import com.eccang.spider.amazon.service.top100.DepartmentWhitelistService;
import com.eccang.spider.amazon.service.top100.SellingProductService;
import com.eccang.spider.base.monitor.ScheduledTask;
import com.eccang.spider.base.util.UrlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Json;
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

    private final static Logger mLogger = LoggerFactory.getLogger(R.BusinessLog.TOP);

    @Autowired
    private DepartmentService mDepartmentService;
    @Autowired
    private SellingProductService mSellingProductService;
    @Autowired
    private BatchTop100Service mBatchTop100Service;
    @Autowired
    private DepartmentBlacklistService mBlacklistService;
    @Autowired
    private DepartmentWhitelistService mWhitelistService;

    @Override
    protected void dealOtherPage(Page page) {
        Url url = getUrl(page);

        if (url.type == R.CrawlType.TOP_100_DEPARTMENT) {

            /*解析当前选中品类名称*/
            Department department = findDepartment(page, url);

            if (department.depLevel < 4) {

                /*解析商品listing,并入库*/
                extractTop100ProductListing(page, url);
                /*解析商品页的第一页，并入库*/
                extractTop100Product(page, url);

                /*
                 *如果当前选中品类级数小于3，则解析子品类
                 */
                if (department.depLevel < 3) {
                    /*解析当前选中品类下的子品类*/
                    List<Department> departments = extractDepartment(page, department);
                    /*入库品类信息*/
                    if (CollectionUtils.isNotEmpty(departments)) {
                        storageDepartmentInfo(departments, department, url);
                    }
                }
            }
        } else if (url.type == R.CrawlType.TOP_100_CLASSIFY) {
            extractTop100ProductListing(page, url);
            extractTop100Product(page, url);
        } else if (url.type == R.CrawlType.TOP_100_PRODUCT) {
            extractTop100Product(page, url);
        }

        /*更新详单总单信息*/
        updateBatchStatus(url);
    }

    /**
     * 解析或查询当前选中品类信息
     */
    private Department findDepartment(Page page, Url url) {

        Department parentDep;
        if (isRootDepartment(page)) {

            /*解析当前选中品类名*/
            String parentDepName = extractDepartmentName(page);

            if (StringUtils.isNotEmpty(parentDepName)) {
                parentDepName = parentDepName.trim();
            }

            parentDep = new Department();
            parentDep.depName = parentDepName;
            parentDep.siteCode = url.siteCode;
            /*解析一级导航*/
            parentDep.depTab = extractDepartmentTag(page);
        } else {
            parentDep = mDepartmentService.findByBatchNumAndDepCode(url.batchNum, url.depCode);
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
            String batchNum = getUrl(page).batchNum;
            int childDepNum = nodeList.getLength();

            /*
             * @param depCombin 解析到的品类的前两级的品类名组合
             * @Note 当只爬取前三级品类的时候适用，如果要解析第四级或以下的品类则无法满足通过品类名称组合转md5来确定唯一的品类码
             */
            String depCombin = dep.depName + dep.parentDepName;

            for (int i = 0; i < childDepNum; i++) {

                Node node = nodeList.item(i);
                System.out.println(node.getTextContent() + ":" + xPath.evaluate("@href", node, XPathConstants.STRING));
                department = new Department();
                department.batchNum = batchNum;

                department.depName = node.getTextContent();
                if (StringUtils.isEmpty(department.depName)) {
                    continue;
                }

                department.depName = department.depName.replace("&amp;", "&");

                department.depCode = UrlUtils.md5(department.depName + depCombin);
                /*
                 *判断当前选中的品类是否在黑名单中，存在，则不解析其子孙品类及产品信息
                 */
                if (mBlacklistService.findByDepCodeCount(department.depCode, dep.siteCode) > 0) {
                    mLogger.info("当前选中的品类(" + department.depName + ")，已经被标识为黑名单，不需要爬取.");
                    continue;
                }

                url = (String) xPath.evaluate("@href", node, XPathConstants.STRING);
                department.depUrl = url;
                departments.add(department);
            }

        } catch (ParserConfigurationException | XPathExpressionException e) {
            mLogger.error(e.toString());
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
        String selectDep = page.getHtml().xpath("//*[@id='zg_browseRoot']//span[@class='zg_selected']/text()").get();
        if (StringUtils.isEmpty(selectDep)) {
            selectDep = page.getHtml().xpath("//*[@id='zg_listTitle']/span[@class='category']/text()").get();
        }
        return selectDep;
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
        proClassifyUrl.priority = url.priority;
        proClassifyUrl.depCode = url.depCode;
        proClassifyUrl.urlMD5 = UrlUtils.md5(url.batchNum + classifyUrl + url.depCode);
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

        int depLevel = parentDep.depLevel + 1;

        for (Department department : departments) {
            depUrl = new Url();
            department.depLevel = depLevel;
            department.pId = parentDep.id;
            department.parentDepName = parentDep.depName;
            department.siteCode = url.siteCode;
            department.depTab = parentDep.depTab;
            department.syncTime = new Date();

            /*
             *判断当前选中的品类是否在白名单中，存在，则修改当前url解析的优先级与白名单中的一致
             */
            DepartmentWhitelist whitelist = mWhitelistService.findByDoubleCode(department.depCode, department.siteCode);

            /*品类URL入库到URL总表中*/
            depUrl.batchNum = url.batchNum;
            depUrl.priority = whitelist != null ? whitelist.priority : url.priority;
            depUrl.url = department.depUrl;
            depUrl.depCode = department.depCode;
            depUrl.urlMD5 = UrlUtils.md5(url.batchNum + department.depUrl + url.depCode);
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
            int size = nodes.size();
            for (int i = 1; i < size; i++) {
                url = new Url();
                String urlStr = nodes.get(i).xpath("/li/a/@href").get();
                url.batchNum = depUrl.batchNum;
                url.priority = depUrl.priority;
                url.urlMD5 = UrlUtils.md5(depUrl.batchNum + urlStr + depUrl.depCode);
                url.batchNum = depUrl.batchNum;
                url.siteCode = depUrl.siteCode;

                if (depUrl.type == R.CrawlType.TOP_100_CLASSIFY) {
                    url.parentUrl = depUrl.parentUrl;
                } else {
                    url.parentUrl = depUrl.url;
                }

                url.depCode = depUrl.depCode;
                url.url = urlStr;
                url.type = R.CrawlType.TOP_100_PRODUCT;
                urls.add(url);
            }
        }

        if (CollectionUtils.isNotEmpty(urls)) {
            mUrlService.addAll(urls);
        }
    }

    private void extractTop100Product(Page page, Url url) {
        if (url.siteCode.equals(R.SiteCode.JP)) {
            extractTop100ProductInfoJP(page, url);
        } else {
            extractTop100ProductInfo(page, url);
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
            String classify = extractProClassify(page);

            if (StringUtils.isNotEmpty(classify) && url.type == R.CrawlType.TOP_100_DEPARTMENT) {
                storageProClassifyUrl(page, url);
            }

            for (Selectable node : nodes) {
                product = new SellingProduct();

                Selectable selectable = node.xpath("//div[@class='zg_itemWrapper']/div/a/@href");
                product.batchNum = url.batchNum;
                product.url = selectable.get();

                /*
                 * 品类下排名的商品，排名中间可能为空
                 */
                if (StringUtils.isEmpty(selectable.get())) {
                    continue;
                }

                product.urlMD5 = UrlUtils.md5(selectable.get());
                product.siteCode = url.siteCode;

                /*解析产品asin*/
                String asinSelectable = node.xpath("//div[@class='zg_itemWrapper']/div[contains(@class, 'p13n-asin')]/@data-p13n-asin-metadata").get();
                product.asin = new Json(asinSelectable).jsonPath("$.asin").get();

                if (url.type == R.CrawlType.TOP_100_DEPARTMENT) {
                    product.depUrl = url.url;
                } else {
                    product.depUrl = url.parentUrl;
                }

                product.depCode = url.depCode;

                product.classify = classify;
                String rankNumStr = node.xpath("//div[@class='zg_rankDiv']/span/text()").get();
                int rankNum = 0;
                if (StringUtils.isNotEmpty(rankNumStr)) {
                    rankNumStr = rankNumStr.trim().replace(".", "");
                    rankNum = Integer.valueOf(rankNumStr);
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
                    reviewNum = reviewNum.replace(",", "");
                    reviewNum = reviewNum.replace(".", "");
                    reviewNum = reviewNum.replace(" ", "");
                    product.reviewNum = Integer.valueOf(reviewNum);
                }

                product.price = node.xpath("//*[@class='p13n-sc-price']/text()").get();
                String amazonDelivery = node.xpath("//*[@class='a-icon-prime' or @class='a-icon-premium']/span[@class='a-icon-alt']/text()").get();
                if (StringUtils.isNotEmpty(amazonDelivery)) {
                    product.amazonDelivery = 1;
                }

                products.add(product);
            }
        }

        mSellingProductService.addAll(products);
    }


    /**
     * 日本站,解析top100热销商品信息,并商品信息入库
     */
    private void extractTop100ProductInfoJP(Page page, Url url) {
        ArrayList<SellingProduct> products = new ArrayList<>();
        /*
         *解析产品分类
         */
        List<Selectable> productClassifyNodes = page.getHtml().xpath("//*[@id='zg_columnTitle']/div/h3/text()").nodes();
        List<String> proClassifies = new ArrayList<>();
        for (Selectable productClassifyNode : productClassifyNodes) {
            String proClassify = productClassifyNode.get();
            if (StringUtils.isNotEmpty(proClassify)) {
                proClassifies.add(proClassify);
            }
        }

        /*
         *解析产品信息
         */
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='zg_critical' or @id='zg_nonCritical']/div[@class='zg_itemRow']").nodes();
        SellingProduct product;

        for (Selectable node : nodes) {
            List<Selectable> proInfos = node.xpath("//div[contains(@class,'p13n-asin')]").nodes();
            int index = 0;
            for (Selectable proInfo : proInfos) {
                product = new SellingProduct();
                product.batchNum = url.batchNum;
                //product url
                product.url = proInfo.xpath("//div[contains(@class,'a-col-right')]/a/@href").get();
                product.urlMD5 = UrlUtils.md5(product.url);
                product.siteCode = url.siteCode;
                if (StringUtils.isEmpty(product.url)) {
                    continue;
                }

                if (url.type == R.CrawlType.TOP_100_DEPARTMENT) {
                    product.depUrl = url.url;
                } else {
                    product.depUrl = url.parentUrl;
                }

                product.depCode = url.depCode;
                //asin
                String asinStr = proInfo.xpath("/div/@data-p13n-asin-metadata").get();
                product.asin = new Json(asinStr).jsonPath("$.asin").get();
                //product img url
                product.imgUrl = proInfo.xpath("//div[contains(@class,'a-col-left')]/a/img/@src").get();
                //rank num
                String rankNumStr = proInfo.xpath("//div[contains(@class,'a-col-right')]/div[@class='zg_rankLine']/span[@class='zg_rankNumber']/text()").get();
                if(StringUtils.isNotEmpty(rankNumStr)) {
                    product.rankNum = Integer.valueOf(rankNumStr.trim().replace(".", ""));
                }
                //title
                product.name = proInfo.xpath("//div[contains(@class,'a-col-right')]/a/div[contains(@class,'p13n-sc-truncated')]/@title").get();
                if (StringUtils.isEmpty(product.name)) {
                    product.name = proInfo.xpath("//div[contains(@class,'a-col-right')]/a/div[contains(@class,'p13n-sc-truncated')]/text()").get();
                }
                //review star
                product.reviewStar = proInfo.xpath("//div[contains(@class,'a-col-right')]/div[contains(@class,'a-icon-row')]//span[@class='a-icon-alt']/text()").get();
                //review num
                String reviewNum = proInfo.xpath("//div[contains(@class,'a-col-right')]/div[contains(@class,'a-icon-row')]/a[contains(@class,'a-size-small')]/text()").get();
                if(StringUtils.isNotEmpty(reviewNum)) {
                    product.reviewNum = Integer.valueOf(reviewNum.replace(",", ""));
                }
                //price
                product.price = proInfo.xpath("//div[contains(@class,'a-col-right')]/div[@class='a-row']//span[@class='p13n-sc-price']/text()").get();
                if(StringUtils.isEmpty(product.price)) {
                    product.price = proInfo.xpath("//div[contains(@class,'a-col-right')]/div[@class='a-row']/span[contains(@class,'a-color-price')]/text()").get();
                }

                String amazonDelivery = proInfo.xpath("//*[contains(@class,'a-icon-prime-jp')]/span[@class='a-icon-alt']/text()").get();
                if (StringUtils.isNotEmpty(amazonDelivery)) {
                    product.amazonDelivery = 1;
                }
                //product classify
                product.classify = proClassifies.size() == 0 ? "" : proClassifies.get(index % proClassifies.size());
                products.add(product);
                index++;
            }
        }

        mSellingProductService.addAll(products);
    }

    /**
     * 更新Top100详单和总单的进度（进度为动态）
     */
    private void updateBatchStatus(Url url) {

        Date currentTime = new Date();

        /*查询url所属的详单信息*/
        BatchTop100 batchTop100 = mBatchTop100Service.findByBatchNumAndSite(url.batchNum, url.siteCode);

        /*查询详单下所有Url数量*/
        int top100UrlTotal = mUrlService.findByBatchNumAndSiteCount(url.batchNum, url.siteCode);

        /*统计详单下已经解析成功的url*/
        int top100FinishUrlTotal = mUrlService.findByBatchNumAndSiteFinishCount(url.batchNum, url.siteCode);

        /*计算当前详单完成进度*/
        float progress = top100FinishUrlTotal / (float) top100UrlTotal;

        /*变化，则更新新的详单进度*/
        if (progress != batchTop100.progress) {
            batchTop100.progress = progress;
            if (progress == 1) {
                batchTop100.status = 2;

                /*详单完成，则查询这个详单对应的所有url*/
                List<Url> urls = mUrlService.findByBatchNumAndSite(url.batchNum, url.siteCode);
                /*将url存入历史表中*/
                addUrlHistory(urls);

                /*删除url数据*/
                mUrlService.deleteByBNSC(url.batchNum, url.siteCode);
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

    /**
     * 将上万数据分批插入数据库中
     */
    private void addUrlHistory(List<Url> urls) {
        int addNum = 2000;
        int index = urls.size() / addNum;
        for (int i = 0; i < index; i++) {
            mUrlHistoryService.addAll(urls.subList(i * addNum, (i + 1) * addNum - 1));
        }
        mUrlHistoryService.addAll(urls.subList(index * addNum, urls.size()));
    }

    @Override
    public void execute() {
        /*查询需要监测的关键词信息*/
        mLogger.info("开始执行Top100商品爬取任务...");
        List<Url> urls = mUrlService.findTop100();
        startToCrawl(urls);
    }
}