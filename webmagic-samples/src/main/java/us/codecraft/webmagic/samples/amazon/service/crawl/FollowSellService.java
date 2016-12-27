package us.codecraft.webmagic.samples.amazon.service.crawl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.crawl.FollowSellDao;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.FollowSell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 跟卖 业务
 * @date 2016/10/11
 */
@Service
public class FollowSellService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private FollowSellDao mDao;

    public void add(FollowSell followSell) {
        if (!isExist(followSell.siteCode, followSell.asin)) {
            mDao.add(followSell);
        } else {
            update(followSell);
        }
    }

    public void addAll(List<FollowSell> followSellList) {
        List<FollowSell> list = new ArrayList<>();
        for (FollowSell followSell : followSellList) {
            if (!isExist(followSell.siteCode, followSell.asin)) {
                list.add(followSell);
            } else {
                update(followSell);
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            mDao.addAll(list);
        }
    }

    public void update(FollowSell followSell) {
        mDao.update(followSell);
    }

    public FollowSell find(String siteCode, String asin) {
        return mDao.find(siteCode, asin);
    }

    public boolean isExist(String siteCode, String asin) {
        return find(siteCode, asin) != null;
    }
}
