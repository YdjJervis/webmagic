package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ReviewStatDao;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewStat;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin Review统计
 * @date 2016/10/11
 */
@Service
public class ReviewStatService {

    @Autowired
    private ReviewStatDao mDao;

    public void add(ReviewStat reviewStat) {
        mDao.add(reviewStat);
    }

    public List<ReviewStat> find(String asin) {
        return mDao.findByAsin(asin);
    }

}
