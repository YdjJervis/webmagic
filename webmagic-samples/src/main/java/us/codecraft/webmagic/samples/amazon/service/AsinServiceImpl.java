package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.AsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;

import java.util.List;

@Service("asinService")
public class AsinServiceImpl implements AsinService {

    @Autowired
    private AsinDao dao;

    @Override
    public List<Asin> findAll() {
        return dao.findAll();
    }

    @Override
    public void update(Asin asin) {
        dao.update(asin);
    }
}
