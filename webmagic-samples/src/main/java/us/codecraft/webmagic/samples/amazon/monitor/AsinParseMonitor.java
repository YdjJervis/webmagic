package us.codecraft.webmagic.samples.amazon.monitor;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.AsinService;
import us.codecraft.webmagic.samples.base.monitor.ParseMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个地方，把asin对象转换成URL对象后入库
 */
@Service
public class AsinParseMonitor extends ParseMonitor {

    @Autowired
    private AsinService mAsinService;

    @Override
    protected List<Url> getUrl() {
        List<Asin> asinList = mAsinService.findAll();


        List<Url> urlList = new ArrayList<Url>();

        if (CollectionUtils.isNotEmpty(asinList)) {
            for (Asin asin : asinList) {

                String[] starArray = asin.saaStar.split("-");
                String[] statusArray = asin.saaStatus.split("-");

                for (int i = 0, len = starArray.length; i < len; i++) {
                    if ("1".equals(starArray[i]) && "0".equals(statusArray[i]) && asin.site.basCrawl == 1) {//标记了要爬取 && 还没有爬取 && 该网站是可以爬取的
                        Url url = new Url();
                        url.url = asin.site.basSite + "/" + Review.PRODUCT_REVIEWS + "/" + asin;
                        url.siteCode = asin.site.basCode;
                        urlList.add(url);//添加进爬取队列
                    }
                }
            }
        }
        return urlList;
    }
}
