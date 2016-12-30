package us.codecraft.webmagic.samples.amazon.dao.crawl;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.GoodsRankInfo;

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
}
