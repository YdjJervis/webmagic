package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
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

    /**
     * 只做添加Url，若对象的url字段已经存在，则不进行任何处理
     */
    public void add(Url url) {
        mUrlDao.add(url);
    }

    public void update(Url url) {
        mUrlDao.update(url);
    }

    public void addAll(List<Url> urlList) {

        if (CollectionUtils.isNotEmpty(urlList)) {
            mUrlDao.addAll(urlList);
        }
    }

    /**
     * @return 状态码不为200的所有Url
     */
    public List<Url> findFailures() {
        return mUrlDao.find("");
    }


}
