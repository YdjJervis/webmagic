package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.base.util.PageUtil;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 亚马逊爬虫基础PageProcessor，处理一些公共操作
 * @date 2016/10/12 14:42
 */
@Service
public class BasePageProcessor implements PageProcessor {

    protected Logger sLogger = Logger.getLogger(getClass());

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(3 * 1000).setTimeOut(10 * 1000);
    protected static final String URL_EXTRA = "url_extra";

    @Autowired
    protected UrlService mUrlService;

    @Override
    public void process(Page page) {
        dealValidate(page);
    }

    @Override
    public Site getSite() {
        sLogger.info("getSite()::");
//        mSite.setUserAgent(UserAgentUtil.getRandomUserAgent());
        return mSite;
    }

    /**
     * @param page 更新Url爬取状态,成功或失败
     */
    protected void updateUrlStatus(Page page) {

        Url url = (Url) page.getRequest().getExtra(URL_EXTRA);
        int statusCode = page.getStatusCode();
        sLogger.info("当前页面:" + page.getUrl() + " 爬取状态：" + statusCode);

        url.status = statusCode;
        sLogger.info("改变状态后的Url对象：" + url);

        mUrlService.update(url);
        mUrlService.updateAsinCrawledAll(url.saaAsin);
    }

    private void dealValidate(Page page) {
        String validateUrl = page.getHtml().xpath("//div[@class='a-row a-text-center']/img/@src").get();
        if (StringUtils.isNotEmpty(validateUrl)) {
            sLogger.error("身份验证,准备保存验证码...");
            PageUtil.saveImage(validateUrl, "C:\\Users\\Administrator\\Desktop\\爬虫\\amazon\\验证码2");

            String value = UrlUtils.getValue(page.getUrl().get(), "flag");
            if (StringUtils.isEmpty(value)) {
                value = "0";
            }
            String newUrl = UrlUtils.setValue(page.getUrl().get(), "flag", String.valueOf(Integer.valueOf(value) + 1));

            Request request = new Request(newUrl);
            page.addTargetRequest(request);
        }
    }
}