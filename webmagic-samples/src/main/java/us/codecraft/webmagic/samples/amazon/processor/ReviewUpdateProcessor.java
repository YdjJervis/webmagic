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
import java.util.List;

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
        if (page.getUrl().get().contains(Review.PRODUCT_REVIEWS)) {

            List<Selectable> reviewNodeList = extractReviewNodeList(page);

            String asin = extractAsin(page);
            String siteCode = extractSite(page).basCode;

            sLogger.info("解析 " + siteCode + " 站点下ASIN码为 " + asin + " 的评论信息,当前URL=" + page.getUrl());

            Asin byAsin = mAsinService.findByAsin(asin);
            List<StarReviewMap> starReviewMapList = new Gson().fromJson(byAsin.extra, new TypeToken<List<StarReviewMap>>() {
            }.getType());

            /*是否需要爬取下一页，默认是需要的*/
            boolean needCrawlNextPage = true;
            List<Review> reviewList = new ArrayList<Review>();
            for (int i = 0, len = reviewNodeList.size(); i < len; ++i) {
                Selectable reviewNode = reviewNodeList.get(i);

                Review review = extractReviewItem(siteCode, asin, reviewNode);

                if (CollectionUtils.isNotEmpty(starReviewMapList)) {
                    for (StarReviewMap map : starReviewMapList) {
                        if (review.sarStar == map.star && review.sarReviewId.equals(map.reviewID)) {
                            /*如果当前页面有一条评论，跟上一次爬取的评论的某星级的最后一条评论的星级相同，日期也相同，代表是
                            * 同一条评论，那么就不需要再爬取下一页了*/
                            needCrawlNextPage = false;
                        }
                    }
                }

                reviewList.add(review);

                /*如果页码为空(如果是更新爬取的首页) && 是第一条评论*/
                if (StringUtils.isEmpty(UrlUtils.getValue(page.getUrl().get(), "pageNumber")) && i == 0) {
                    /*把asin的extra字段对应星级的时间更改成最新的评论时间*/
                    mAsinService.updateAsinExtra(asin, review, UrlUtils.getValue(page.getUrl().get(), "filterByStar"));
                }
            }
            mReviewService.addAll(reviewList);

            if (needCrawlNextPage) {
                /*提取页码，若为空，就设置成 1 */
                String pageNum = UrlUtils.getValue(page.getUrl().get(), "pageNumber");
                if (StringUtils.isEmpty(pageNum)) {
                    pageNum = "1";
                }
                /*对URL的页码加 1 */
                String newUrl = UrlUtils.setValue(page.getUrl().get(), "pageNumber", String.valueOf(Integer.valueOf(pageNum) + 1));

                /*把新的Url放进爬取队列*/
                Url url = new Url();
                url.siteCode = siteCode;
                url.saaAsin = asin;
                url.parentUrl = page.getUrl().get();
                url.url = newUrl;
                url.url = UrlUtils.md5(newUrl);

                mUrlService.add(url);
            } else {
                /*不需要继续翻页，代表该星级的更新爬取已经完成，就删除该星级的Url*/
                String filter = UrlUtils.getValue(page.getUrl().get(), "filterByStar");
                mUrlService.deleteUpdateCrawl(asin, filter);
            }
        }
    }

    @Override
    public void execute() {
        sLogger.info("开始执行更新爬取...");
        List<Url> urlList = mUrlService.find(2);
        startToCrawl(urlList);
    }
}