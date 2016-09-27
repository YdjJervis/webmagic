package us.codecraft.webmagic.samples.amazon.listener;

import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.base.util.ThreadUtil;

/**
 * 爬取URL的监听
 */
public class CrawlUrlListener implements Runnable{

    @Autowired
    private UrlService mUrlService;

    private boolean mStart = true;

    private PageProcessor mPageProcessor;

    private CrawlUrlListener(PageProcessor pageProcessor){
        mPageProcessor = pageProcessor;
    }

    @Override
    public void run() {
        while (mStart) {



            ThreadUtil.sleep(60);
        }
    }

    public void stop() {
        mStart = false;
    }

    public void start() {
        mStart = true;
        run();
    }
}
