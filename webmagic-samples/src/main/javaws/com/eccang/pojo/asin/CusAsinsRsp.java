package com.eccang.pojo.asin;

import com.eccang.pojo.BaseRspParam;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/23 18:17
 */
public class CusAsinsRsp extends BaseRspParam {

    public List<CusAsinsRsp.CustomerAsin> data;

    public class CustomerAsin {
        public String asin;
        public String siteCode;
        public int crawl;
        public int priority;
        public int frequency;
        public int onSell;
        public String star;
        public String syncTime;
        public String createTime;
        public String updateTime;
    }
}