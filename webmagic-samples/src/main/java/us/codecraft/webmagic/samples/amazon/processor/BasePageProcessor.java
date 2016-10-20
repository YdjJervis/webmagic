package us.codecraft.webmagic.samples.amazon.processor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pojo.ImgValidateResult;
import us.codecraft.webmagic.samples.amazon.pojo.RequestStat;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.RequestStatService;
import us.codecraft.webmagic.samples.amazon.service.SiteService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.amazon.ws.validate.ImageOCRService;
import us.codecraft.webmagic.samples.base.service.UserAgentService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private UserAgentService mUserAgentService;

    @Autowired
    protected SiteService mSiteService;

    /*压力测试统计相关 -- start*/
    private Date mFirstPageTime;
    @Autowired
    private RequestStatService mStatService;
    public static final String CONDITIONS = "RandomUA-Validate-10Thread";
    public static final String CONDITIONS_CODE = DigestUtils.md5Hex(CONDITIONS);
    /*压力测试统计相关 -- end*/

    @Override
    public void process(Page page) {
        dealValidate(page);
    }

    @Override
    public Site getSite() {
        sLogger.info("getSite()::");
        mSite.setUserAgent(mUserAgentService.findRandomUA().userAgent);
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

    protected synchronized void dealValidate(Page page) {
        RequestStat stat = mStatService.find(CONDITIONS_CODE);
        if (stat == null) {
            stat = new RequestStat();
            stat.conditions = CONDITIONS;
            stat.conditionsCode = CONDITIONS_CODE;
        }
        if (stat.isFirstPage == 1) {
            stat.isFirstPage = 0;
            mFirstPageTime = new Date();
        }
        stat.firstPageTime = mFirstPageTime;

        /*
        * 1,因为需要输入验证码时，页面Url还是不变的，只有通过判断时候包好验证码图片元素来判断是否是验证码页面
        * 2,这里抽取图片的Url
        */
        String validateUrl = page.getHtml().xpath("//div[@class='a-row a-text-center']/img/@src").get();
        if (StringUtils.isNotEmpty(validateUrl)) {
            sLogger.error("身份验证,准备保存验证码...网页状态码：" + page.getStatusCode());

            /*保存图片验证码*/
//            PageUtil.saveImage(validateUrl, "C:\\Users\\Administrator\\Desktop\\爬虫\\amazon\\验证码");

            /*
            * 当需要验证码时，page的状态码还是200，不符合我们的逻辑，所以修改一下
            */
            page.setStatusCode(401);

            /*
            * 请求表单的Url，调用验证码识别接口
            */
            String validateCodeJson = getValidateCode(validateUrl, "review");
            ImgValidateResult result = new Gson().fromJson(validateCodeJson, ImgValidateResult.class);
            sLogger.info("验证码码结果：" + result);

            /*获取表单参数*/
            String domain = page.getUrl().regex("(https://www.amazon.*?)/.*").get();
            String amzn = page.getHtml().xpath("//input[@name='amzn']/@value").get();
            String amzn_r = page.getHtml().xpath("//input[@name='amzn-r']/@value").get();
            String urlStr = domain + "/errors/validateCaptcha?amzn=" + amzn + "&amzn-r=" + amzn_r + "&field-keywords=" + result.getValue();
            sLogger.info("验证表单：" + urlStr);

            Url url = (Url) page.getRequest().getExtra(URL_EXTRA);
            Request request = new Request(urlStr);
            request.putExtra(URL_EXTRA, url);
            page.addTargetRequest(request);

            /*压力测试统计相关 -- start*/
            if (stat.isFirstValidate == 1) {
                stat.isFirstValidate = 0;
                stat.firstValidateTime = new Date();
                stat.successCountBeforeValidate = stat.totalRequestCount - stat.successCount - stat.validateCount;
            }
            stat.validateCount++;
            /*压力测试统计相关 -- end*/
        } else {
            /*压力测试统计相关 -- start*/
            if (stat.isFirstValidate == 0) {
                stat.successCount++;
            }
            /*压力测试统计相关 -- end*/
        }

        /*压力测试统计相关 -- start*/
        stat.totalRequestCount++;
        stat.requestCountor++;
        /*是否到了100个请求 && 已经出现过一次验证了*/
        if (stat.requestCountor % 100 == 0 && stat.isFirstValidate == 0) {
            /*开始往大字段里面统计100个请求达到时，失败与成功的总比例，用来分析走势*/
            stat.requestCountor = 0;
            List<Float> extraList = new Gson().fromJson(stat.extra, new TypeToken<List<Float>>() {
            }.getType());
            if (CollectionUtils.isEmpty(extraList)) {
                extraList = new ArrayList<Float>();
            }
            extraList.add(stat.validateCount * 1.0f / stat.successCount);
            stat.extra = new Gson().toJson(extraList);
        }
        mStatService.addOnDuplicate(stat);
        /*压力测试统计相关 -- end*/
    }

    /**
     * 根据Url提取域名，并根据域名获取站点码
     */
    protected String extractSiteCode(Page page) {
        String domain = page.getUrl().regex("(https://www.amazon.*?)/.*").get();
        return mSiteService.findByDomain(domain).basCode;
    }

    /**
     * 调用第三方验证码识别接口
     *
     * @param imgUrl 图片Url
     * @return 验证码
     */
    private String getValidateCode(String imgUrl, String type) {
        ImageOCRService service = new ImageOCRService();
        String validateCode = service.getBasicHttpBindingIImageOCRService().getVerCodeFromUrl(imgUrl, type);
        return validateCode;
    }
}