package us.codecraft.webmagic.samples.amazon.pojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 需要爬取的的星级和该星级最后的评论时间的键值对
 * @date 2016/10/14 15:09
 */
public class StarTimeMap {

    /**
     * 需要爬取的星级。取值范围[1,5]
     */
    public int star;
    /**
     * 该星级对应最后评论时间
     */
    public Date date;

    public StarTimeMap(int star, Date date) {
        this.star = star;
        this.date = date;
    }

    @Override
    public String toString() {
        return "StarTimeMap{" +
                "star=" + star +
                ", date=" + date +
                '}';
    }
}