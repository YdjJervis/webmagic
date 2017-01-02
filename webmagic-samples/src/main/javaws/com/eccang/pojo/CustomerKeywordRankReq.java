package com.eccang.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/2 14:08
 */
public class CustomerKeywordRankReq extends BaseReqParam {

    private List<KeywordRank> data = new ArrayList<>();

    public List<KeywordRank> getData() {
        return data;
    }

    public void setData(List<KeywordRank> data) {
        this.data = data;
    }

    public class KeywordRank {
        private String asin;
        private String siteCode;
        private String departmentCode;
        private String keyword;
        private int priority;
        private int frequency;
        private int crawl;

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