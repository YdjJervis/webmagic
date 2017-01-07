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
        public int crawl;
        public int priority;
        public int frequency;

        public String getAsin() {
            return asin;
        }

        public void setAsin(String asin) {
            this.asin = asin;
        }

        public String getSiteCode() {
            return siteCode;
        }

        public void setSiteCode(String siteCode) {
            this.siteCode = siteCode;
        }

        public String getDepartmentCode() {
            return departmentCode;
        }

        public void setDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public int getCrawl() {
            return crawl;
        }

        public void setCrawl(int crawl) {
            this.crawl = crawl;
        }
    }
}