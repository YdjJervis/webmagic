package com.eccang.spider.amazon.service.top100;

import com.eccang.spider.amazon.dao.top100.StockUrlDao;
import com.eccang.spider.amazon.pojo.top100.StockUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/2/21 18:26
 */
@Service
public class StockUrlService {
    @Autowired
    StockUrlDao mDao;

    /**
     * 添加一条数据
     */
    public void add(StockUrl stockUrl) {
        mDao.add(stockUrl);
    }

    /**
     * 添加多条数据
     */
    public void addAll(List<StockUrl> stockUrls) {
        for (StockUrl stockUrl : stockUrls) {
            if (mDao.findByObjCount(stockUrl) == 0) {
                mDao.add(stockUrl);
            }
        }
    }

    /**
     * 更新
     */
    public void update(StockUrl stockUrl) {
        mDao.update(stockUrl);
    }

    /**
     * 查询需要解析的url
     */
    public List<StockUrl> findNeed2ParseUrl(int num) {
        return mDao.findNeed2ParseUrl(num);
    }

    /**
     * 通过批次号和url的MD5值查询
     */
    public StockUrl findByBatchNumAndUrlMD5(String batchNum, String urlMD5) {
        return mDao.findByBatchNumAndUrlMD5(batchNum, urlMD5);
    }

    /**
     * 通过id更新url状态
     */
    public void updateById(StockUrl stockUrl) {
        mDao.updateById(stockUrl);
    }


}