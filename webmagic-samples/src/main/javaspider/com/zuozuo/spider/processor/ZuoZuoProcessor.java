package com.zuozuo.spider.processor;

import com.eccang.spider.base.util.UrlUtils;
import com.zuozuo.spider.pojo.Task;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

public class ZuoZuoProcessor implements PageProcessor {

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(10 * 1000).setTimeOut(10 * 1000);

    @Override
    public void process(Page page) {
        List<Selectable> taskNodes = page.getHtml().xpath("//*[@id='form1']//tbody/tr").nodes();

       // 抽取任务列表
        List<Task> taskList = new ArrayList<>();
        for (Selectable taskNode : taskNodes) {
            Task task = new Task();
            task.totalPrice = taskNode.xpath("td[@class='link']/b/text()").get();
            task.commission = taskNode.xpath("td[@class='money']/img/@src").get();
            task.publisher = taskNode.xpath("td[@class='sender']/a/text()").get();
            task.requirements = taskNode.xpath("td[@style='text-align:left;']/text()").get();
            task.operation = taskNode.xpath("td[@class='opare']/input/@id").get();
            System.out.println(task);

            taskList.add(task);
        }

        // 抽取最大翻页页码
        String lastPageStr = page.getHtml().xpath("//*[@id='lastid']/text()").get();

        // 如果最大页码存在 && 当前页是第一页
        if (StringUtils.isNotEmpty(lastPageStr) && StringUtils.isEmpty(UrlUtils.getValue(page.getUrl().get(), "page"))) {
            int lastPage = Integer.valueOf(lastPageStr);
            for (int i = 2; i <= lastPage; i++) {
                Request request = new Request(page.getUrl().get() + "&page=" + i);
                page.addTargetRequest(request);
            }
        }
    }

    @Override
    public Site getSite() {
        mSite.addHeader("Cookie", "ASP.NET_SessionId=da2z1j33kxbcos45nz0wjb55");
        return mSite;
    }

    public static void main(String[] args) {
        //http://zuozuowangluo.cc/user/cyrw.aspx?sm=10&em=500&isblack=1
//921223113&id=150039392

        Spider mSpider = Spider.create(new ZuoZuoProcessor()).addUrl("http://zuozuowangluo.cc/user/hyrw.aspx?sm=10&em=200&isblack=1");

        mSpider.start();
    }
}
