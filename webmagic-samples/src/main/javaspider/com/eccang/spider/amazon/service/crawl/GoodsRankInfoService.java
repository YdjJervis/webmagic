package com.eccang.spider.amazon.service.crawl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.crawl.GoodsRankInfoDao;
import com.eccang.spider.amazon.pojo.crawl.GoodsRankInfo;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/27 17:51
 */
@Service
public class GoodsRankInfoService {
    @Autowired
    GoodsRankInfoDao mGoodsRankInfoDao;

    public void addAll(List<GoodsRankInfo> goodsRankInfos) {
        mGoodsRankInfoDao.addAll(goodsRankInfos);
    }

    public List<GoodsRankInfo> findByBatch(String batchNum) {
        return mGoodsRankInfoDao.findByBatch(batchNum);
    }

    public List<GoodsRankInfo> findByBatchAndKeywordInfo(String batchNum, String asin, String keyword, String siteCode, String departmentCode) {
        return mGoodsRankInfoDao.findByBatchAndKeywordInfo(batchNum, asin, keyword, siteCode, departmentCode);
    }
}