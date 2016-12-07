package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.RankSearchKeywordDao;
import us.codecraft.webmagic.samples.amazon.pojo.RankSearchKeyword;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * 亚马逊关键词排名业务服务
 * 2016/12/2 16:55
 */
@Service
public class RankSearchKeywordService {

    @Autowired
    RankSearchKeywordDao mRankSearchKeywordDao;

    /**
     * 添加一条数据
     */
    public void add(RankSearchKeyword rankSearchKeyword) {
        mRankSearchKeywordDao.add(rankSearchKeyword);
    }

    /**
     * 通过id删除一条数据
     */
    public void deleteById(int id) {
        mRankSearchKeywordDao.deleteById(id);
    }

    /**
     * 通过id更新一条数据
     */
    public void updateById(RankSearchKeyword rankSearchKeyword) {
        mRankSearchKeywordDao.updateById(rankSearchKeyword);
    }

    /**
     * 通过id查询一条数据
     */
    public RankSearchKeyword findById(int id) {
        return mRankSearchKeywordDao.findById(id);
    }

    /**
     * 查询所有数据
     */
    public List<RankSearchKeyword> findAll() {
        return mRankSearchKeywordDao.findAll();
    }
}