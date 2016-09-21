package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.DiscussDao;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;

import java.util.List;

@Service("discussService")
public class DiscussServiceImpl implements DiscussService {

    @Autowired
    private DiscussDao discussDao;

    @Override
    public List<Discuss> findAll() {
        return discussDao.findAll();
    }

    @Override
    public long add(Discuss discuss) {
        return discussDao.add(discuss);
    }

    @Override
    public long addAll(List<Discuss> discussList) {
        return discussDao.addAll(discussList);
    }

    @Override
    public List<Discuss> findAllByAsin(String asin) {
        return discussDao.findAllByAsin(asin);
    }


}
