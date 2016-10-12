package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Url爬取队列业务
 * @date 2016/10/11
 */
@Service
public class UrlService {

    @Autowired
    UrlDao mUrlDao;
    @Autowired
    AsinService mAsinService;

    private Logger mLogger = Logger.getLogger(getClass());

    /**
     * 只做添加Url，若对象的url字段已经存在，则不进行任何处理
     */
    public void add(Url url) {
        mUrlDao.add(url);
    }

    public void update(Url url) {
        mUrlDao.update(url);
    }

    public void addAll(List<Url> urlList) {

        if (CollectionUtils.isNotEmpty(urlList)) {
            mUrlDao.addAll(urlList);
        }
    }

    /**
     * @param type 抓取类型。0-抓Review
     * @return 状态码不为200的所有Url
     */
    public List<Url> find(int type) {
        return mUrlDao.findByType(type);
    }

    /**
     * 根据Url最大页码和已经爬取过的页码判断是否已经爬取完毕
     * yes：把asin的状态更新到爬取完毕，no：ignore
     */
    public void updateAsinCrawledAll(String asin) {

        List<Url> list = mUrlDao.findByAsin(asin);

        /*
        * 找出评论的最大页码
        */
        int maxPage = 0;
        if (CollectionUtils.isNotEmpty(list)) {
            for (Url url : list) {
                String tmp = UrlUtils.getValue(url.url, "pageNumber");
                int current = StringUtils.isEmpty(tmp) ? 0 : Integer.valueOf(tmp);
                if (current > maxPage) {
                    maxPage = current;
                }
            }
        }

        /*
        * 如果列表集合数量<最大页码，表示已经爬取完毕了
        */
        mLogger.info("最大页码：" + maxPage + " 已经爬取的页码：" + list.size());
        Asin asinObj = mAsinService.findByAsin(asin);
        if (list.size() > maxPage) {
            mAsinService.updateStatus(asinObj, true);
        } else {
            if (maxPage != 0) {
                float progress = 1.0f * list.size() / maxPage;
                asinObj.saaProgress = progress > 1.0f ? 1.0f : progress;
                mLogger.info(asin + " 的爬取进度：" + asinObj.saaProgress);
                mAsinService.updateStatus(asinObj, false);
            }
        }
    }


}
