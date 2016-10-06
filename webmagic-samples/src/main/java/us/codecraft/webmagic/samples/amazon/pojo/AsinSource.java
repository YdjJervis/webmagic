package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * Asin源
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
