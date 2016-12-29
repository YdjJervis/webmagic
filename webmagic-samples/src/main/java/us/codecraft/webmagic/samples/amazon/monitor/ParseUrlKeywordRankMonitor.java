package us.codecraft.webmagic.samples.amazon.monitor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchRank;
import us.codecraft.webmagic.samples.amazon.pojo.dict.Site;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchRankService;
import us.codecraft.webmagic.samples.amazon.service.dict.SiteService;
import us.codecraft.webmagic.samples.base.monitor.ParseMonitor;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/27 9:25
 */
@Service
public class ParseUrlKeywordRankMonitor extends ParseMonitor {

    @Autowired
    BatchRankService mBatchRankService;
    @Autowired
    SiteService mSiteService;
    @Autowired
    UrlService mUrlService;

    @Override
    public void execute() {
        List<Url> urls = getUrl(true);

        /* 把所有需要监听的Review转换成URL后入库 */
        mUrlService.addAll(urls);
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {
        List<Url> urls = new ArrayList<>();
        /*查询搜索排名批次关系表中需要转换URL的数据转换成URL对象*/
        List<BatchRank> batchRanks = mBatchRankService.findNotCrawled();

        Url url;
        for (BatchRank batchRank : batchRanks) {
            url = new Url();
            url.batchNum = batchRank.getBatchNum();
            url.siteCode = batchRank.getSiteCode();
            Site site = mSiteService.find(url.siteCode);
            if(StringUtils.isEmpty(batchRank.getDepartmentCode())) {
                url.url = site.site + "/s?keywords=" + URLEncoder.encode(batchRank.getKeyword()) + "&url=search-alias=aps&ie=UTF8&lo=none";
            } else {
                url.url = site.site + "/s?keywords=" + URLEncoder.encode(batchRank.getKeyword()) + "&url=" + URLEncoder.encode(batchRank.getDepartmentCode()) + "&ie=UTF8&lo=none";
            }
            url.urlMD5 = UrlUtils.md5(url.batchNum + url.url);
            url.type = batchRank.getType();
            url.asin = batchRank.getAsin();
            url.priority = batchRank.getPriority();

            batchRank.setStatus(1);
            mBatchRankService.update(batchRank);
            urls.add(url);
        }
        sLogger.info("新添加的review的监听条数：" + urls.size());

        return urls;
    }
}