package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.AsinService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.base.monitor.ParseMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 对标记为监听的Review进行Url转换
 * @date 2016/10/11
 */
@Service
public class RootAsinMonitor extends ParseMonitor {

    @Autowired
    private AsinService mAsinService;

    @Autowired
    private UrlService mUrlService;

    @Override
    public void execute() {
        List<Url> urlList = getUrl(true);

        mUrlService.addAll(urlList);
    }


    protected List<Url> getUrl(boolean isCrawlAll) {
        List<Url> urlList = new ArrayList<Url>();

        /*List<Asin> asinList = mAsinService.findNotRooted();
        for (Asin asin : asinList) {
            Url url = new Url();

            url.url = asin.site.basSite + "/dp/" + asin.asin;
            url.urlMD5 = UrlUtils.md5(url.url);
            url.type = 3;
            url.siteCode = asin.site.basCode;
            url.priority = asin.saaPriority;
            url.saaAsin = asin.asin;

            urlList.add(url);

            *//* 设置ASIN为爬取中，不然下个几秒进来后又会转换 *//*
            asin.crawledHead = 1;
            mAsinService.update(asin);
        }*/

        sLogger.info("新的ASIN转产品首页的URL条数：" + urlList.size());
        return urlList;
    }

}
