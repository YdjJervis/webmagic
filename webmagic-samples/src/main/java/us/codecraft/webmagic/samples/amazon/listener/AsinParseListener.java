package us.codecraft.webmagic.samples.amazon.listener;

import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.AsinService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.base.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个地方，把asin对象转换成URL对象后入库
 */
public class AsinParseListener implements Runnable {

    @Autowired
    private AsinService asinService;
    @Autowired
    private UrlService urlService;

    private boolean start = true;

    @Override
    public void run() {

        while (start) {

            List<Asin> list = asinService.findAll();

            List<Url> urlList = new ArrayList<Url>();
            for (Asin asin : list) {
                Url url = new Url();
                url.site = asin.site;
                url.status = 0;
                url.url = "http://" + url.site + "/dp/" + asin.code;
                urlList.add(url);

                asin.status = 1;
                asinService.update(asin);
            }

            urlService.addAll(urlList);
            ThreadUtil.sleep(60);
        }
    }

    public void stop() {
        start = false;
    }

    public void start() {
        start = true;
        run();
    }
}
