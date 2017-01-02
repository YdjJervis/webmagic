package us.codecraft.webmagic.samples.amazon.service.crawl;

import org.apache.commons.collections.CollectionUtils;
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

    @Autowired
    private FollowSellDao mDao;

    public void addAll(List<FollowSell> followSellList) {
        List<FollowSell> list = new ArrayList<>();

        for (FollowSell followSell : followSellList) {
            if (isExist(followSell.batchNum, followSell.siteCode, followSell.asin, followSell.sellerID)) {
                list.add(followSell);
            } else {
                mDao.update(followSell);
            }
        }

        if (CollectionUtils.isNotEmpty(list)) {
            mDao.addAll(followSellList);
        }
    }

    /**
     * @param followSell batchNum属性必填，siteCode，asin，sellerID可选
     */
    public List<FollowSell> findAll(FollowSell followSell) {
        return mDao.findAll(followSell);
    }

    public FollowSell find(String batchNum, String siteCode, String asin, String sellerId) {
        return mDao.find(batchNum, siteCode, asin, sellerId);
    }

    public boolean isExist(String batchNum, String siteCode, String asin, String sellerId) {
        return find(batchNum, siteCode, asin, sellerId) != null;
    }
}
