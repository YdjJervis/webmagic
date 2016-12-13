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
import us.codecraft.webmagic.samples.amazon.util.ValidateProxyUtils;
import us.codecraft.webmagic.samples.base.service.UserAgentService;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.UrlUtils;

import java.util.Date;
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

    private Set<Integer> mSet = Sets.newHashSet(0, 402, 403, 404, 407, 417, 429, 503);
    private Set<Integer> mDealSet = Sets.newHashSet(0, 200, 402, 403, 404, 407, 417, 429, 503);

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
    protected UrlBatchStatService mUrlBatchStatService;

    @Autowired
    protected HttpClientImplDownloader sDownloader;

    @Autowired
    protected AbuProxyDownloader mAbuProxyDownloader;

    @Autowired
    private HttpClientImplDownloader mHttpClientImplDownloader;

    @Autowired
    private IpsProxyHttpClientDownloader mIpsProxyHttpClientDownloader;

    @Autowired
    private NoSellService mNoSellService;

    @Override
    public synchronized void process(Page page) {
        sLogger.info("process(Page page)::URL=" + page.getUrl() + " StatusCode=" + page.getStatusCode());
        page = IsNotNullAndProxyCaptcha(page);

        /*记录每一批次解析的URL的问题以及对应异常状态码出现的次数*/
        ipsExceptionStatusStat(page);

        /*监测对应URL的代理使用情况*/
        //statUrlProxy(page);

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
        mSite.setUserAgent(mUserAgentService.findRandomUA().userAgent).setAcceptStatCode(mDealSet);
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
    void dealPageNotFound(Page page) {

        Site site = extractSite(page);
        String asin = extractAsin(page);
        mAsinService.updateAndDeleteUrl(site.basCode, asin);

        /* 二期业务：如果页面不存在，就把所有的记录的进度更新成1，type改成2(全量爬取完毕) */
        BatchAsin batchAsin = mBatchAsinService.findAllByAsin(getUrl(page).batchNum, site.basCode, asin);
        batchAsin.status = 4;
        batchAsin.progress = 1;
        batchAsin.finishTime = new Date();
        mBatchAsinService.update(batchAsin);

        /* 添加到下架表里 */
        mNoSellService.add(new Asin(site.basCode, asin));

    }

    protected Url getUrl(Page page) {
        return (Url) page.getRequest().getExtra(URL_EXTRA);
    }

    /**
     * @param page 更新Url爬取状态,成功或失败
     */
    private void updateUrlStatus(Page page) {
        Url url = getUrl(page);

        if (needUpdateStatus()) {
            int statusCode = page.getStatusCode();
            sLogger.info("当前页面:" + page.getUrl() + " 爬取状态：" + statusCode);

            if (statusCode == 407 || statusCode == 0 || statusCode == 402) {
                if (page.getRequest() != null && page.getRequest().getExtra("ipsType") != null && page.getRequest().getExtra("host") != null) {
                    mIpsStatService.switchIp((String) page.getRequest().getExtra("ipsType"), (String) page.getRequest().getExtra("host"), statusCode);
                }
            }

            url.status = statusCode;
        }

        url.crawling = 0;
        url.times++;
        sLogger.info("改变状态后的Url对象：" + url);

        mUrlService.update(url);
    }

    private void dealValidate(Page page) {

        String validateUrl = getValidateUrl(page);

        if (StringUtils.isNotEmpty(validateUrl)) {
            if (page.getRequest() != null && page.getRequest().getExtra("ipsType") != null && page.getRequest().getExtra("host") != null) {
                mIpsStatService.switchIp((String) page.getRequest().getExtra("ipsType"), (String) page.getRequest().getExtra("host"), -1);
            }
        }

        sLogger.warn("身份验证,准备保存验证码...网页状态码：" + page.getStatusCode());

        /*保存图片验证码*/
        //PageUtil.saveImage(validateUrl, "C:\\Users\\Administrator\\Desktop\\爬虫\\amazon\\验证码");

        /* 当需要验证码时，page的状态码还是200，不符合我们的逻辑，所以修改一下 */
//        page.setStatusCode(401);

        Request request;
        Url url = (Url) page.getRequest().getExtra(URL_EXTRA);
        /*
        * 请求表单的Url，调用验证码识别接口
        */
        if (page.getRequest().getExtra("ipsType") == null) {
            String validateCodeJson = getValidateCode(validateUrl, "review");
            ImgValidateResult result = new Gson().fromJson(validateCodeJson, ImgValidateResult.class);
            sLogger.info("验证码码结果：" + result);

            /*获取表单参数*/
            String domain = page.getUrl().regex("(https://www.amazon.*?)/.*").get();
            String amzn = page.getHtml().xpath("//input[@name='amzn']/@value").get();
            String amzn_r = page.getHtml().xpath("//input[@name='amzn-r']/@value").get();
            String urlStr = domain + "/errors/validateCaptcha?amzn=" + amzn + "&amzn-r=" + amzn_r + "&field-keywords=" + result.getValue();
            sLogger.info("验证表单：" + urlStr);
            request = new Request(urlStr);
            request.putExtra(URL_EXTRA, url);
            page.addTargetRequest(request);
        } else {
            sLogger.info("================ 使用代理出现验证码，将响应的状态码修改为0.");
            page.setStatusCode(0);
            updateUrlStatus(page);
        }
    }

    /**
     * 获取验证码表单请求URL
     */
    private String getValidateFormUrl(Page page) {
        String validateUrl = getValidateUrl(page);
        String validateCodeJson = getValidateCode(validateUrl, "review");
        ImgValidateResult result = new Gson().fromJson(validateCodeJson, ImgValidateResult.class);
        sLogger.info("验证码码结果：" + result);
        /*获取表单参数*/
        String domain = page.getUrl().regex("(https://www.amazon.*?)/.*").get();
        String amzn = page.getHtml().xpath("//input[@name='amzn']/@value").get();
        String amzn_r = page.getHtml().xpath("//input[@name='amzn-r']/@value").get();
        return domain + "/errors/validateCaptcha?amzn=" + amzn + "&amzn-r=" + amzn_r + "&field-keywords=" + result.getValue();
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
     * 通过代理下载页面，返回为只有标签没有内容
     */
    private boolean isNullHtml(Page page) {
        return StringUtils.isEmpty(page.getHtml().xpath("//body/html()").get());
    }

    /**
     * @return 是否是评论页面
     */
    private boolean isReviewPage(Page page) {
        return page.getUrl().get().contains(Review.PRODUCT_REVIEWS);
    }

    /**
     * 对代理打码
     */
    private String proxyCaptcha(Page page) {
        IpsInfoManage ipsInfoManage = (IpsInfoManage) page.getRequest().getExtra("proxyIpInfo");
        String validateFormUrl = getValidateFormUrl(page);
        ValidateProxyUtils validateProxyUtils = new ValidateProxyUtils();
        return validateProxyUtils.parseValidUrl(ipsInfoManage.getIpHost(), Integer.valueOf(ipsInfoManage.getIpPort()), ipsInfoManage.getIpVerifyUserName(), ipsInfoManage.getIpVerifyPassword(), validateFormUrl);
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
                    .setDownloader(mHttpClientImplDownloader)
                    .thread(5);

            for (Url url : urlList) {
                Request request = new Request(url.url);
                request.putExtra(URL_EXTRA, url);
                mSpider.addRequest(request);

                url.crawling = 1;
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
     * 判断解析内容是不是为空，如果出现验证码则打码返回结果,更新page对应状态
     */
    private Page IsNotNullAndProxyCaptcha(Page page) {
        if (isNullHtml(page)) {
            /*通过代理解析url返回内容为空，则将状态码改为402，让其重新解析*/
            sLogger.info("parse " + page.getUrl().get() + " content is empty.");
            page.setStatusCode(402);
        } else {
            if (isValidatePage(page)) {
                IpsInfoManage ipsInfoManage = (IpsInfoManage) page.getRequest().getExtra("proxyIpInfo");
                if (ipsInfoManage != null) {
                    sLogger.info("解析url(" + page.getRequest().getUrl() + ")出现验证码，需要对代理(" + ipsInfoManage.getIpHost() + ":" + ipsInfoManage.getIpPort() + ")打码");
                    String html = proxyCaptcha(page);
                    if (StringUtils.isNotEmpty(html)) {
                        page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(html, page.getRequest().getUrl())));
                        if (StringUtils.isNotEmpty(getValidateUrl(page))) {
                            sLogger.info("解析url(" + page.getRequest().getUrl() + ")出现验证码，代理(" + ipsInfoManage.getIpHost() + ":" + ipsInfoManage.getIpPort() + ")打码失败.");
                            page.setStatusCode(0);
                        } else {
                            sLogger.info("解析url(" + page.getRequest().getUrl() + ")出现验证码，代理(" + ipsInfoManage.getIpHost() + ":" + ipsInfoManage.getIpPort() + ")打码成功.");
                        }
                    }
                }
            }
        }
        return page;
    }

    /**
     * 监测对应批次下的url使用代理解析情况
     */
    private void statUrlProxy(Page page) {
        /*是否使用代理*/
        int isProxy = page.getRequest().getExtra("ipsType") == null ? 0 : 1;
        /*解析url正常或异常*/
        String parseStatus = page.getStatusCode() == 200 ? "correct" : "exception";

        /*通过url获取这个URL的asin*/
        String asinCode = extractAsin(page);
        /*当前url*/
        String url = page.getUrl().get();
        if (StringUtils.isNotEmpty(asinCode)) {
            /*通过asin来查询这个URL所对应的批次信息*/
            BatchAsin batchAsin = mBatchAsinService.findAllByAsin(getUrl(page).batchNum, getUrl(page).siteCode, asinCode);
            /*查询批次下的url代理监测信息*/
            UrlBatchStat urlBatchStat = mUrlBatchStatService.findByBatchAndUrl(batchAsin.batchNumber, url);
            if (urlBatchStat != null) {
                /*存在则更新状态*/
                if ("correct".equals(parseStatus)) {
                    urlBatchStat.setCorrectTime(urlBatchStat.getCorrectTime() + 1);
                } else {
                    urlBatchStat.setExceptionTime(urlBatchStat.getExceptionTime() + 1);
                }
                /*更新正确次数或异常次数*/
                mUrlBatchStatService.updateById(urlBatchStat);
            } else {
                /*不存在则添加*/
                urlBatchStat = new UrlBatchStat();
                urlBatchStat.setUrl(url);
                urlBatchStat.setIsProxy(isProxy);
                urlBatchStat.setBatchNum(batchAsin.batchNumber);
                if ("correct".equals(parseStatus)) {
                    urlBatchStat.setCorrectTime(1);
                    urlBatchStat.setExceptionTime(0);
                } else {
                    urlBatchStat.setCorrectTime(0);
                    urlBatchStat.setExceptionTime(1);
                }
                mUrlBatchStatService.addOne(urlBatchStat);
            }
        }
    }
}