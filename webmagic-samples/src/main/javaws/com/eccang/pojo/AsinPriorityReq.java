package com.eccang.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 设置Asin优先级的请求参数
 * @date 2016/11/22 9:40
 */
public class AsinPriorityReq extends BaseReqParam {

    public List<Asin> data = new ArrayList<Asin>();

    public class Asin {
        public String asin;
        public int priority;
    }
}