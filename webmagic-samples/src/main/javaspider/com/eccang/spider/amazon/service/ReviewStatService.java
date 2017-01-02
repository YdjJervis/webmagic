package com.eccang.spider.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.ReviewStatDao;
import com.eccang.spider.amazon.pojo.ReviewStat;

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
