package com.eccang.spider.amazon.util;

import com.eccang.spider.amazon.R;
import com.eccang.wsclient.validate.ImageOCRService;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.proxy.Proxy;
import com.eccang.spider.amazon.pojo.ImgValidateResult;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.HttpConstant;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;


/***
 * 通过亚马逊代理验证
 */
@ThreadSafe
public class ValidateProxyUtils {

    private static final Logger logger = LoggerFactory.getLogger(R.BusinessLog.PUBLIC);

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private CloseableHttpClient getHttpClient(Site site, Proxy proxy) {
        if (site == null) {
            return httpClientGenerator.getClient(null, proxy);
        }
        CloseableHttpClient httpClient = null;
        httpClient = httpClientGenerator.getClient(site, proxy);

        return httpClient;
    }

    /**
     * 验证代理
     */
    public boolean isValidate(String proxyHost, Integer proxyIp, String userName, String password) {
        Request request = new Request();
        /*验证url*/
        request.setUrl("https://www.amazon.com/product-reviews/B01KT1GYX6?filterByStar=all&pageNumber=101");
        Site site = new Site();
        /*设置请求参数*/
        site.setTimeOut(20 * 1000);
        site.setRetrySleepTime(1000);
        logger.info("validating proxy {}:{}", proxyHost, proxyIp);
        CloseableHttpResponse httpResponse = null;
        int statusCode = 0;
        Proxy proxy = null;
        HttpHost httpHost = null;
        CloseableHttpClient closeableHttpClient = null;
        try {
            httpHost = new HttpHost(proxyHost, proxyIp);
            site.setHttpProxy(httpHost);
            if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
                site.setUsernamePasswordCredentials(new UsernamePasswordCredentials(userName, password));
            }

            /*下载页面*/
            HttpUriRequest httpUriRequest = getHttpUriRequest(request, site, null, httpHost);
            closeableHttpClient = getHttpClient(site, proxy);
            httpResponse = closeableHttpClient.execute(httpUriRequest);
            String html = getContent("utf-8", httpResponse);
            System.out.println(html);
            statusCode = httpResponse.getStatusLine().getStatusCode();

            logger.info("parse Amazon url statusCode : {}", statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            logger.error("validate proxy " + proxyHost + ":" + proxyIp + " fail", e);
            return false;
        } finally {
            try {
                if (httpResponse != null) {
                    //ensure the connection is released back to pool
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (IOException e) {
                logger.error("close response fail", e);
            }
        }
    }

    /**
     * 对代理IP进行打码
     */
    public String parseValidUrl(String proxyHost, Integer proxyIp, String userName, String password, String url) {
        Request request = new Request();
        /*验证url*/
        request.setUrl(url);
        Site site = new Site();
        /*设置请求参数*/
        site.setTimeOut(10 * 1000);
        site.setRetrySleepTime(1000);
        logger.info("validating proxy {}:{}", proxyHost, proxyIp);
        CloseableHttpResponse httpResponse = null;
        int statusCode = 0;
        Proxy proxy = null;
        HttpHost httpHost = null;
        CloseableHttpClient closeableHttpClient = null;
        String html = null;
        try {
            httpHost = new HttpHost(proxyHost, proxyIp);
            site.setHttpProxy(httpHost);
            if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
                site.setUsernamePasswordCredentials(new UsernamePasswordCredentials(userName, password));
            }

            /*下载页面*/
            HttpUriRequest httpUriRequest = getHttpUriRequest(request, site, null, httpHost);
            closeableHttpClient = getHttpClient(site, proxy);
            httpResponse = closeableHttpClient.execute(httpUriRequest);
            html = getContent("utf-8", httpResponse);
            statusCode = httpResponse.getStatusLine().getStatusCode();

            logger.info("parse Amazon url statusCode : {}", statusCode);
        } catch (IOException e) {
            logger.error("validate proxy {}:{} fail", proxyHost, proxyIp, e);
        } finally {
            try {
                if (httpResponse != null) {
                    //ensure the connection is released back to pool
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (IOException e) {
                logger.error("close response fail", e);
            }
        }
        return html;
    }

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

    /**
     * 获取验证码URL
     */
    public String getValidUrl(String url, Html html) {
        String validateUrl = html.xpath("//div[@class='a-row a-text-center']/img/@src").get();
        String validateCodeJson = getValidateCode(validateUrl, "review");
        ImgValidateResult result = new Gson().fromJson(validateCodeJson, ImgValidateResult.class);
        logger.info("验证码码结果：{}", result);

            /*获取表单参数*/
        String domain = new PlainText(url).regex("(https://www.amazon.*?)/.*").get();
        String amzn = html.xpath("//input[@name='amzn']/@value").get();
        String amzn_r = html.xpath("//input[@name='amzn-r']/@value").get();
        String urlStr = domain + "/errors/validateCaptcha?amzn=" + amzn + "&amzn-r=" + amzn_r + "&field-keywords=" + result.getValue();
        return urlStr;
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

//    public static void main(String[] args) {
//        String url = "https://www.amazon.com/product-reviews/B01KT1GYX6?filterByStar=all&pageNumber=101";
//        ValidateProxyUtils utils = new ValidateProxyUtils();
//        new PlainText(url);
//
//        String htmlStr = utils.parseValidUrl("104.217.205.19", 8080, "hanyun45314", "hanyun45314", "https://www.amazon.com/product-reviews/B01KT1GYX6?filterByStar=all&pageNumber=101");
//        Html html = new Html(Jsoup.parse(htmlStr));
//        if(StringUtils.isNotEmpty(html.xpath("//div[@class='a-row a-text-center']/img/@src").get()) || new PlainText(url).get().contains("validateCaptcha")) {
//            String validUrl = utils.getValidUrl(url, html);
//            System.out.println(utils.parseValidUrl("104.217.205.19", 8080, "hanyun45314", "hanyun45314", validUrl));
//        }
//        //104.217.205.19
//        //23.228.254.51
//        //
//    }

    public static void main(String[] args) {
        ValidateProxyUtils utils = new ValidateProxyUtils();
        System.out.println(utils.isValidate("104.216.237.83", 8080, "hanyun893851", "hanyun893851"));
    }
}
