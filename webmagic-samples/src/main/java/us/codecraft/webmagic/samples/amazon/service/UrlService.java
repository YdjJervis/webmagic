package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.amazon.pojo.Url;

import java.util.List;

/**
 * Review业务
 */
@Service
public class UrlService {

    @Autowired
    UrlDao mUrlDao;

    public void add(Url url) {
        mUrlDao.add(url);
    }

    public void addAll(List<Url> urlList) {
        mUrlDao.addAll(urlList);
    }

    /**
     * @return 状态码不为200的所有Url
     */
    public List<Url> findFailures(){
        return mUrlDao.find("");
    }
}
