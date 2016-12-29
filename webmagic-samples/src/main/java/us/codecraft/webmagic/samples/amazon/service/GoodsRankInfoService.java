package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.GoodsRankInfoDao;
import us.codecraft.webmagic.samples.amazon.pojo.GoodsRankInfo;

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
}