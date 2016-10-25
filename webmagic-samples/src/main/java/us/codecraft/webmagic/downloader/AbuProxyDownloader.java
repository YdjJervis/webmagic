package us.codecraft.webmagic.downloader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.samples.amazon.pojo.IpsStat;
import us.codecraft.webmagic.samples.amazon.service.IpsStatService;
import us.codecraft.webmagic.samples.amazon.util.DateUtils;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 扩展的阿步云代理下载器
 * @date 2016/10/22 10:22
 */
@Service
public class AbuProxyDownloader extends AbstractDownloader {

    private Logger mLogger = Logger.getLogger(getClass());
    private final static String SWITCHIPURL = "http://proxy.abuyun.com/switch-ip";

    @Autowired
    IpsStatService mIpsStatService;

    @Override
    public Page download(Request request, Task task) {

        IpsStat ipsStat = mIpsStatService.findIpsStatById(1);

        List<String> ipsChangeRecordList = new ArrayList<String>();

        /*ipsStat.getIpsStatStatus() == 1 需要切换IP*/
        if (ipsStat.getIpsStatStatus().equals(1)) {
            String ipsChangeRecord = ipsStat.getIpsChangRecord();
            /*切IP服务*/
            this.parseHtml(SWITCHIPURL);

            /*记录切换IP时的时间,并添加到数据库中*/
            if (ipsChangeRecord != null || !("").equals(ipsChangeRecord)) {
                ipsChangeRecordList = new Gson().fromJson(ipsChangeRecord, new TypeToken<List<String>>() {
                }.getType());
            }
            ipsChangeRecordList.add(DateUtils.getNow());
            ipsStat.setIpsChangRecord(new Gson().toJson(ipsChangeRecordList));
            ipsStat.setIpsStatStatus("0");
            mIpsStatService.updateIpsStatById(ipsStat);
            mLogger.info("切换IP， date : " + DateUtils.getNow());
        }

        byte[] response = this.parseHtml(request.getUrl());
        Page page = null;
        if (response != null) {

            page = new Page();
            page.setRawText(new String(response));
            page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(new String(response), request.getUrl())));

            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);
        } else {
            page.setStatusCode(404);
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

    /***
     * parse url to html
     * @param urlStr
     * @return
     */
    private synchronized byte[] parseHtml(String urlStr) {
        // 代理服务器
        String proxyServer = "proxy.abuyun.com";
        int proxyPort = 9010;

        // 代理隧道验证信息
        String proxyUser = "H40BG298L37TP22P";
        String proxyPass = "C38CA614F1830C2B";

        byte[] response = null;
        try {
            URL url = new URL(urlStr);

            Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPass));

            // 创建代理服务器地址对象
            InetSocketAddress addr = new InetSocketAddress(proxyServer, proxyPort);
            // 创建HTTP类型代理对象
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

            // 设置通过代理访问目标页面
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
            // 设置IP切换头
            //connection.setRequestProperty("Proxy-Switch-Ip", "no");

            // 解析返回数据
            response = readStream(connection.getInputStream());

        } catch (Exception e) {
            mLogger.error(e);
        }
        return response;
    }
}