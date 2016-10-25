package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 单个ASIN总的评价统计
 * @date 2016/10/21 14:04
 */
public class ReviewStat extends BasePojo {

    public String basCode;
    public String saaAsin;
    public int sarsTotalReview;
    public int sarsTotalPage;
    public float sarsAverageStar;


    @Override
    public String toString() {
        return super.toString() + "ReviewStat{" +
                "basCode='" + basCode + '\'' +
                "saaAsin='" + saaAsin + '\'' +
                ", sarsTotalReview=" + sarsTotalReview +
                ", sarsTotalPage=" + sarsTotalPage +
                ", sarsAverageStar=" + sarsAverageStar +
                '}';
    }

    /**
     * 星级和对应比例
     */
    public static class StarProp{

        public int star;
        /**
         * 比例 。eg：75%
         */
        public String proportion;

        @Override
        public String toString() {
            return "StarProp{" +
                    "star=" + star +
                    ", proportion='" + proportion + '\'' +
                    '}';
        }
    }

}