package us.codecraft.webmagic.downloader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hardy
 * @version V0.1
 * @Description: 扩展固定代理IP下载器
 * @date 2016/10/28 19:47
 */
@Service
public class IpsProxyDownloader extends HttpClientDownloader {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    IpsStatService mIpsStatService;
    @Autowired
    IpsInfoManageService mIpsInfoManageService;
    @Autowired
    private UrlService mUrlService;


    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    @Override
    public Page download(Request request, Task task) {

        ParseUtils parseUtils = new ParseUtils();

        /*获取请求URL的域名*/
        String urlHost;
        try {
            urlHost = ParseUtils.getHostByUrl(request.getUrl());
        } catch (MalformedURLException e) {
            mLogger.error(e);
            /*启用备用域的代理IP（获取不到域名的URL）*/
            urlHost = "";
        }

        /*获取当前正在使用的代理IP*/
        IpsInfoManage ipsInfoManage = new IpsInfoManage();

        synchronized (this) {
            /*代理IP管理中，没有对应的域名则添加所有代理IP对这个域名的管理*/
            if (!mIpsInfoManageService.isExist(urlHost)) {
                Url url = (Url)request.getExtra(BasePageProcessor.URL_EXTRA);
                String basCode = url.siteCode;
                mIpsInfoManageService.addIpsInfoManageAll(urlHost, basCode);
            }

            List<IpsInfoManage> ipsInfoManageList = mIpsInfoManageService.findIpInfoIsUsing(urlHost);

            if (null != ipsInfoManageList && ipsInfoManageList.size() > 0) {
                /*更新代理IP使用次数及其最后使用时间*/
                ipsInfoManage = ipsInfoManageList.get(0);
                ipsInfoManage.setLastUsedDate(new Date());
                ipsInfoManage.setUsedCount(ipsInfoManage.getUsedCount() + 1);
                mIpsInfoManageService.update(ipsInfoManage);
            }
        }

        if (null == ipsInfoManage) {
            ipsInfoManage = new IpsInfoManage();
        }


        /*通代理IP解析URL源码*/
        HtmlResponse htmlResponse = parseUtils.parseHtmlByProxy(request.getUrl(), ipsInfoManage, urlHost);


        CloseableHttpResponse httpResponse = null;
        int statusCode = 0;

        String htmlContent = null;
        Page page = new Page();
        if(httpResponse != null) {
            statusCode = httpResponse.getStatusLine().getStatusCode();



        } else {
            statusCode = 417;
        }

        if (htmlContent != null) {
            page.setRawText(htmlContent);
            page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(htmlContent, request.getUrl())));
        } else {
            page.setRawText("");
        }
        page.setStatusCode(statusCode);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.getRequest().putExtra("ipsType", "ipsProxy");
        page.getRequest().putExtra("host", urlHost);
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }

    @Override
    protected void onError(Request request) {
        Url url = (Url) request.getExtra(BasePageProcessor.URL_EXTRA);
        url.crawling = 0;
        mLogger.warn("HttpClient下载异常，更新URL状态：" + url);
        mUrlService.update(url);
    }
}