package us.codecraft.webmagic.samples.amazon.dao;


import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.DownloadStat;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/4 17:11
 */
@Repository
public interface DownloadStatDao extends BaseDao<DownloadStat> {

    /**
     * 通过url MD5 查询监测信息
     * @param urlMD5 url的MD5值
     * @return 测试对象
     */
    DownloadStat findByUrlMD5(String urlMD5);


    /**
     * 通过id更新
     * @param downloadStat
     */
    void updateById(DownloadStat downloadStat);
}
