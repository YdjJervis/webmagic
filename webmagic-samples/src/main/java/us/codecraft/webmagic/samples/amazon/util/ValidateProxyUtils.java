package us.codecraft.webmagic.samples.amazon.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.IOException;
import java.util.Map;


/***
 * 通过亚马逊代理验证
 */
@ThreadSafe
public class ValidateProxyUtils {

    private Logger logger = LoggerFactory.getLogger(getClass());

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
        request.setUrl("https://www.amazon.com/");
        Site site = new Site();
        /*设置请求参数*/
        site.setTimeOut(20*1000);
        site.setRetrySleepTime(1000);
        logger.info("validating proxy {}", proxyHost + ":" + proxyIp);
        CloseableHttpResponse httpResponse = null;
        int statusCode=0;
        Proxy proxy = null;
        HttpHost httpHost = null;
        CloseableHttpClient closeableHttpClient = null;
        try {
            httpHost = new HttpHost(proxyHost, proxyIp);
            site.setHttpProxy(httpHost);
            if(StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
                site.setUsernamePasswordCredentials(new UsernamePasswordCredentials(userName, password));
            }

            /*下载页面*/
            HttpUriRequest httpUriRequest = getHttpUriRequest(request, site, null, httpHost);
            closeableHttpClient = getHttpClient(site, proxy);
            httpResponse = closeableHttpClient.execute(httpUriRequest);
            statusCode = httpResponse.getStatusLine().getStatusCode();

            logger.info("parse Amazon url statusCode : " + statusCode);
            System.out.println(httpResponse.getEntity().toString());
            if(statusCode == HttpStatus.SC_OK) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            logger.warn("validate proxy " + proxyHost + ":" + proxyIp + " fail", e);
            return false;
        } finally {
            try {
                if (httpResponse != null) {
                    //ensure the connection is released back to pool
                    EntityUtils.consume(httpResponse.getEntity());
                }
            } catch (IOException e) {
                logger.warn("close response fail", e);
            }
        }
    }

    protected HttpUriRequest getHttpUriRequest(Request request, Site site, Map<String, String> headers,HttpHost proxy) {
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
        if (proxy !=null) {
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

    public static void main(String[] args) {
        ValidateProxyUtils utils = new ValidateProxyUtils();
        System.out.println(utils.isValidate("104.216.237.83", 8080, "hanyun893851", "hanyun893851"));
    }
}
