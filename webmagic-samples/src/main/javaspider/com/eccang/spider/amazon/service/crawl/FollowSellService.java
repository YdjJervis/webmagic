package com.eccang.spider.amazon.service.crawl;

import com.eccang.spider.amazon.dao.crawl.FollowSellDao;
import com.eccang.spider.amazon.pojo.crawl.FollowSell;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (CollectionUtils.isNotEmpty(followSellList)) {
            mDao.addAll(followSellList);
        }
    }

    /**
     * @param followSell batchNum属性必填，siteCode，asin，sellerID可选
     */
    public List<FollowSell> findAll(FollowSell followSell) {
        return mDao.findAll(followSell);
    }

    public List<FollowSell> findByBatchNum(String batchNum) {
        return mDao.findByBatchNum(batchNum);
    }

}
