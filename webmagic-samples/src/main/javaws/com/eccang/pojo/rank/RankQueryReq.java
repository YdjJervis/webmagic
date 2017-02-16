package com.eccang.pojo.rank;

import com.eccang.pojo.BaseReqParam;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/4 14:14
 */
public class RankQueryReq extends BaseReqParam {

    public KeywordInfo data;

    public KeywordInfo getData() {
        return data;
    }

    public void setData(KeywordInfo data) {
        this.data = data;
    }

    public class KeywordInfo {
        public String batchNum;
        public String asin;
        public String siteCode;
        public String departmentCode;
        public String keyword;
    }
}