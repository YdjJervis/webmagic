package us.codecraft.webmagic.samples.base.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.base.pojo.BaseUrl;

import java.util.List;

/**
 * 从数据库获取未爬去的URL的监听器
 */
@Service
public class UrlMonitor implements ScheduledTask {

    @Autowired
    private UrlService mUrlService;

    protected Page sPage;

    /**
     * @param page 爬虫Processor中回调的page对象
     */
    public void setPage(Page page) {
        sPage = page;
    }

    @Override
    public void execute() {
        onUrlList(mUrlService.findFailures());
    }

    /**
     * this method can parse url object to request object,if you want
     * to change the rule such as change crawl priority,override it in
     * subclass
     * @param urlList Url list
     */
    protected void onUrlList(List<Url> urlList) {

        if (sPage == null) return;

        for (int i = 0, len = urlList.size(); i < len; i++) {
            BaseUrl url = urlList.get(i);

            Request request = new Request(url.url);
            sPage.addTargetRequest(request);
        }
    }
}
