package us.codecraft.webmagic.samples.amazon.util;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.samples.amazon.pojo.HtmlResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final static String ABU_PROXY_USER = "H40BG298L37TP22P";
    private final static String ABU_PROXY_PASS = "C38CA614F1830C2B";

    /*固定代理身份验证信息*/
    private final static String IP_PROXY_USER = "hanyun853490";
    private final static String IP_PROXY_PASS = "hanyun853490";

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
    public HtmlResponse parseHtmlByProxy(String urlStr, String proxyHost, String proxyPort) {
        mLogger.info("=============== 调用固定代理IP解析URL ===============");
        HtmlResponse htmlResponse = new HtmlResponse();
        /*构造HttpClient的实例*/
        HttpClient httpClient = new HttpClient();


        if(proxyHost != null && proxyPort != null) {
            /*如果代理需要密码验证，这里设置用户名密码*/
            httpClient.getState().setProxyCredentials(AuthScope.ANY, new UsernamePasswordCredentials(IP_PROXY_USER, IP_PROXY_PASS));
            /*设置代理服务器的ip地址和端口*/
            httpClient.getHostConfiguration().setProxy(proxyHost, Integer.valueOf(proxyPort));
            /*使用抢先认证*/
            httpClient.getParams().setAuthenticationPreemptive(true);
        }
        /*创建GET方法的实例*/
        GetMethod getMethod = new GetMethod(urlStr);
        /*使用系统提供的默认的恢复策略*/
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        String htmlContent = null;
        int statusCode = 417;
        try {
            /*执行getMethod*/
            statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                mLogger.info("Method failed: " + getMethod.getStatusLine());
            }
            htmlContent = getMethod.getResponseBodyAsString();
        } catch (HttpException e) {
            mLogger.error(e);
        } catch (IOException e) {
            mLogger.error(e);
        } finally {
            htmlResponse.setHtmlContent(htmlContent);
            htmlResponse.setStatusCode(statusCode);
            /*释放连接*/
            getMethod.releaseConnection();
        }
        return htmlResponse;
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

    /**
     * 正则提取异常中的状态码
     *@param targetContent 目标内容
     * @param reg 正则
     * @return 匹配到的字符串
     */
    private static String getStatusCodeByReg(String targetContent, String reg) {
        Matcher matcher = Pattern.compile(reg).matcher(targetContent);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return null;
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

    public static void main(String[] args) throws MalformedURLException {
        ParseUtils parseUtils = new ParseUtils();
        HtmlResponse htmlResponse = parseUtils.parseHtmlByAbu("https://www.amazon.de/product-reviews/B01J5CHZJW?filterByStar=all");
        System.out.println(htmlResponse.getHtmlContent());
    }
}