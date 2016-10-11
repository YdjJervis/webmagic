package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.AsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Review业务
 */
@Service
public class AsinService {

    @Autowired
    AsinDao mAsinDao;

    private Logger mLogger = Logger.getLogger(getClass());

    private List<Asin> findByPriority(int priority) {

        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("priority must between 0 and 5");
        }

        int[] startStatus = {0, 0, 0, 0, 0};
        for (int i = 0; i < priority; i++) {//eg：吧0,0,0,0,0改成0,0,1,1,1表示查找3星,4星,5星的
            startStatus[startStatus.length - i - 1] = 1;
        }

        String status = getStatusStr(startStatus);
        List<Asin> asinList = mAsinDao.find(status);

        if (CollectionUtils.isNotEmpty(asinList)) {
            for (Asin asin : asinList) {
                asin.saaSyncTime = new Date();
                mAsinDao.updateSyncTime(asin);
            }
        }

        return asinList;
    }

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
     * @param priority 级别，爬取priority星级以下的评论,取值范围[1,5]
     * @return 返回priority及以下等级的Asin列表
     */
    public List<Asin> find(int priority) {

        List<Asin> asinList = new ArrayList<Asin>();

        for (int i = 1; i <= priority; i++) {
            asinList.addAll(findByPriority(i));
        }

        return asinList;
    }

    /**
     * 把Asin的priority星级的状态改成status
     *
     * @param asin     Asin对象
     * @param priority 星级，取值范围[1,5]
     * @param status   状态，取值范围[0,2]
     */
    public void update(Asin asin, int priority, int status) {
        String[] strArray = asin.saaStatus.split("-");
        int[] intArray = new int[5];

        for (int i = 0, len = strArray.length; i < len; i++) {
            intArray[i] = Integer.valueOf(strArray[i]);
        }

        //0-0-0-0-0更改成0-0-0-1-0表示二星级的已经爬取过一条记录
        //0-0-0-0-0更改成0-0-0-2-0表示二星级的已经完全爬取
        intArray[5 - priority] = status;
        asin.saaStatus = getStatusStr(intArray);
        asin.updatetime = new Date();

        mAsinDao.update(asin);
    }

    /**
     * @return 未转换成URL的ASIN对象列表
     */
    public List<Asin> findAll() {
        return mAsinDao.findAll();
    }

    /**
     * 把asin更新为已经爬取过的状态
     */
    public void updateStausCrawled(Asin asin) {
        if (asin == null) return;

        int[] starArray = parseStringArray2IntArray(asin.saaStar.split("-"));
        int[] statusArray = parseStringArray2IntArray(asin.saaStatus.split("-"));

        for (int i = 0; i < starArray.length; i++) {
            if (starArray[i] == 1) {
                statusArray[i] = 1;
            }
        }

        asin.saaStatus = getStatusStr(statusArray);
        asin.saaParsed = 1;
        mAsinDao.update(asin);
    }

    public void find(String string) {

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
