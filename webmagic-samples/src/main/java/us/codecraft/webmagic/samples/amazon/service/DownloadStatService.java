package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.DownloadStatDao;
import us.codecraft.webmagic.samples.amazon.pojo.DownloadStat;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/4 17:51
 */
@Service
public class DownloadStatService {

    @Autowired
    DownloadStatDao mDownloadStatDao;

    /**
     * 查询所有数据
     * @return DownloadStat对象集合
     */
    public List<DownloadStat> findAll() {
        return mDownloadStatDao.findAll();
    }

    /**
     * 通过url MD5 查询监测信息
     * @param urlMD5 url的MD5
     * @return DownloadStat
     */
    public DownloadStat findByUrlMD5(String urlMD5) {
        return mDownloadStatDao.findByUrlMD5(urlMD5);
    }

    /**
     * 增加一条对应URL的监测信息
     * @param downloadStat
     */
    public void add(DownloadStat downloadStat) {
        mDownloadStatDao.add(downloadStat);
    }

    /**
     * 通过id更新监测信息
     * @param downloadStat
     */
    public void updateById(DownloadStat downloadStat) {
        mDownloadStatDao.updateById(downloadStat);
    }

}