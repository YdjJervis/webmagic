package com.eccang.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: ASIN状态查询返回
 * @date 2016/11/21 15:49
 */
public class AsinQueryRsp extends BaseRspParam {

    public List<Asin> data = new ArrayList<Asin>();

    public class Asin {
        public String asin;
        public String rootAsin;
        public int onSale;
        public float progress;
    }
}