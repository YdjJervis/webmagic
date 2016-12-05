package us.codecraft.webmagic.samples.amazon.processor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.StarReviewMap;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.AsinService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.base.util.UrlUtils;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论更新爬取业务
 * @date 2016/10/14 15:24
 */
@Service
public class ReviewUpdateProcessor extends ReviewProcessor {

    @Autowired
    private UrlService mUrlService;
    @Autowired
    private AsinService mAsinService;

    @Override
    protected void dealReview(Page page) {

        String filter = UrlUtils.getValue(page.getUrl().get(), "filterByStar");

        List<Selectable> reviewNodeList = extractReviewNodeList(page);

        String asin = extractAsin(page);
        String siteCode = extractSite(page).basCode;

        sLogger.info("解析 " + siteCode + " 站点下ASIN码为 " + asin + " 的评论信息,当前URL=" + page.getUrl());

        Asin byAsin = mAsinService.findByAsin(siteCode, asin);
        List<StarReviewMap> starReviewMapList = new Gson().fromJson(byAsin.extra, new TypeToken<List<StarReviewMap>>() {
        }.getType());

        /* 把所有星级评论的ID加入Set集合 */
        Set<String> lastReviewSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(starReviewMapList)) {
            for (StarReviewMap map : starReviewMapList) {
                lastReviewSet.add(map.reviewID);
            }
        }

        /* 是否需要爬取下一页，默认是需要的 */
        boolean needCrawlNextPage = true;

        List<Review> reviewList = new ArrayList<Review>();
        for (Selectable reviewNode : reviewNodeList) {

            Review review = extractReviewItem(siteCode, asin, reviewNode);
            if (lastReviewSet.contains(review.reviewId)) {
                needCrawlNextPage = false;
                break;
            }
            reviewList.add(review);
        }
        mReviewService.addAll(reviewList);

        if (needCrawlNextPage) {
            /* 提取页码，若为空，就设置成 1 */
            String pageNum = UrlUtils.getValue(page.getUrl().get(), "pageNumber");
            pageNum = StringUtils.isEmpty(pageNum) ? "1" : pageNum;

            int currentPage = Integer.valueOf(pageNum);
            int totalPage = extractTotalPage(page);

            if (currentPage < totalPage) {

                /* 对URL的页码加 1 */
                String nextPageUrl = UrlUtils.setValue(page.getUrl().get(), "pageNumber", String.valueOf(currentPage + 1));

                /* 把新的Url放进爬取队列 */
                Url url = new Url();
                url.asin = asin;
                url.parentUrl = page.getUrl().get();
                url.url = nextPageUrl;
                url.urlMD5 = UrlUtils.md5(url.batchNum + nextPageUrl);
                url.siteCode = siteCode;
                url.type = 2;

                mUrlService.add(url);
            } else {
                needCrawlNextPage = false;
            }
        }

        if (!needCrawlNextPage) {
            /* 不需要继续翻页，代表该星级的更新爬取已经完成，就删除该星级的Url */
            mUrlService.deleteUpdateCrawl(siteCode, asin, filter);
        }
    }

    @Override
    public void execute() {
        sLogger.info("开始执行更新爬取...");
        List<Url> urlList = mUrlService.find(2);
        startToCrawl(urlList);
    }

}