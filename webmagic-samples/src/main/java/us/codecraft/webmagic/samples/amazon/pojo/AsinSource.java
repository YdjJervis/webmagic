package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: ASIN来源对象
 * @date 2016/10/11
 */
public class AsinSource extends BasePojo {

    public String baasCode;

    public String baasName;

    @Override
    public String toString() {
        return super.toString() + "AsinSource{" +
                "baasCode='" + baasCode + '\'' +
                ", baasName='" + baasName + '\'' +
                '}';
    }
}
