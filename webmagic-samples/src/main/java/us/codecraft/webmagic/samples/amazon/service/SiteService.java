package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.SiteDao;
import us.codecraft.webmagic.samples.amazon.pojo.Site;
import us.codecraft.webmagic.samples.base.service.BaseServiceImpl;

import java.util.List;

@Service
public class SiteService extends BaseServiceImpl<Site> {

    @Autowired
    private SiteDao dao;

    @Override
    public long add(Site obj) {
        return dao.add(obj);
    }

    @Override
    public long addAll(List<Site> objList) {
        return dao.addAll(objList);
    }

    @Override
    public long update(Site obj) {
        return dao.update(obj);
    }

    @Override
    public List<Site> find(String keyWord) {
        return dao.find(keyWord);
    }

    @Override
    public List<Site> findAll() {
        return dao.findAll();
    }
}
