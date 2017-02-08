package com.eccang.pojo.pay;

import com.eccang.pojo.BaseReqParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户购买套餐请求参数
 * @date 2017/1/5
 */
public class CusPayAddReq extends BaseReqParam {

    public List<PayPackageStub> data = new ArrayList<>();

    public class PayPackageStub {
        public String businessCode;
        public int day;
        public int priority;
        public int frequency;
        public int count;
    }

}