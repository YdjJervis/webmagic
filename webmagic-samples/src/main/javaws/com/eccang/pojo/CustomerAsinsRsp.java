package com.eccang.pojo;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/23 18:17
 */
public class CustomerAsinsRsp extends BaseRspParam {

    public List<CustomerAsinsRsp.CustomerAsin> data;

    public class CustomerAsin {
        public String asin;
        public String siteCode;
        public int crawl;
        public int priority;
        public int frequency;
        public String star;
        public String syncTime;
        public String createTime;
        public String updateTime;
    }
}