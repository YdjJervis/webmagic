package us.codecraft.webmagic.samples.amazon.processor;

import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.extractor.followsell.FollowSellExtractorAdapter;
import us.codecraft.webmagic.samples.amazon.pojo.FollowSell;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 产品跟卖
 * @date 2016/11/4 17:02
 */
@Service
public class FollowSellProcessor extends BasePageProcessor implements ScheduledTask {

    @Override
    protected void dealOtherPage(Page page) {
        /* 如果是产品首页 */
        if (Pattern.compile(".*/gp/offer-listing/.*").matcher(page.getUrl().get()).matches()) {

            List<FollowSell> followSellList = new FollowSellExtractorAdapter().extract(extractSite(page).code, extractAsin(page), page);
            System.out.println(followSellList);

        }
    }

    @Override
    String extractAsin(Page page) {
        return page.getUrl().regex(".*/gp/offer-listing/([0-9A-Za-z]*)").get();
    }

    @Override
    public void execute() {
        sLogger.info("开始执行 跟卖 爬取任务...");
        List<Url> urlList = mUrlService.find(4);
        startToCrawl(urlList);
    }
}