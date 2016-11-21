package com.eccang.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: ASIN状态查询请求参数
 * @date 2016/11/21 15:47
 */
public class AsinQueryReq extends BaseReqParam {

    public List<Asin> data = new ArrayList<Asin>();

    public class Asin{
        public String asin;
    }
}