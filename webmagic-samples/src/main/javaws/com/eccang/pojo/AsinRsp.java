package com.eccang.pojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 添加ASIN批次返回的数据结构
 * @date 2016/11/17 15:48
 */
public class AsinRsp extends BaseRspParam {

    public BatchOrder data = new BatchOrder();

    public class BatchOrder {
        public String number;
        public int totalCount;
        public int oldCount;
        public int newCount;
    }

}