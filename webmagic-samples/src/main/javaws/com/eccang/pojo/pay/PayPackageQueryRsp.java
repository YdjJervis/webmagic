package com.eccang.pojo.pay;

import com.eccang.pojo.BaseRspParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 固定套餐列表查询 返回参数
 * @date 2017/1/6 10:59
 */
public class PayPackageQueryRsp extends BaseRspParam {

    public List<PayPackage> data = new ArrayList<>();

    public class PayPackage {
        public String payPackageCode;
        public int price;

        public List<PayPackageStub> stubs = new ArrayList<>();

        public class PayPackageStub {
            public String businessCode;
            public int day;
            public int priority;
            public int frequency;
            public int count;
            public int averageTime;
            public float price;
        }
    }
}
