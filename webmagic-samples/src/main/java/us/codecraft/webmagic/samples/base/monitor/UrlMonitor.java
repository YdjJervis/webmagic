package us.codecraft.webmagic.samples.base.monitor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.UrlService;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Url监听器
 * @date 2016/10/11
 */
@Service
public class UrlMonitor implements ScheduledTask {

    @Autowired
    private UrlService mUrlService;

    protected Page sPage;

    protected Logger sLogger = Logger.getLogger(getClass());

    /**
     * @param page 爬虫Processor中回调的page对象
     */
    public void setPage(Page page) {
        sPage = page;
    }

    @Override
    public void execute() {
        onUrlList(mUrlService.find(0));
    }

    /**
     * this method can parse url object to request object,if you want
     * to change the rule such as change crawl priority,override it in
     * subclass
     * @param urlList Url list
     */
    protected void onUrlList(List<Url> urlList) {

        if (sPage == null || CollectionUtils.isEmpty(urlList)) return;

        for (Url url : urlList) {
            Request request = new Request(url.url);
            sPage.addTargetRequest(request);
        }
    }
}
