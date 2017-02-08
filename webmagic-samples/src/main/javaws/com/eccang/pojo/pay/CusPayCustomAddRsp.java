package com.eccang.pojo.pay;

import com.eccang.pojo.BaseRspParam;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 订购自定义套餐返回参数
 * @date 2017/2/8 11:34
 */
public class CusPayCustomAddRsp extends BaseRspParam {

    public PayPackage data = new PayPackage();

    public class PayPackage {

        public int payPackageCode;
    }

}
