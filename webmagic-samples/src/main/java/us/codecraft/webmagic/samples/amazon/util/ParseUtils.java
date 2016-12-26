package us.codecraft.webmagic.samples.amazon.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import us.codecraft.webmagic.samples.amazon.pojo.HtmlResponse;
import us.codecraft.webmagic.samples.amazon.pojo.dict.IpsInfoManage;
import us.codecraft.webmagic.samples.base.util.UserAgentUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * @author Hardy
 * @version V0.1
 * @Description: 解析html工具类
 * @date 2016/10/26 14:10
 */
public class ParseUtils {

    private Logger mLogger = Logger.getLogger(getClass());

    /*代理服务器*/
    private final static String PROXY_SERVER = "proxy.abuyun.com";
    /*代理端口*/
    private final static int PROXY_PORT = 9010;

    /*阿布云代理隧道验证信息*/
    private final static String ABU_PROXY_USER = "H48BF9157I793Y1P";
    private final static String ABU_PROXY_PASS = "3FFF5172112CBBA1";

    /**
     * 通过阿布云的代理隧道IP解析URL
     *
     * @param urlStr url
     * @return htmlResponse
     */
    public HtmlResponse parseHtmlByAbu(String urlStr) {
        mLogger.info("=============== 调用阿布云隧道代理解析URL ===============");
        HtmlResponse htmlResponse = new HtmlResponse();

        InputStream inputStream;
        HttpURLConnection connection = null;
        int statusCode = 0;
        try {
            URL url = new URL(urlStr);

            Authenticator.setDefault(new ProxyAuthenticator(ABU_PROXY_USER, ABU_PROXY_PASS));

            /*创建代理服务器地址对象*/
            InetSocketAddress address = new InetSocketAddress(PROXY_SERVER, PROXY_PORT);
            /*创建HTTP类型代理对象*/
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);

            /*设置通过代理访问目标页面*/
            connection = (HttpURLConnection) url.openConnection(proxy);
            /*设置IP切换头*/
            connection.setRequestProperty("Proxy-Switch-Ip", "yes");
            /*设置连接超时时间*/
            //connection.setReadTimeout(5000);
            /*返回状态码*/
            statusCode = connection.getResponseCode();
            htmlResponse.setStatusCode(statusCode);
            mLogger.info("============== 阿布云解析 , url : " + urlStr + ", statusCode : " + statusCode);

            /*解析返回数据*/
            inputStream = connection.getInputStream();

            /*将流信息转化成字符串*/
            htmlResponse.setHtmlContent(new String(readStream(inputStream)));

        } catch (MalformedURLException e) {
            mLogger.error(e);
        } catch (IOException e) {
            mLogger.error(e);
            mLogger.info("===================== 阿布云代理异常 =====================");
            htmlResponse.setHtmlContent(null);

            if (statusCode == 402 || statusCode == 403 || statusCode == 404 || statusCode == 407 || statusCode == 429 || statusCode == 503) {
                htmlResponse.setStatusCode(statusCode);
            } else {
                htmlResponse.setStatusCode(417);
            }
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return htmlResponse;
    }

    /***
     * 固定代理IP解析URL
     * @param urlStr url
     * @return htmlResponse对象
     */
    public HtmlResponse parseHtmlByProxy(String urlStr, IpsInfoManage ipsInfoManage, String urlHost) {
        mLogger.info("=============== 调用固定代理IP解析URL ===============");
        String proxyHost = ipsInfoManage.getIpHost();
        String proxyPort = ipsInfoManage.getIpPort();
        String domain = ipsInfoManage.getIpDomain();
        String verifyUserName = ipsInfoManage.getIpVerifyUserName();
        String verifyPassword = ipsInfoManage.getIpVerifyPassword();
        HtmlResponse htmlResponse = new HtmlResponse();

        /*构造HttpClient的实例*/
        HttpClient httpClient = new DefaultHttpClient();

        //设定目标站点
        HttpHost httpHost = new HttpHost(urlHost);

        if(proxyHost != null && proxyPort != null) {
            //设置代理对象 ip/代理名称,端口
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(proxyHost, Integer.valueOf(proxyPort)));

            CredentialsProvider credentialsProvider = null;
            /*如果代理需要密码验证，这里设置用户名密码*/
            if(null != domain && domain.equalsIgnoreCase("US")) {
                credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(proxyHost, Integer.valueOf(proxyPort)), new UsernamePasswordCredentials(verifyUserName, verifyPassword));
                ((DefaultHttpClient) httpClient).setCredentialsProvider(credentialsProvider);
            }
            /*使用抢先认证*/
            //httpClient.getParams().setAuthenticationPreemptive(true);
        }

        // 目标地址
        HttpGet httpGet = new HttpGet(urlStr);

        /*使用系统提供的默认的恢复策略*/
        //getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        httpGet.setHeader("User-Agent", UserAgentUtil.getRandomUserAgent());

        HttpResponse httpResponse = null;
        String htmlContent = null;
        int statusCode = 0;
        try {
            /*执行getMethod*/
            httpResponse = httpClient.execute(httpHost, httpGet);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                mLogger.info("Method failed: " + httpResponse.getStatusLine());
            }
            htmlContent = getContent("utf-8", httpResponse);
        }  catch (IOException e) {
            mLogger.error(e);
        } finally {
            htmlResponse.setHtmlContent(htmlContent);
            htmlResponse.setStatusCode(statusCode);
            /*释放连接*/
            httpGet.releaseConnection();
        }
        return htmlResponse;
    }

    /**
     * 验证代理可用性，通过亚马逊网站
     */
    public boolean validateProxyByAmazon() {
        boolean isValidate = false;

        return isValidate;
    }

    /**
     * 代理服务器地址对象
     */
    private class ProxyAuthenticator extends Authenticator {
        private String user, password;

        private ProxyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }

    /***
     * 将输入流转换成字符串
     * @param inStream 输入流
     * @return 输入流转到字节
     * @throws IOException IO
     */
    private static byte[] readStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;

        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();

        return outSteam.toByteArray();
    }

    /**
     * 获取url的域名
     * @param urlStr url字符串
     * @return url的域名
     */
    public static String getHostByUrl(String urlStr) throws MalformedURLException {
        URL url = new URL(urlStr);
        return url.getHost();
    }

    protected String getContent(String charset, HttpResponse httpResponse) throws IOException {
        byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        mLogger.warn("Charset autodetect failed, use " + charset + " as charset. Please specify charset in Site.setCharset");
        return new String(contentBytes, charset);
    }

    /**
     * 通过代理IP接口获取代理IP（单个）
     */
    public static HttpHost getDynamicsIp(String urlStr) throws IOException {
        Connection.Response response = Jsoup.connect(urlStr).execute();
        String proxyIp = null;
        HttpHost proxyHost = null;
        if(response != null) {
            proxyIp = response.body();
            if(proxyIp != null) {
                proxyIp = proxyIp.trim();
                proxyHost = new HttpHost(proxyIp.split(":")[0], Integer.valueOf(proxyIp.split(":")[1]));
            }
        }
        return proxyHost;
    }

    public static void main(String[] args) throws MalformedURLException {
        ParseUtils parseUtils = new ParseUtils();
        IpsInfoManage ipsInfoManage =new IpsInfoManage();
        ipsInfoManage.setIpHost("104.216.201.2");
        ipsInfoManage.setIpPort("8080");
        ipsInfoManage.setIpDomain("US");
        ipsInfoManage.setIpVerifyUserName("hanyun753977");
        ipsInfoManage.setIpVerifyPassword("hanyun753977");
        HtmlResponse htmlResponse = parseUtils.parseHtmlByProxy("https://www.amazon.com/dp/B01M4G902N", ipsInfoManage, "www.amazon.com");
        System.out.println(htmlResponse.getHtmlContent());
    }
}