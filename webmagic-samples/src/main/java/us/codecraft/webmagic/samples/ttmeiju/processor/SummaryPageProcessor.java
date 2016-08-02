package us.codecraft.webmagic.samples.ttmeiju.processor;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.ttmeiju.pipeline.MoviePipleline;
import us.codecraft.webmagic.samples.ttmeiju.pojo.Movie;
import us.codecraft.webmagic.samples.ttmeiju.pojo.Summary;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜
 */
public class SummaryPageProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(SummaryPageProcessor.class);
    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        if (page.getUrl().get().contains("summary")) {
            List<Selectable> trNodes = page.getHtml().xpath("//tbody/tr").nodes();
            List<Summary> summaryList = new ArrayList<Summary>();
            for (int i = 3, len = trNodes.size(); i < len; i++) {
                Selectable trNode = trNodes.get(i);
                List<Selectable> tdNodes = trNode.xpath("td").nodes();
                if (tdNodes.size() < 6) continue;

                Summary summary = new Summary();
                summary.setSeri(getContent(tdNodes.get(0)));
                summary.setName(tdNodes.get(1).xpath("a/text()").get().trim());
                summary.setUrl(tdNodes.get(1).xpath("a/@href").get());
                summary.setStatus(getContent(tdNodes.get(2)));
                summary.setUpdateDate(getContent(tdNodes.get(3)));
                summary.setBackTime(getContent(tdNodes.get(4)));
                summary.setLeftTime(getContent(tdNodes.get(5)));

                summaryList.add(summary);
            }
            logger.info(summaryList.toString());
            page.putField(MoviePipleline.SUMMARY_LIST,summaryList);
            //继续下载详情
            for (Summary summary : summaryList) {
                page.addTargetRequest(summary.getUrl());//爬每一部的列表
            }

//            logger.info(summaryList.get(0).getUrl());
//            page.addTargetRequest(summaryList.get(0).getUrl());//爬每一部的列表

        } else if (page.getUrl().get().contains("seed")) {

        } else {//每一部的列表解析

            String img = page.getHtml().xpath("//img[@id='spic']/@src").get();

            List<Selectable> trNodes = page.getHtml().xpath("//tr[@class='Scontent']").nodes();

            List<Movie> movieList = new ArrayList<Movie>();

            for (Selectable trNode : trNodes) {
                List<Selectable> tdNodes = trNode.xpath("td").nodes();
                if (tdNodes.size() != 7) continue;

                Movie movie = new Movie();
                movie.setSeri(getContent(tdNodes.get(0)));
                movie.setName(tdNodes.get(1).xpath("a/text()").get().trim());
                movie.setUrl(tdNodes.get(1).xpath("a/@href").get());

                //urls=============
                List<Selectable> aNodes = tdNodes.get(2).xpath("a").nodes();
                List<String> downloadList = new ArrayList<String>();
                for (Selectable aNode : aNodes) {
                    downloadList.add(aNode.xpath("a/@href").get());
                }
                movie.setDownload(new Gson().toJson(downloadList));
                //urls=============

                movie.setSize(getContent(tdNodes.get(3)));
                movie.setType(getContent(tdNodes.get(4)));
                movie.setWords(tdNodes.get(5).xpath("td/font/text()|td/a/text()").get());
                movie.setDiscuss(getContent(tdNodes.get(6)));

                movie.setImg(img);

                movieList.add(movie);
            }
            logger.info(movieList.toString());
            page.putField(MoviePipleline.MOVIE_LIST,movieList);

            //翻页信息
            List<String> pageList = page.getHtml().xpath("//div[@class='pages']//a/@href").all();
            logger.info(pageList.toString());
            for (String pageUrl : pageList) {
                page.addTargetRequest(pageUrl);
            }
        }
    }

    private String getContent(Selectable selectable) {
        return selectable.xpath("td/text()").get().trim();
    }

    @Override
    public Site getSite() {
        return mSite;
    }

    public static void main(String[] args) {
        Spider.create(new SummaryPageProcessor())
                .addUrl("http://www.ttmeiju.com/summary.html")
                .addPipeline(new MoviePipleline())
                .thread(1)
                .start();
    }
}
