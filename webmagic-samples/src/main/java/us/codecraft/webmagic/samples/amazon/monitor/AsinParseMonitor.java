package us.codecraft.webmagic.samples.amazon.monitor;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
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
        List<Asin> asinList = mAsinService.find(3);

        List<Url> urlList = new ArrayList<Url>();

        if (CollectionUtils.isNotEmpty(asinList)) {
            for (Asin asin : asinList) {
                if (asin.site.basCrawl == 1) {
                    Url url = new Url();
                    url.url = asin.site.basSite + "/dp/" + asin;
                    urlList.add(url);
                }
            }
        }
        return urlList;
    }
}
