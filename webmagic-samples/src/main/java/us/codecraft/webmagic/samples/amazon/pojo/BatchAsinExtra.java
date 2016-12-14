package us.codecraft.webmagic.samples.amazon.pojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description: ASIN批次详单Extra字段内容
 * @date 2016/12/14 17:23
 */
public class BatchAsinExtra {

    public String reviewID;
    public int star;

    @Override
    public String toString() {
        return "BatchAsinExtra{" +
                "reviewID='" + reviewID + '\'' +
                ", star=" + star +
                '}';
    }
}
