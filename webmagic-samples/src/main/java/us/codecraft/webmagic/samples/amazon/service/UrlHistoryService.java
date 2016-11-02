package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlHistoryDao;
import us.codecraft.webmagic.samples.amazon.pojo.Url;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Url爬取队列业务
 * @date 2016/10/11
 */
@Service
public class UrlHistoryService {

    @Autowired
    private UrlHistoryDao mDao;

    Logger mLogger = Logger.getLogger(getClass());

    public void addAll(List<Url> urlList) {
        List<Url> newList = new ArrayList<Url>();
        for (Url url : urlList) {
            if (!isExist(url.urlMD5)) {
                newList.add(url);
            }
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            try {
                mDao.addAll(newList);
            } catch (Exception e) {
                mLogger.warn("历史URL重复，影响不大，打印出来快叫爬虫的检查");
                mLogger.warn(newList);
            }
        }
    }

    public boolean isExist(String urlMD5) {
        return mDao.find(urlMD5).size() > 0;
    }

    public List<Url> findByAsin(String asin){

        List<Url> list = mDao.findByAsin(asin);
        for (Url url : list) {
            url.status = 200;
        }
        return list;
    }

}
