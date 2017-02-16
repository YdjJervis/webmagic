package com.eccang.pojo.rank;

import com.eccang.pojo.BaseReqParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/5 15:51
 */
public class RankReq extends BaseReqParam {

    public List<RankReq.KeywordRank> data = new ArrayList<>();

    public List<RankReq.KeywordRank> getData() {
        return data;
    }

    public void setData(List<RankReq.KeywordRank> data) {
        this.data = data;
    }

    public class KeywordRank {
        public String asin;
        public String siteCode;
        public String departmentCode;
        public String keyword;
        public int crawl = 1;

    }
}