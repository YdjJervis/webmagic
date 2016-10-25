package us.codecraft.webmagic.amazon.processor;

import org.apache.commons.io.FileUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.AbuProxyDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.io.IOException;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 单页面保存测试，用于测试某些页面是否需要JS渲染
 * @date 2016/10/20 10:09
 */
public class SinglePageSaveProcessor implements PageProcessor {
    @Override
    public void process(Page page){
        try {
            FileUtils.writeStringToFile(new File("C:\\Users\\Administrator\\Desktop\\download.html"),page.getHtml().get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Site getSite() {
        return Site.me();
    }

    public static void main(String[] args) {
        Spider.create(new SinglePageSaveProcessor()).addUrl("https://www.amazon.cn/")
                .setDownloader(new AbuProxyDownloader())
                .start();
    }
}