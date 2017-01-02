package com.eccang.spider.downloader;

import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.proxy.Proxy;
import com.eccang.spider.amazon.pojo.dict.IpsInfoManage;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.processor.BasePageProcessor;
import com.eccang.spider.amazon.service.dict.IpsInfoManageService;
import com.eccang.spider.amazon.util.ParseUtils;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.HttpConstant;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * The http downloader based on HttpClient.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
@Service
@ThreadSafe
public class IpsProxyHttpClientDownloader extends AbstractDownloader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IpsInfoManageService mIpsInfoManageService;

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private CloseableHttpClient getHttpClient(Site site, Proxy proxy) {
        if (site == null) {
            return httpClientGenerator.getClient(null, proxy);
        }
        CloseableHttpClient httpClient = null;
        httpClient = httpClientGenerator.getClient(site, proxy);
        return httpClient;
    }

    @Override
    public Page download(Request request, Task task) {
        Site site = null;
        if (task != null) {
            site = task.getSite();
        }
        Set<Integer> acceptStatCode;
        String charset = null;
        Map<String, String> headers = null;
        if (site != null) {
            acceptStatCode = site.getAcceptStatCode();
            charset = site.getCharset();
            headers = site.getHeaders();
        } else {
            acceptStatCode = Sets.newHashSet(200);
        }
        logger.info("downloading page {}", request.getUrl());
        CloseableHttpResponse httpResponse = null;
        int statusCode = 0;
        HttpHost proxyHost = null;
        Proxy proxy = null;
        Page page = null;
        String urlHost = null;
        CloseableHttpClient closeableHttpClient = null;
        IpsInfoManage ipsInfoManage = null;
        try {
            /*获取当前是IP池中的IP并将IP配置到请求中*/
            urlHost = getUrlHost(request.getUrl());
            Url url = (Url) request.getExtra(BasePageProcessor.URL_EXTRA);
            String basCode = url.siteCode;
            ipsInfoManage = getPoxyHost(urlHost, basCode);
            logger.info("use proxy {}", ipsInfoManage.getIpHost() + ":" + ipsInfoManage.getIpPort());
            if (ipsInfoManage.getIpVerifyUserName() != null && !"".equals(ipsInfoManage.getIpVerifyUserName())) {
                site.setUsernamePasswordCredentials(new UsernamePasswordCredentials(ipsInfoManage.getIpVerifyUserName(), ipsInfoManage.getIpVerifyPassword()));
            }
            proxyHost = new HttpHost(ipsInfoManage.getIpHost(), Integer.valueOf(ipsInfoManage.getIpPort()));
            site.setHttpProxy(proxyHost);

            Long startTime = System.currentTimeMillis();

            /*下载页面*/
            HttpUriRequest httpUriRequest = getHttpUriRequest(request, site, headers, proxyHost);
            closeableHttpClient = getHttpClient(site, proxy);
            httpResponse = closeableHttpClient.execute(httpUriRequest);

            Long entTime = System.currentTimeMillis();
            logger.info("======================= 固定代理IP(" + proxyHost.getHostName() + ":" + proxyHost.getPort() + ")解析URL，所需要的时间:" + (entTime - startTime) / 1000 + "s.");
            statusCode = httpResponse.getStatusLine().getStatusCode();
            request.putExtra(Request.STATUS_CODE, statusCode);
            page = handleResponse(request, charset, httpResponse);
            logger.info("固定代理IP(" + proxyHost.getHostName() + ":" + proxyHost.getPort() + ")解析URL(" + request.getUrl() + ")，返回状态码：" + statusCode);
            if(statusCode == 407) {
                System.out.println(statusCode);
            }
            if (!statusAccept(acceptStatCode, statusCode)) {
                page.setStatusCode(statusCode);
                /*添加可接受的状态码set对象不存在的状态码*/
                acceptStatCode.add(statusCode);
                site.setAcceptStatCode(acceptStatCode);
            }
        } catch (Exception e) {
            logger.warn("download page " + request.getUrl() + " error", e);
        } finally {
            try {
                if (httpResponse != null) {
                    //ensure the connection is released back to pool
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (IOException e) {
                logger.warn("close response fail", e);
            }

            if (page == null) {
                page = new Page();
                page.setRawText("");
            } else {
                if (page.getRawText() == null) {
                    page.setRawText("");
                }
            }
            /*解析发生异常时，状态码默认为0*/
            page.setStatusCode(statusCode);
            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
            page.getRequest().putExtra("ipsType", "ipsProxy");
            page.getRequest().putExtra("host", urlHost);
            page.getRequest().putExtra("proxyIpInfo", ipsInfoManage);
        }
        return page;
    }

    /**
     * 获取固定IP池中正在使用的IP
     */
    protected synchronized IpsInfoManage getPoxyHost(String urlHost, String basCode) {
        /*获取当前正在使用的代理IP*/
        IpsInfoManage ipsInfoManage;

        /*代理IP管理中，没有对应的域名则添加所有代理IP对这个域名的管理*/
        if (!mIpsInfoManageService.isExist(urlHost)) {
            mIpsInfoManageService.addIpsInfoManageAll(urlHost, basCode);
        }
        /*查询对应域名下正在使用的IP列表*/
        List<IpsInfoManage> ipsInfoManageList = mIpsInfoManageService.findIpInfoIsUsing(urlHost);

        if (null == ipsInfoManageList || ipsInfoManageList.size() == 0) {
            ipsInfoManage = mIpsInfoManageService.getUsingIp(urlHost);
        } else {
            ipsInfoManage = ipsInfoManageList.get(0);
        }

        /*更新代理IP使用次数及其最后使用时间*/
        ipsInfoManage.setLastUsedDate(new Date());
        ipsInfoManage.setUsedCount(ipsInfoManage.getUsedCount() + 1);
        mIpsInfoManageService.update(ipsInfoManage);
        return ipsInfoManage;
    }

    /**
     * 获取url域名
     */
    protected String getUrlHost(String url) {
        /*获取请求URL的域名*/
        String urlHost;
        try {
            urlHost = ParseUtils.getHostByUrl(url);
        } catch (MalformedURLException e) {
            logger.error(e.toString());
            /*启用备用域的代理IP（获取不到域名的URL）*/
            urlHost = "";
        }
        return urlHost;
    }

    @Override
    public void setThread(int thread) {
        httpClientGenerator.setPoolSize(thread);
    }

    protected boolean statusAccept(Set<Integer> acceptStatCode, int statusCode) {
        return acceptStatCode.contains(statusCode);
    }

    /**
     * 设置httpclient的请求参数（请求头参数，超时时间，代理信息等）
     */
    protected HttpUriRequest getHttpUriRequest(Request request, Site site, Map<String, String> headers, HttpHost proxy) {
        RequestBuilder requestBuilder = selectRequestMethod(request).setUri(request.getUrl());
        if (headers != null) {
            for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(site.getTimeOut())
                .setSocketTimeout(site.getTimeOut())
                .setConnectTimeout(site.getTimeOut())
                .setCookieSpec(CookieSpecs.BEST_MATCH);
        if (proxy != null) {
            requestConfigBuilder.setProxy(proxy);
            request.putExtra(Request.PROXY, proxy);
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        return requestBuilder.build();
    }

    /**
     * 选择httpclient请求方式
     */
    protected RequestBuilder selectRequestMethod(Request request) {
        String method = request.getMethod();
        if (method == null || method.equalsIgnoreCase(HttpConstant.Method.GET)) {
            //default get
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.POST)) {
            RequestBuilder requestBuilder = RequestBuilder.post();
            NameValuePair[] nameValuePair = (NameValuePair[]) request.getExtra("nameValuePair");
            if (nameValuePair != null && nameValuePair.length > 0) {
                requestBuilder.addParameters(nameValuePair);
            }
            return requestBuilder;
        } else if (method.equalsIgnoreCase(HttpConstant.Method.HEAD)) {
            return RequestBuilder.head();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.PUT)) {
            return RequestBuilder.put();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.DELETE)) {
            return RequestBuilder.delete();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.TRACE)) {
            return RequestBuilder.trace();
        }
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

    /**
     * 将解析响应的结果封装到page对象里
     */
    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse) throws IOException {
        String content = getContent(charset, httpResponse);
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        return page;
    }

    /**
     * 将响应字节流按编码转换成字符串
     */
    protected String getContent(String charset, HttpResponse httpResponse) throws IOException {
        if (charset == null) {
            byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            String htmlCharset = getHtmlCharset(httpResponse, contentBytes);
            if (htmlCharset != null) {
                return new String(contentBytes, htmlCharset);
            } else {
                logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
                return new String(contentBytes);
            }
        } else {
            return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
        }
    }

    /**
     * 获取解析到的URL内容的编码
     */
    protected String getHtmlCharset(HttpResponse httpResponse, byte[] contentBytes) throws IOException {
        String charset;
        // charset
        // 1、encoding in http header Content-Type
        String value = httpResponse.getEntity().getContentType().getValue();
        charset = UrlUtils.getCharset(value);
        if (StringUtils.isNotBlank(charset)) {
            logger.debug("Auto get charset: {}", charset);
            return charset;
        }
        // use default charset to decode first time
        Charset defaultCharset = Charset.defaultCharset();
        String content = new String(contentBytes, defaultCharset.name());
        // 2、charset in meta
        if (StringUtils.isNotEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements links = document.select("meta");
            for (Element link : links) {
                // 2.1、html4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                String metaContent = link.attr("content");
                String metaCharset = link.attr("charset");
                if (metaContent.indexOf("charset") != -1) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                    charset = metaContent.split("=")[1];
                    break;
                }
                // 2.2、html5 <meta charset="UTF-8" />
                else if (StringUtils.isNotEmpty(metaCharset)) {
                    charset = metaCharset;
                    break;
                }
            }
        }
        logger.debug("Auto get charset: {}", charset);
        return charset;
    }
}
