package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.service.BaseServiceImpl;

import java.util.List;

@Service("urlService")
public class UrlService extends BaseServiceImpl<Url> {

    @Autowired
    private UrlDao dao;

    @Override
    public long add(Url obj) {
        return dao.add(obj);
    }

    @Override
    public long addAll(List<Url> objList) {
        return dao.addAll(objList);
    }

    @Override
    public long update(Url obj) {
        return dao.update(obj);
    }

    @Override
    public List<Url> find(String keyWord) {
        return dao.find(keyWord);
    }

    @Override
    public List<Url> findAll() {
        return dao.findAll();
    }
}
