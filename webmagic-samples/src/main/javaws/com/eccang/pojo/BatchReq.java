package com.eccang.pojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次详单查询请求参数结构
 * @date 2016/11/22 14:13
 */
public class BatchReq extends BaseReqParam {

    public Batch data;

    public class Batch {
        public String number;
    }
}