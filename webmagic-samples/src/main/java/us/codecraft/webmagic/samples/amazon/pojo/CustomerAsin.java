package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户-Asin关系
 * @date 2016/11/23 9:33
 */
public class CustomerAsin extends BasePojo {

    public String customerCode;
    public String asin;
    public int status = 1;

    public CustomerAsin() {
    }

    public CustomerAsin(String customerCode, String asin) {
        this.customerCode = customerCode;
        this.asin = asin;
    }

    @Override
    public String toString() {
        return "CustomerAsin{" +
                "customerCode='" + customerCode + '\'' +
                ", asin='" + asin + '\'' +
                ", status=" + status +
                '}';
    }
}