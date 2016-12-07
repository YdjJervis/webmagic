package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: ASIN码对象
 * @date 2016/10/11
 */
public class Asin extends BasePojo {

    /**
     * asin码
     */
    public String siteCode;
    public String rootAsin;

    @Override
    public String toString() {
        return "Asin{" +
                "siteCode='" + siteCode + '\'' +
                ", rootAsin='" + rootAsin + '\'' +
                '}';
    }
}
