package com.eccang.spider.amazon.pojo.pay;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 付费套餐日志
 * @date 2017/2/13 16:19
 */
public class PayPackageLog extends BasePojo {

    public String customerCode;
    public int curd = Curd.ADD;
    public String describe;
    public int price;

    public PayPackageLog() {}

    public PayPackageLog(String customerCode) {
        this.customerCode = customerCode;
    }

    @Override
    public String toString() {
        return "PayPackageLog{" +
                "customerCode='" + customerCode + '\'' +
                ", curd=" + curd +
                ", describe='" + describe + '\'' +
                ", price=" + price +
                '}';
    }

    public static final class Curd {
        public static final int ADD = 0;
        public static final int DELETE = 1;
        public static final int QUERY = 2;
        public static final int UPDATE = 3;
    }

    public static final class Describe {
        public static final String MSG_1 = "订购数据抓取固定套餐";
        public static final String MSG_2 = "订购数据抓取自定义套餐";
    }
}
