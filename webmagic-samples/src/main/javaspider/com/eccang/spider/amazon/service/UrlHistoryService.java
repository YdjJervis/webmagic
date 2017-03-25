package com.eccang.spider.amazon.service;

import com.eccang.spider.amazon.dao.UrlHistoryDao;
import com.eccang.spider.amazon.pojo.Url;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addAll(List<Url> urlList) {
        List<Url> newList = new ArrayList<>();
        for (Url url : urlList) {
            if (!isExist(url.urlMD5)) {
                newList.add(url);
            }
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            mDao.addAll(newList);
        }
    }

    public void add(Url url) {
        if (!isExist(url.urlMD5)) {
            mDao.add(url);
        }
    }

    public boolean isExist(String urlMD5) {
        return mDao.find(urlMD5).size() > 0;
    }

    public List<Url> findByAsin(String siteCode, String asin) {

        List<Url> list = mDao.findByAsin(siteCode, asin);
        for (Url url : list) {
            url.status = 200;
        }
        return list;
    }

}
