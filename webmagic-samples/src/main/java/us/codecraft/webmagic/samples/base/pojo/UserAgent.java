package us.codecraft.webmagic.samples.base.pojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 用户代理
 * @date 2016/10/14 9:36
 */
public class UserAgent extends BasePojo {

    public String userAgent;
    public int useCount;

    @Override
    public String toString() {
        return super.toString() + "UserAgent{" +
                "userAgent='" + userAgent + '\'' +
                ", useCount=" + useCount +
                '}';
    }
}