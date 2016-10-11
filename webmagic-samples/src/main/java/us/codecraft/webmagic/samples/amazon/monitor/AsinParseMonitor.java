package us.codecraft.webmagic.samples.amazon.monitor;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.AsinService;
import us.codecraft.webmagic.samples.base.monitor.ParseMonitor;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin对象转Url对象的监听器，会心跳监听ASIN表的变化
 * @date 2016/10/11
 */
@Service
public class AsinParseMonitor extends ParseMonitor {

    @Autowired
    private AsinService mAsinService;

    @Override
    protected List<Url> getUrl() {
        List<Asin> asinList = mAsinService.findAll();
        sLogger.info("剩余未转换成URL的Asin数量：" + asinList.size());

        List<Url> urlList = new ArrayList<Url>();

        if (CollectionUtils.isNotEmpty(asinList)) {
            for (Asin asin : asinList) {

                String[] starArray = asin.saaStar.split("-");
                String[] statusArray = asin.saaStatus.split("-");

                List<String> filterList = mAsinService.getFilterWords(asin.saaStar);

                for (int i = 0, len = starArray.length; i < len; i++) {
                    if ("1".equals(starArray[i]) && "0".equals(statusArray[i]) && asin.site.basCrawl == 1) {//标记了要爬取 && 还没有爬取 && 该网站是可以爬取的
                        for (String filter : filterList) {
                            Url url = new Url();
                            url.url = asin.site.basSite + "/" + Review.PRODUCT_REVIEWS + "/" + asin.saaAsin;
                            url.url = UrlUtils.setValue(url.url, "filterByStar", filter);//为Url添加过滤器
                            url.siteCode = asin.site.basCode;
                            url.asin = asin;
                            url.priority = asin.saaPriority;
                            url.type = 0;
                            url.saaAsin = asin.saaAsin;

                            urlList.add(url);//添加进爬取队列，会有很多重复的，但是入库会去重
                        }
                    }
                }
            }
        }

        sLogger.info("转换后的URL列表如下：");
        sLogger.info(urlList);
        return urlList;
    }

}
