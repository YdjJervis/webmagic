package us.codecraft.webmagic.downloader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.samples.amazon.pojo.HtmlResponse;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfoManage;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.processor.BasePageProcessor;
import us.codecraft.webmagic.samples.amazon.service.IpsInfoManageService;
import us.codecraft.webmagic.samples.amazon.service.IpsStatService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.amazon.util.ParseUtils;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

import java.net.MalformedURLException;
import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 * @Description: 扩展固定代理IP下载器
 * @date 2016/10/28 19:47
 */
@Service
public class IpsProxyDownloader extends AbstractDownloader {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    IpsStatService mIpsStatService;
    @Autowired
    IpsInfoManageService mIpsInfoManageService;
    @Autowired
    private UrlService mUrlService;

    @Override
    public Page download(Request request, Task task) {
        ParseUtils parseUtils = new ParseUtils();

        /*获取请求URL的域名*/
        String urlHost;
        try {
            urlHost = ParseUtils.getHostByUrl(request.getUrl());
        } catch (MalformedURLException e) {
            mLogger.error(e);
            /*启用备用的代理IP（获取不到域名的URL）*/
            urlHost = "";
        }

        /*获取当前正在使用的代理IP*/
        IpsInfoManage ipsInfoManage;
        synchronized (this) {
            /*代理IP管理中，没有对应的域名则添加所有代理IP对这个域名的管理*/
            if (!mIpsInfoManageService.isExist(urlHost)) {
                mIpsInfoManageService.addIpsInfoManageAll(urlHost);
            }

            ipsInfoManage = mIpsInfoManageService.findIpInfoIsUsing(urlHost);

            if (null != ipsInfoManage) {
                /*更新代理IP使用次数及其最后使用时间*/
                ipsInfoManage.setLastUsedDate(new Date());
                ipsInfoManage.setUsedCount(ipsInfoManage.getUsedCount() + 1);
                mIpsInfoManageService.update(ipsInfoManage);
            }
        }

        if (null == ipsInfoManage) {
            ipsInfoManage = new IpsInfoManage();
        }

        /*通代理IP解析URL源码*/
        HtmlResponse htmlResponse = parseUtils.parseHtmlByProxy(request.getUrl(), ipsInfoManage.getIpHost(), ipsInfoManage.getIpPort());

        Page page = new Page();

        String htmlContent = htmlResponse.getHtmlContent();

        if (htmlContent != null) {
            page.setRawText(htmlContent);
            page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(htmlContent, request.getUrl())));
        } else {
            page.setRawText("");
        }
        page.setStatusCode(htmlResponse.getStatusCode());
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.putField("ipsType", "ipsProxy");
        page.putField("host", urlHost);
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }

    @Override
    protected void onError(Request request) {
        Url url = (Url) request.getExtra(BasePageProcessor.URL_EXTRA);
        url.sauCrawling = 0;
        mLogger.warn("HttpClient下载异常，更新URL状态：" + url);
        mUrlService.update(url);
    }
}