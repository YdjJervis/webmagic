package us.codecraft.webmagic.samples.amazon.processor;

import com.eccang.wsclient.validate.ImageOCRService;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.AbuProxyDownloader;
import us.codecraft.webmagic.downloader.HttpClientImplDownloader;
import us.codecraft.webmagic.downloader.IpsProxyHttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.service.*;
import us.codecraft.webmagic.samples.base.service.UserAgentService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 亚马逊爬虫基础PageProcessor，处理一些公共操作
 * @date 2016/10/27
 */
@Service
public class BasePageProcessor implements PageProcessor {

    Logger sLogger = Logger.getLogger(getClass());
    private us.codecraft.webmagic.Site mSite = us.codecraft.webmagic.Site.me().setRetryTimes(3).setSleepTime(10 * 1000).setTimeOut(10 * 1000);

    public static final String URL_EXTRA = "url_extra";

    private Set<Integer> mSet = new HashSet<Integer>() {
        {
            add(402);
            add(403);
            add(407);
            add(417);
            add(429);
            add(503);
        }
    };

    @Autowired
    protected UrlService mUrlService;

    @Autowired
    protected IpsStatService mIpsStatService;

    @Autowired
    private UserAgentService mUserAgentService;

    @Autowired
    protected SiteService mSiteService;

    @Autowired
    protected AsinService mAsinService;

    @Autowired
    protected IpsSwitchManageService mIpsSwitchManageService;

    @Autowired
    protected IpsInfoManageService mIpsInfoManageService;

    @Autowired
    protected BatchAsinService mBatchAsinService;

    @Autowired
    protected HttpClientImplDownloader sDownloader;

    @Autowired
    protected AbuProxyDownloader mAbuProxyDownloader;

    @Autowired
    private HttpClientImplDownloader mHttpClientImplDownloader;

    @Autowired
    private IpsProxyHttpClientDownloader mIpsProxyHttpClientDownloader;

    @Override
    public synchronized void process(Page page) {
        sLogger.info("process(Page page)::URL=" + page.getUrl() + " StatusCode=" + page.getStatusCode());
        //Url url = (Url) page.getRequest().getExtra(URL_EXTRA);
        /*监测对应URL的代理使用情况*/


        /*记录每一批次解析的URL的问题以及对应异常状态码出现的次数*/
        ipsExceptionStatusStat(page);

        updateUrlStatus(page);

        if (isPage404(page)) {
            dealPageNotFound(page);
        } else if (mSet.contains(page.getStatusCode())) {
            //包括mSet里的状态码，则不做处理
        } else if (isValidatePage(page)) {
            dealValidate(page);
        } else if (isReviewPage(page)) {
            dealReview(page);
        } else {
            dealOtherPage(page);
        }
    }

    /**
     * 处理其它页码，对亚马逊页面爬取的扩展
     */
    protected void dealOtherPage(Page page) {
    }

    @Override
    public us.codecraft.webmagic.Site getSite() {
        sLogger.info("getSite()::");
        mSite.setUserAgent(mUserAgentService.findRandomUA().userAgent).setAcceptStatCode(Sets.newHashSet(200, 402, 403, 404, 407, 417, 429, 503));
        return mSite;
    }

    /**
     * 处理Review。不同子类可以覆写后做不同处理
     */
    protected void dealReview(Page page) {
    }

    /**
     * 更新URL的时候是否需要更新状态码。全量爬取的时候需要，
     * 监听爬取的时候不需要。
     */
    protected boolean needUpdateStatus() {
        return true;
    }

    /**
     * 处理不工作的页面。eg:下架页面
     */
    private void dealPageNotFound(Page page) {

        String asinCode = extractAsin(page);
        mAsinService.updateAndDeleteUrl(asinCode);

        /* 二期业务：如果页面不存在，就把所有的记录的进度更新成1，type改成1(全量爬取完毕) */
        List<BatchAsin> batchAsinList = mBatchAsinService.findAllByAsin(asinCode);
        for (BatchAsin batchAsin : batchAsinList) {
            batchAsin.progress = 1;
            batchAsin.type = 1;
        }
        mBatchAsinService.updateAll(batchAsinList);

    }

    /**
     * @param page 更新Url爬取状态,成功或失败
     */
    private void updateUrlStatus(Page page) {
        Url url = (Url) page.getRequest().getExtra(URL_EXTRA);

        if (needUpdateStatus()) {
            int statusCode = page.getStatusCode();
            sLogger.info("当前页面:" + page.getUrl() + " 爬取状态：" + statusCode);

            if (statusCode == 407 || statusCode == 417) {
                if (page.getRequest() != null && page.getRequest().getExtra("ipsType") != null && page.getRequest().getExtra("host") != null) {
                    mIpsStatService.switchIp((String) page.getRequest().getExtra("ipsType"), (String) page.getRequest().getExtra("host"), statusCode);
                }
            }

            url.status = statusCode;
        }

        url.sauCrawling = 0;
        url.times++;
        sLogger.info("改变状态后的Url对象：" + url);

        mUrlService.update(url);
    }

    private void dealValidate(Page page) {

        String validateUrl = getValidateUrl(page);

        if (StringUtils.isNotEmpty(validateUrl)) {
            if (page.getRequest() != null && page.getRequest().getExtra("ipsType") != null && page.getRequest().getExtra("host") != null) {
                mIpsStatService.switchIp((String) page.getRequest().getExtra("ipsType"), (String) page.getRequest().getExtra("host"), 0);
            }
        }

        sLogger.warn("身份验证,准备保存验证码...网页状态码：" + page.getStatusCode());

        /*保存图片验证码*/
        //PageUtil.saveImage(validateUrl, "C:\\Users\\Administrator\\Desktop\\爬虫\\amazon\\验证码");

        /* 当需要验证码时，page的状态码还是200，不符合我们的逻辑，所以修改一下 */
//        page.setStatusCode(401);

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

    }

    private String getValidateUrl(Page page) {
        return page.getHtml().xpath("//div[@class='a-row a-text-center']/img/@src").get();
    }

    /**
     * 是否是验证码页面。因为需要输入验证码时，页面Url还是不变的，
     * 只有通过判断时候包好验证码图片元素来判断是否是验证码页面。
     */
    private boolean isValidatePage(Page page) {
        return StringUtils.isNotEmpty(getValidateUrl(page)) || page.getUrl().get().contains("validateCaptcha");
    }

    /**
     * @return 是否是404页面
     */
    private boolean isPage404(Page page) {
        return page.getStatusCode() == 404;
    }

    /**
     * @return 是否是评论页面
     */
    private boolean isReviewPage(Page page) {
        return page.getUrl().get().contains(Review.PRODUCT_REVIEWS);
    }

    /**
     * 根据Url提取其中ASIN码
     */
    String extractAsin(Page page) {
        return page.getUrl().regex(".*product-reviews/([0-9a-zA-Z\\-]*).*").get();
    }

    /**
     * 根据Url提取域名，并根据域名获取站点码
     */
    Site extractSite(Page page) {
        String domain = page.getUrl().regex("(https://www.amazon.*?)/.*").get();
        return mSiteService.findByDomain(domain);
    }

    /**
     * 调用第三方验证码识别接口
     *
     * @param imgUrl 图片Url
     * @return 验证码
     */
    private String getValidateCode(String imgUrl, String type) {
        ImageOCRService service = new ImageOCRService();
        return service.getBasicHttpBindingIImageOCRService().getVerCodeFromUrl(imgUrl, type);
    }

    void startToCrawl(List<Url> urlList) {
        sLogger.info("找到状态码不为200的Url个数：" + urlList.size());
        if (CollectionUtils.isNotEmpty(urlList)) {

            Spider mSpider = Spider.create(this)
                    .setDownloader(mIpsProxyHttpClientDownloader)
                    .thread(1);

            for (Url url : urlList) {
                Request request = new Request(url.url);
                request.putExtra(URL_EXTRA, url);
                mSpider.addRequest(request);

                url.sauCrawling = 1;
                mUrlService.update(url);
            }

            sLogger.info("开始爬取评论...");
            mSpider.start();
        }
    }

    /**
     * 记录每一批次解析的URL的问题以及对应异常状态码出现的次数
     */
    private void ipsExceptionStatusStat(Page page) {
        mIpsSwitchManageService.ipsSwitchManageExceptionRecord(page);
    }

    /**
     * 监测对应批次下的url使用代理解析情况
     */
    void stataUrlProxy(Page page) {
        /*通过url获取这个URL的asin*/
        String asinCode = page.getUrl().regex(".*product-reviews/([0-9a-zA-Z\\-]*).*").get();
        if (asinCode != null && !"".equals(asinCode)) {
            /*通过asin来查询这个URL所对应的批次信息*/
            List<BatchAsin> batchAsinList = mBatchAsinService.findAllByAsin(asinCode);
            StringBuffer sb;
            if (batchAsinList != null && batchAsinList.size() > 0) {
                sb = new StringBuffer();
                for (BatchAsin batchAsin : batchAsinList) {
                    sb.append(batchAsin.batchNumber + ";");
                }
            }
        }
    }
}