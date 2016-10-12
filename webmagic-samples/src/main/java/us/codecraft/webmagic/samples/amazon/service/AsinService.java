package us.codecraft.webmagic.samples.amazon.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.AsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin业务
 * @date 2016/10/11
 */
@Service
public class AsinService {

    @Autowired
    AsinDao mAsinDao;

    private Logger mLogger = Logger.getLogger(getClass());

    /**
     * 把String数组转换成Int数组
     */
    private int[] parseStringArray2IntArray(String[] array) {
        int[] startStatus = {0, 0, 0, 0, 0};
        for (int i = 0; i < array.length; i++) {
            startStatus[i] = Integer.valueOf(array[i]);
        }
        return startStatus;

    }

    /**
     * 拼凑字符串，eg：把{0,0,1,1,1}拼凑成"0-0-1-1-1"
     */
    private String getStatusStr(int[] startStatus) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = startStatus.length; i < len; i++) {
            sb.append(String.valueOf(startStatus[i]));
            if (i != len - 1) {
                sb.append("-");
            }
        }

        return sb.toString();
    }

    /**
     * @return 未转换成URL的ASIN对象列表
     */
    public List<Asin> findAll() {
        return mAsinDao.findAll();
    }

    /**
     * Asin转Url的时候调用，爬取没一条Url的时候调用
     *
     * @param isCrawlFinish true:爬取完毕，false:Asin已经转换成了Url
     */
    public void updateStatus(Asin asin, boolean isCrawlFinish) {
        mLogger.info("ASIN状态转换前：" + asin);
        if (asin == null) return;

        int[] starArray = parseStringArray2IntArray(asin.saaStar.split("-"));
        int[] statusArray = parseStringArray2IntArray(asin.saaStatus.split("-"));

        for (int i = 0, len = starArray.length; i < len; i++) {
            if (starArray[i] == 1) {
                if (isCrawlFinish) {
                    /*
                    * 爬取完毕了，状态标记为2
                    */
                    statusArray[i] = 2;
                    asin.saaProgress = 1;
                } else {
                    /*
                    * Asin转换成了Url，代表需要开始爬取了，状态标记为1
                    */
                    statusArray[i] = 1;
                }
            }
        }

        asin.saaStatus = getStatusStr(statusArray);
        asin.saaParsed = 1;
        asin.updatetime = new Date();

        mLogger.info("ASIN状态转换后：" + asin);
        mAsinDao.update(asin);
    }

    public Asin findByAsin(String asin) {
        return mAsinDao.findByAsin(asin);
    }

    /**
     * @param star 需要抓取的等级。eg：0-0-1-1-1表示需要抓取差评
     * @return 亚马逊顾虑器关键字集合
     */
    public List<String> getFilterWords(String star) {
        List<String> filterList = new ArrayList<String>();

        if ("0-0-0-0-1".equals(star)) {//抓一星
            filterList.add(Filter.START_1);
        } else if ("0-0-0-1-0".equals(star)) {//抓二星
            filterList.add(Filter.START_2);
        } else if ("0-0-1-0-0".equals(star)) {//抓三星
            filterList.add(Filter.START_3);
        } else if ("0-1-0-0-0".equals(star)) {//抓四星
            filterList.add(Filter.START_4);
        } else if ("1-0-0-0-0".equals(star)) {//抓五星
            filterList.add(Filter.START_5);
        } else if ("0-0-1-1-1".equals(star)) {//抓123星
            filterList.add(Filter.START_BAD);
        } else if ("1-1-0-0-0".equals(star)) {//抓45星
            filterList.add(Filter.START_GOOD);
        } else if ("0-0-0-1-1".equals(star)) {//抓12星
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_2);
        } else if ("0-0-1-0-1".equals(star)) {//抓13星
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_3);
        } else if ("0-1-0-0-1".equals(star)) {//抓14星
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_4);
        } else if ("1-0-0-0-1".equals(star)) {//抓15星
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_5);
        } else if ("0-0-1-1-0".equals(star)) {//抓23
            filterList.add(Filter.START_2);
            filterList.add(Filter.START_3);
        } else if ("0-1-0-1-0".equals(star)) {//抓24
            filterList.add(Filter.START_2);
            filterList.add(Filter.START_4);
        } else if ("1-0-0-1-0".equals(star)) {//抓25
            filterList.add(Filter.START_2);
            filterList.add(Filter.START_5);
        } else if ("0-1-1-0-0".equals(star)) {//抓34
            filterList.add(Filter.START_3);
            filterList.add(Filter.START_4);
        } else if ("1-0-3-0-0".equals(star)) {//抓35
            filterList.add(Filter.START_3);
            filterList.add(Filter.START_5);
        } else if ("1-1-1-0-0".equals(star)) {//抓好3
            filterList.add(Filter.START_GOOD);
            filterList.add(Filter.START_3);
        } else if ("1-1-0-1-0".equals(star)) {//抓好2
            filterList.add(Filter.START_GOOD);
            filterList.add(Filter.START_2);
        } else if ("1-1-0-0-1".equals(star)) {//抓好1
            filterList.add(Filter.START_GOOD);
            filterList.add(Filter.START_1);
        } else if ("1-0-1-1-0".equals(star)) {//抓235
            filterList.add(Filter.START_2);
            filterList.add(Filter.START_3);
            filterList.add(Filter.START_5);
        } else if ("0-1-1-1-0".equals(star)) {//抓234
            filterList.add(Filter.START_2);
            filterList.add(Filter.START_3);
            filterList.add(Filter.START_4);
        } else if ("1-0-1-0-1".equals(star)) {//抓135
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_3);
            filterList.add(Filter.START_5);
        } else if ("0-1-1-0-1".equals(star)) {//抓134
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_3);
            filterList.add(Filter.START_4);
        } else if ("1-0-0-1-1".equals(star)) {//抓125
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_2);
            filterList.add(Filter.START_5);
        } else if ("0-1-0-1-1".equals(star)) {//抓124
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_2);
            filterList.add(Filter.START_4);
        } else if ("1-1-1-1-0".equals(star)) {//抓23好
            filterList.add(Filter.START_2);
            filterList.add(Filter.START_3);
            filterList.add(Filter.START_GOOD);
        } else if ("1-1-1-0-1".equals(star)) {//抓13好
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_3);
            filterList.add(Filter.START_GOOD);
        } else if ("1-1-0-1-1".equals(star)) {//抓12好
            filterList.add(Filter.START_2);
            filterList.add(Filter.START_1);
            filterList.add(Filter.START_GOOD);
        } else if ("1-0-1-1-1".equals(star)) {//抓5差
            filterList.add(Filter.START_5);
            filterList.add(Filter.START_BAD);
        } else if ("0-1-1-1-1".equals(star)) {//抓4差
            filterList.add(Filter.START_4);
            filterList.add(Filter.START_BAD);
        } else {//抓全部
            filterList.add(Filter.START_ALL);
        }

        mLogger.info("当前过滤器集合为：" + filterList);
        return filterList;
    }

    private static final class Filter {

        private static final String START_1 = "one_star";
        private static final String START_2 = "two_star";
        private static final String START_3 = "three_star";
        private static final String START_4 = "four_star";
        private static final String START_5 = "five_star";

        private static final String START_GOOD = "positive";
        private static final String START_BAD = "critical";
        private static final String START_ALL = "all";

    }

}
