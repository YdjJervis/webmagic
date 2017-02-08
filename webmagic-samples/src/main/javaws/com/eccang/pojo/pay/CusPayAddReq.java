package com.eccang.pojo.pay;

import com.eccang.pojo.BaseReqParam;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户购买固定套餐请求参数
 * @date 2017/1/5
 */
public class CusPayAddReq extends BaseReqParam {

    public PayPackage data = new PayPackage();

    public class PayPackage {
        public String payPackageCode;
    }

}