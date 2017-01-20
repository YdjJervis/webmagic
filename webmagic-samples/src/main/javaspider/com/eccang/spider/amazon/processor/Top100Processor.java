package com.eccang.spider.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.crawl.Department;
import com.eccang.spider.amazon.pojo.top100.SellingProduct;
import com.eccang.spider.amazon.service.crawl.DepartmentService;
import com.eccang.spider.amazon.service.top100.SellingProductService;
import com.eccang.spider.base.monitor.ScheduledTask;
import com.eccang.spider.base.util.UrlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/16 14:36
 */
@Service
public class Top100Processor extends BasePageProcessor implements ScheduledTask {

    @Autowired
    private DepartmentService mDepartmentService;
    @Autowired
    private SellingProductService mSellingProductService;

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
                if (department.depLevel < 3) {
                    /*解析商品listing,并入库*/
                    extractTop100ProductListing(page);
                    /*解析商品页的第一页，并入库*/
                    extractTop100ProductInfo(page, url);

                    /*查询*/
                    List<Department> childDepList = mDepartmentService.findByParent(department.depName, department.urlMD5);

                    if (CollectionUtils.isEmpty(childDepList)) {
                        /*解析当前选中品类下的子品类*/
                        List<Department> departments = extractDepartment(page);
                        /*入库品类信息*/
                        if (CollectionUtils.isNotEmpty(departments)) {
                            storageDepartmentInfo(departments, department, url);
                        }
                    }
                }
            }
        }  else if (url.type == R.CrawlType.TOP_100_CLASSIFY) {
            extractTop100ProductListing(page);
            extractTop100ProductInfo(page, url);
        } else if (url.type == R.CrawlType.TOP_100_PRODUCT) {
            extractTop100ProductInfo(page, url);
        }
    }

    /**
     * 解析或查询当前选中品类信息
     */
    private Department findDepartment(Page page) {
        /*解析当前选中品类名*/
        String parentDepName = extractDepartmentName(page);
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
    private List<Department> extractDepartment(Page page) {
        List<Department> departments = new ArrayList<>();
        List<Selectable> depNodes = page.getHtml().xpath("//*[@id='zg_browseRoot']/ul/li/a").nodes();

        if (CollectionUtils.isNotEmpty(depNodes)) {
            Department department;
            String url;
            for (Selectable depNode : depNodes) {
                department = new Department();
                department.depName = depNode.xpath("/a/text()").get();
                url = depNode.xpath("/a/@href").get();
                department.depUrl = url;
                department.urlMD5 = UrlUtils.md5(url);
                departments.add(department);
            }
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
     *解析当前选中品类名
     */
    private String extractDepartmentName(Page page) {
        return page.getHtml().xpath("//*[@id='zg_browseRoot']//span[@class='zg_selected']/text()").get();
    }

    /**
     * 解析商品分类
     */
    private String extractProClassify(Page page) {
        return page.getHtml().xpath("//li[@id='zg_tabTitle' and @class='active']/h3/text()").get();
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
            department.depTab = parentDep.depTab;
            department.syncTime = new Date();

            /*品类URL入库到URL总表中*/
            depUrl.batchNum = url.batchNum;
            depUrl.url = department.depUrl;
            depUrl.urlMD5 = UrlUtils.md5(department.depUrl);
            depUrl.type = R.CrawlType.TOP_100_PRODUCT;
            depUrl.siteCode = url.siteCode;
            urls.add(depUrl);
        }
        mDepartmentService.addAll(departments);
        mUrlService.addAll(urls);
    }

    /**
     * 解析商品listing,并入库
     */
    private void extractTop100ProductListing(Page page) {
        Url depUrl = getUrl(page);
        List<Url> urls = new ArrayList<>();
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='zg_paginationWrapper']/ol/li").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            Url url;
            for (Selectable node : nodes) {
                url = new Url();
                String urlStr = node.xpath("/li/a/@href").get();
                if (UrlUtils.getValue(urlStr, "pg").equalsIgnoreCase("1")) {
                    continue;
                }
                url.urlMD5 = UrlUtils.md5(urlStr);
                url.batchNum = depUrl.batchNum;
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
            for (Selectable node : nodes) {
                product = new SellingProduct();

                Selectable selectable = node.xpath("//div[@class='zg_itemWrapper']/div/a/@href");
                product.url = selectable.get();
                product.urlMD5 = UrlUtils.md5(selectable.get());
                product.siteCode = url.siteCode;
                product.asin = selectable.regex(".*product-reviews/([0-9a-zA-Z\\-]*).*").get();

                product.depName = departmentName;
                product.classify = classify;
                String rankNumStr = node.xpath("//div[@class='zg_rankDiv']/span/text()").get();
                int rankNum = 0;
                if (StringUtils.isNotEmpty(rankNumStr)) {
                    rankNum = Integer.valueOf(rankNumStr.trim().replace(".", ""));
                }
                product.rankNum = rankNum;
                product.name = node.xpath("//div[@class='zg_itemWrapper']/div/a/text()").get();
                if (StringUtils.isEmpty(product.name)) {
                    product.name = node.xpath("//div[@class='zg_itemWrapper']/div/a/span/@title").get();
                }

                product.imgUrl = node.xpath("//div[@class='zg_itemWrapper']/div//img/@src").get();

                product.reviewStar = node.xpath("//div[@class='zg_itemWrapper']/div/div[contains(@class,'a-icon-row')]//span[@class='a-icon-alt']/text()").get();
                String reviewNum = node.xpath("//div[@class='zg_itemWrapper']/div/div[contains(@class,'a-icon-row')]/a[contains(@class,'a-size-small')]/text()").get();
                if (StringUtils.isNotEmpty(reviewNum)) {
                    product.reviewNum = Integer.valueOf(reviewNum);
                }

                product.price = node.xpath("//*[@class='p13n-sc-price']/text()").get();
                String amazonDelivery = node.xpath("//*[@class='a-icon-prime']/span[@class='a-icon-alt']/text()").get();
                if (StringUtils.isNotEmpty(amazonDelivery)) {
                    product.amazonDelivery = 1;
                }
            }
        }

        if(CollectionUtils.isNotEmpty(products)) {
            mSellingProductService.addAll(products);
        }
    }

    @Override
    public void execute() {
        /*查询需要监测的关键词信息*/
        sLogger.info("开始执行关键词排名爬取任务...");
        List<Url> urls = mUrlService.find(R.CrawlType.TOP_100_DEPARTMENT);
        startToCrawl(urls);
    }
}