package com.eccang.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户ASIN关系 请求参数
 * @date 2016/11/23 16:16
 */
public class CustomerAsinReq extends BaseReqParam{

    public List<Asin> data = new ArrayList<Asin>();

    public class Asin{
        public String asin;
        public String crawl;
    }
}