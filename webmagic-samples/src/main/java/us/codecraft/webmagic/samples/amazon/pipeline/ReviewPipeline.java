package us.codecraft.webmagic.samples.amazon.pipeline;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.ReviewService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论和Url入库的管道
 * @date 2016/10/11
 */
@Service
public class ReviewPipeline implements Pipeline {

    public static final String PARAM_LIST = "param_list";
    public static final String PARAM_URL = "param_url";

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private UrlService mUrlService;

    @Autowired
    private ReviewService mReviewService;

    @Override
    public void process(ResultItems resultItems, Task task) {

        List<Review> reviewList = resultItems.get(PARAM_LIST);
        if (CollectionUtils.isNotEmpty(reviewList)) {
            mLogger.info("新评论列表准备入库...");
            mLogger.info(reviewList.toString());
            for (Review review : reviewList) {
                mReviewService.add(review);
            }
        }

        List<Url> urlList = resultItems.get(PARAM_URL);
        if (CollectionUtils.isNotEmpty(urlList)) {
            mLogger.info("新Url列表准备入库...");
            mLogger.info(urlList);
            for (Url url : urlList) {
                mUrlService.add(url);
            }
        }
    }
}
