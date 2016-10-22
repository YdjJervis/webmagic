package us.codecraft.webmagic.samples.amazon.processor;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewStat;
import us.codecraft.webmagic.samples.amazon.service.ReviewStatService;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/21 14:40
 */
@Service
public class ReviewStatProcessor extends ReviewProcessor {

    @Autowired
    private ReviewStatService mService;

    @Override
    public void process(Page page) {
        dealValidate(page);
        dealReviewStat(page);
    }

    private void dealReviewStat(Page page) {
        if (page.getUrl().get().contains(Review.PRODUCT_REVIEWS) && !isValidatePage(page)) {

            updateUrlStatus(page);

            String asin = extractAsin(page);

            int totalReview = Integer.valueOf(page.getHtml().xpath("//*[@data-hook='total-review-count']/text()").get());
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
            reviewStat.saaAsin = asin;
            reviewStat.extra = new Gson().toJson(propList);
            reviewStat.sarsTotalReview = totalReview;
            reviewStat.sarsAverageStar = starAverage;
            reviewStat.sarsTotalPage = totalPage;

            sLogger.info(reviewStat);
            mService.add(reviewStat);
        }
    }

    @Override
    public void execute() {
        super.execute();
    }

}