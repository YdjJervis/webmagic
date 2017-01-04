package com.eccang.spider.amazon.processor;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import com.eccang.spider.amazon.pojo.ReviewStat;
import com.eccang.spider.amazon.service.ReviewStatService;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 统计给定一批ASIN的评论总数和评论的页码数
 * @date 2016/10/21 14:40
 */
@Service
public class ReviewStatProcessor extends ReviewProcessor {

    @Autowired
    private ReviewStatService mService;

    @Override
    protected void dealOtherPage(Page page) {
        /*提取ASIN码*/
        String asin = extractAsin(page);
        String siteCode = extractSite(page).code;

        /*提取总评价数*/
        String totalReviewStr = page.getHtml().xpath("//*[@data-hook='total-review-count']/text()").get();
        if (totalReviewStr == null) {
            page.setStatusCode(403);
            return;
        }
        int totalReview = Integer.valueOf(totalReviewStr.replace(",", ""));

        //a-icon a-icon-star-medium a-star-medium-4-5 averageStarRating
        float starAverage = Float.valueOf(page.getHtml().xpath("//*[@data-hook='average-star-rating']/@class").regex(".*medium-([0-5\\-]{1,3}).*").get().replace("-", "."));
        List<Selectable> pageNodes = page.getHtml().xpath("//li[@class='page-button']").nodes();
        int totalPage = 1;
        if (CollectionUtils.isNotEmpty(pageNodes)) {
            totalPage = Integer.valueOf(pageNodes.get(pageNodes.size() - 1).xpath("a/text()").get());
        }
        List<Selectable> starPropNodes = page.getHtml().xpath("//tr[@class='a-histogram-row']").nodes();
        List<ReviewStat.StarProp> propList = new ArrayList<ReviewStat.StarProp>();
        for (Selectable propNode : starPropNodes) {
            String starStr = propNode.xpath("td[@class='aok-nowrap']/a/text()").regex(".*([1-5]).*").get();
            if (StringUtils.isEmpty(starStr)) {
                /*该星级没有评论，跳过*/
                continue;
            }
            sLogger.info("星级：" + starStr);
            int star = Integer.valueOf(starStr);
            String prop = propNode.xpath("td[@class='a-text-right aok-nowrap']/a/text()").get();
            ReviewStat.StarProp starProp = new ReviewStat.StarProp();
            starProp.star = star;
            starProp.proportion = prop;
            propList.add(starProp);
        }

        ReviewStat reviewStat = new ReviewStat();
        reviewStat.basCode = siteCode;
        reviewStat.saaAsin = asin;
        reviewStat.extra = new Gson().toJson(propList);
        reviewStat.sarsTotalReview = totalReview;
        reviewStat.sarsAverageStar = starAverage;
        reviewStat.sarsTotalPage = totalPage;

        sLogger.info(reviewStat);
        mService.add(reviewStat);
    }

}