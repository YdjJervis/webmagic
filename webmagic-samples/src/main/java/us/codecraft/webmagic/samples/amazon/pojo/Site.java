package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * 字典表
 */
public class Site extends BasePojo {

    public String basCode;
    public String basSite;
    public String basName;
    public String basCrawl;

    @Override
    public String toString() {
        return super.toString() + "Site{" +
                "basCode='" + basCode + '\'' +
                ", basSite='" + basSite + '\'' +
                ", basName='" + basName + '\'' +
                ", basCrawl='" + basCrawl + '\'' +
                '}';
    }
}
