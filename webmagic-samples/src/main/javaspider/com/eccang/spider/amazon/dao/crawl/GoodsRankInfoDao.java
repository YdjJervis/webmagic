package com.eccang.spider.amazon.dao.crawl;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.crawl.GoodsRankInfo;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/27 17:36
 */
@Repository
public interface GoodsRankInfoDao {

    void addAll(List<GoodsRankInfo> goodsRankInfos);

    List<GoodsRankInfo> findByBatch(String batchNum);

    List<GoodsRankInfo> findByBatchAndKeywordInfo(String batchNum, String asin, String keyword, String siteCode, String departmentCode);
}
