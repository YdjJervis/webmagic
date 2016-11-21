package com.eccang.pojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: Review查询请求参数
 * @date 2016/11/21 16:49
 */
public class ReviewQueryReq extends BaseReqParam {

    public Asin data;

    public class Asin {
        public String asin;
        public String level;
        public String experience;
        public String personID;
        public int pageNum;
        public int pageSize;
    }


}