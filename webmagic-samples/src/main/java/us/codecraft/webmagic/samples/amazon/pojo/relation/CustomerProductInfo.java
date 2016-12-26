package us.codecraft.webmagic.samples.amazon.pojo.relation;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 客户和产品基本信息关系
 * @date 2016/12/26 11:29
 */
public class CustomerProductInfo extends BasePojo {

    public String customerCode;
    public String siteCode;
    public String asin;
    public String rootAsin;

    @Override
    public String toString() {
        return "CustomerProductInfo{" +
                "customerCode='" + customerCode + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", asin='" + asin + '\'' +
                ", rootAsin='" + rootAsin + '\'' +
                '}';
    }
}
