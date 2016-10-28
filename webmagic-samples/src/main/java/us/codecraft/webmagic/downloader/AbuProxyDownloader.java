package us.codecraft.webmagic.downloader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.samples.amazon.pojo.HtmlResponse;
import us.codecraft.webmagic.samples.amazon.service.IpsStatService;
import us.codecraft.webmagic.samples.amazon.util.ParseUtils;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 扩展的阿步云代理下载器
 * @date 2016/10/22 10:22
 */
@Service
public class AbuProxyDownloader extends AbstractDownloader {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    IpsStatService mIpsStatService;

    @Override
    public Page download(Request request, Task task) {

        ParseUtils parseUtils = new ParseUtils();

        /*阿布云切换IP*/
        mIpsStatService.manualSwitchIp(1);

        HtmlResponse htmlResponse = parseUtils.parseHtmlByAbu(request.getUrl());
        byte[] response = null;
        try {
            mLogger.info("=============== 调用阿布云隧道代理解析URL ===============");
            response = readStream(htmlResponse.getInputStream());
        } catch (Exception e) {
            mLogger.debug(e);
        }

        Page page = new Page();
        if (response != null) {

            page.setRawText(new String(response));
            page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(new String(response), request.getUrl())));
        } else {
            page.setRawText("");
        }
        page.setStatusCode(htmlResponse.getStatusCode());
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);

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
}