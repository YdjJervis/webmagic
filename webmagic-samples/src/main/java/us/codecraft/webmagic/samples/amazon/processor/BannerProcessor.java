package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pipeline.BannerPipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Banner;
import us.codecraft.webmagic.samples.amazon.pojo.Country;
import us.codecraft.webmagic.samples.amazon.pojo.UrlPrefix;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 */
public class BannerProcessor implements PageProcessor {


    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(250);

    private Logger logger = Logger.getLogger(getClass());
    private static Country mCountry = UrlPrefix.getCountry("jp");

    @Override
    public void process(Page page) {
        dealBanner(page);
    }

    private void dealBanner(Page page) {
        System.out.println(page.getHtml());
        if (isHomePage(page)) {
            String site = page.getUrl().get();
            List<Selectable> bannerNodeList = page.getHtml().xpath("//div[@class='cc-lm-tcgImgItem']").nodes();
            List<Banner> bannerList = new ArrayList<Banner>();
            for (Selectable bannerNode : bannerNodeList) {
                Banner banner = new Banner(site);
                banner.setSort(bannerNode.xpath("/div[@id='ccCateName']/text()").get());
                banner.setPrice(bannerNode.xpath("/div[@id='ccShowAd']/text()").get());
                banner.setImgUrl(bannerNode.xpath("/a/@src").get());

                bannerList.add(banner);
            }

            page.putField(BannerPipeline.PARAM_LIST, bannerList);
        }
    }

    private boolean isHomePage(Page page) {
        String site = page.getUrl().get();
        if (site.startsWith("https://www.amazon.") && site.length() <= 25) {
            return true;
        }
        return false;
    }

    @Override
    public Site getSite() {
        mSite.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36 QIHU 360SE");
        return mSite;
    }

    public static void main(String[] args) {
        Spider.create(new BannerProcessor())
                .addPipeline(new BannerPipeline())
                .thread(1)
                .addUrl("https://www.amazon.cn")
                .start();
    }
}
