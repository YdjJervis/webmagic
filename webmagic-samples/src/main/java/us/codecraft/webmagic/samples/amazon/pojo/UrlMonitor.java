package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * Url监听表
 */
public class UrlMonitor extends BasePojo{

    public String smuUrl;
    public String smuStatus;

    @Override
    public String toString() {
        return super.toString() + "UrlMonitor{" +
                "smuUrl='" + smuUrl + '\'' +
                ", smuStatus='" + smuStatus + '\'' +
                '}';
    }

}
