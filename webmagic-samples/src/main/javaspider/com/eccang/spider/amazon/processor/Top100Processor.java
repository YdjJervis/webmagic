package com.eccang.spider.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.crawl.Department;
import com.eccang.spider.amazon.service.crawl.DepartmentService;
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

    @Override
    protected void dealOtherPage(Page page) {
        Url url = getUrl(page);

        if(url.type == R.CrawlType.TOP_100_DEPARTMENT) {
            /*解析当前选中品类名称（即父品类）*/
            Department parentDep = findParentDep(page);

            /*
             *如果父品类级数大于2，则子品类不再解析
             */
            if (parentDep != null) {
                if (parentDep.depLevel > 2) {
                    /*解析当前选中品类下的子品类*/
                    List<Department> departments = extractDepartment(page);

                    /*入库品类信息*/
                    if (CollectionUtils.isNotEmpty(departments)) {
                        storageDepartmentInfo(departments, parentDep, url);
                    }
                }
            }
        }else if(url.type == R.CrawlType.TOP_100_DEP_PRODUCT) {
            System.out.println("解析产品信息");
        }
    }


    /**
     * 解析或查询父品类信息
     */
    private Department findParentDep(Page page) {
        /*解析父品类名*/
        String parentDepName = page.getHtml().xpath("//*[@id='zg_browseRoot']//span[@class='zg_selected']/text()").get();
        Department parentDep;
        if(isRootDepartment(page)) {
            parentDep = new Department();
            parentDep.depName = parentDepName;
            parentDep.depLevel = 0;
            /*解析一级导航*/
            parentDep.depTab = extractDepartmentTag(page);
        } else {
            parentDep = mDepartmentService.findByNameAndUrl(parentDepName, getUrl(page).url);
        }
        return parentDep;
    }

    /**
     * 解析品类名称与url
     */
    private List<Department> extractDepartment(Page page) {
        List<Department> departments = new ArrayList<>();
        List<Selectable> depNodes = page.getHtml().xpath("//*[@id='zg_browseRoot']/ul/li/a").nodes();

        if(CollectionUtils.isNotEmpty(depNodes)) {
            Department department;
            for (Selectable depNode : depNodes) {
                department = new Department();
                department.depName = depNode.xpath("/a/text()").get();
                department.depUrl = depNode.xpath("/a/@href").get();
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
     * 判断父品类是不是根品类
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
            depUrl.type = R.CrawlType.TOP_100_DEP_PRODUCT;
            depUrl.siteCode = url.siteCode;
            urls.add(depUrl);
        }
        mDepartmentService.addAll(departments);
        mUrlService.addAll(urls);
    }

    @Override
    public void execute() {
        /*查询需要监测的关键词信息*/
        sLogger.info("开始执行关键词排名爬取任务...");
        List<Url> urls = mUrlService.find(R.CrawlType.TOP_100_DEPARTMENT);
        startToCrawl(urls);
    }
}