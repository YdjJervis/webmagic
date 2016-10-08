package us.codecraft.webmagic.samples.amazon.pojo;

/**
 * 站点枚举
 */
public enum SiteEnum {

    /**
     * 中国站
     */
    CN("CN"),
    /**
     * 日本站
     */
    JP("JP"),
    /**
     * 德国站
     */
    DE("DE"),
    /**
     * 墨西哥站
     */
    MX("MX"),
    /**
     * 美国站
     */
    US("US"),
    /**
     * 加拿大站
     */
    CA("CA");

    private String mValue = "CN";

    SiteEnum(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
