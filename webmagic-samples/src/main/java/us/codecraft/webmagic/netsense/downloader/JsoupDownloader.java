package us.codecraft.webmagic.netsense.downloader;

import org.jsoup.Jsoup;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.io.IOException;

public class JsoupDownloader implements Downloader {
    @Override
    public Page download(Request request, Task task) {
        Page page = new Page();
        try {
            String content = Jsoup.connect(request.getUrl()).timeout(1000 * 10).get().html();
            page.setRawText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        return page;
    }

    public Page download(String url) {
        Page page = new Page();
        try {
            String content = Jsoup.connect(url).get().html();
            page.setRawText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.setUrl(new PlainText(url));
        page.setRequest(new Request(url));
        return page;
    }


    @Override
    public void setThread(int threadNum) {
    }
}
