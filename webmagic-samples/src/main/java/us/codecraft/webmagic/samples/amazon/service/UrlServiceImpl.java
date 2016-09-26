package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.amazon.pojo.Url;

import java.util.List;

@Service("urlService")
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlDao dao;

    @Override
    public List<Url> findAll() {
        return dao.findAll();
    }

    @Override
    public void update(Url url) {
        dao.update(url);
    }

    @Override
    public long addAll(List<Url> urlList) {
        return dao.addAll(urlList);
    }
}
