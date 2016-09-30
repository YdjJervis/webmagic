package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ReviewDao;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.base.service.BaseServiceImpl;

import java.util.List;

@Service
public class ReviewService extends BaseServiceImpl<Review> {

    @Autowired
    private ReviewDao dao;

    @Override
    public long add(Review obj) {
        return dao.add(obj);
    }

    @Override
    public long addAll(List<Review> objList) {
        return dao.addAll(objList);
    }

    @Override
    public long update(Review obj) {
        return dao.update(obj);
    }

    @Override
    public List<Review> find(String keyWord) {
        return dao.find(keyWord);
    }

    @Override
    public List<Review> findAll() {
        return dao.findAll();
    }


}
