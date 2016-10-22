package us.codecraft.webmagic.downloader;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.*;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 扩展的阿步云代理下载器
 * @date 2016/10/22 10:22
 */
public class AbuProxyDownloader extends AbstractDownloader {

    private Logger mLogger = Logger.getLogger(getClass());

    @Override
    public Page download(Request request, Task task) {

        Page page = null;

        // 代理服务器
        String proxyServer = "proxy.abuyun.com";
        int proxyPort = 9010;

        // 代理隧道验证信息
        String proxyUser = "H40BG298L37TP22P";
        String proxyPass = "C38CA614F1830C2B";

        try {
            URL url = new URL(request.getUrl());

            Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPass));

            // 创建代理服务器地址对象
            InetSocketAddress addr = new InetSocketAddress(proxyServer, proxyPort);
            // 创建HTTP类型代理对象
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

            // 设置通过代理访问目标页面
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
            // 设置IP切换头
            connection.setRequestProperty("Proxy-Switch-Ip", "yes");

            // 解析返回数据
            byte[] response = readStream(connection.getInputStream());

            page = new Page();
            page.setRawText(new String(response));
            page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(new String(response), request.getUrl())));

            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
        } catch (Exception e) {
            mLogger.error(e);
        }
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }

    /**
     * 将输入流转换成字符串
     */
    private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;

        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();

        return outSteam.toByteArray();
    }

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