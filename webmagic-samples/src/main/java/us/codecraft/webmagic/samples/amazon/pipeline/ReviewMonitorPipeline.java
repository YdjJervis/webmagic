package us.codecraft.webmagic.samples.amazon.pipeline;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.service.ReviewService;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论和Url入库的管道
 * @date 2016/10/11
 */
@Service
public class ReviewMonitorPipeline implements Pipeline {

    public static final String PARAM_REVIEW = "param_review";

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private ReviewService mReviewService;

    @Override
    public void process(ResultItems resultItems, Task task) {

        Review review = resultItems.get(PARAM_REVIEW);
        if (review != null) {
            mReviewService.add(review);
        }
    }
}
