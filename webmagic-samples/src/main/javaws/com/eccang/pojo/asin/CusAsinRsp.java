package com.eccang.pojo.asin;

import com.eccang.pojo.BaseRspParam;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户ASIN关系 返回参数
 * @date 2016/11/23 16:18
 */
public class CusAsinRsp extends BaseRspParam {

    public BusinessInfo data = new BusinessInfo();

    public class BusinessInfo {
        public int usableNum; /*可用asin数*/
        public int hasUsedNum; /*已用数*/
    }
}