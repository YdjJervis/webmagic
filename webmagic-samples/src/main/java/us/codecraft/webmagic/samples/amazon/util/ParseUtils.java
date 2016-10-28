package us.codecraft.webmagic.samples.amazon.util;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.samples.amazon.pojo.HtmlResponse;

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

    /**
     * 通过阿布云的代理隧道IP解析URL
     * @param urlStr
     * @return htmlResponse
     */
    public synchronized HtmlResponse parseHtmlByAbu(String urlStr) {
        HtmlResponse htmlResponse = new HtmlResponse();

        // 代理服务器
        String proxyServer = "proxy.abuyun.com";
        int proxyPort = 9010;

        // 代理隧道验证信息
        String proxyUser = "H48BF9157I793Y1P";
        String proxyPass = "3FFF5172112CBBA1";

        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);

            Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPass));

            // 创建代理服务器地址对象
            InetSocketAddress addr = new InetSocketAddress(proxyServer, proxyPort);
            // 创建HTTP类型代理对象
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

            // 设置通过代理访问目标页面
            connection = (HttpURLConnection) url.openConnection(proxy);
            // 设置IP切换头
            //connection.setRequestProperty("Proxy-Switch-Ip", "no");
            // 解析返回数据
            inputStream = connection.getInputStream();
            htmlResponse.setInputStream(inputStream);
            htmlResponse.setStatusCode(connection.getResponseCode());
            mLogger.info("============== url : " + urlStr + ", statusCode : " + connection.getResponseCode());

        } catch (MalformedURLException e) {
            mLogger.error(e);
        } catch (IOException e) {
            mLogger.info("===================== 阿布云代理异常");
            System.out.println("**************** " + e.toString() + " ****************");
            htmlResponse.setInputStream(null);
            htmlResponse.setStatusCode(503);
            mLogger.error(e);
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
}