package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlHistoryDao;
import us.codecraft.webmagic.samples.amazon.pojo.Url;

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
    
    public void addAll(List<Url> urlList){
        mDao.addAll(urlList);
    }

}
