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

        Long startTime =  System.currentTimeMillis();

        HtmlResponse htmlResponse = parseUtils.parseHtmlByAbu(request.getUrl());

        Long entTime =  System.currentTimeMillis();

        mLogger.info("======================= 调用阿布云解析URL，所需要的时间:" + (entTime -startTime)/1000 + "s.");

        Page page = new Page();

        String htmlContent = htmlResponse.getHtmlContent();

        if(htmlContent != null) {
            page.setRawText(htmlContent);
            page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(htmlContent, request.getUrl())));
        } else {
            page.setRawText("");
        }
        page.setStatusCode(htmlResponse.getStatusCode());
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.getRequest().putExtra("ipsType", "abu");
        page.getRequest().putExtra("host", "");
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }


}