package com.eccang.pojo.pay;

import com.eccang.pojo.BaseReqParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户购买自定义套餐请求参数
 * @date 2017/1/5
 */
public class CusPayCustomAddReq extends BaseReqParam {

    public List<PayPackageStub> data = new ArrayList<>();

    public class PayPackageStub {
        public String businessCode;
        public int day;
        public int priority;
        public int frequency;
        public int count;

        @Override
        public String toString() {
            return "PayPackageStub{" +
                    "businessCode='" + businessCode + '\'' +
                    ", day=" + day +
                    ", priority=" + priority +
                    ", frequency=" + frequency +
                    ", count=" + count +
                    '}';
        }
    }

}