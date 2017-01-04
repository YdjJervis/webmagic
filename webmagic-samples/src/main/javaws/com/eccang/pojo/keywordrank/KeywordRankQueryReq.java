package com.eccang.pojo.keywordrank;

import com.eccang.pojo.BaseReqParam;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/4 14:14
 */
public class KeywordRankQueryReq extends BaseReqParam {

    private KeywordInfo data;

    public KeywordInfo getData() {
        return data;
    }

    public void setData(KeywordInfo data) {
        this.data = data;
    }

    public class KeywordInfo {
        private String batchNum;
        private String asin;
        private String siteCode;
        private String departmentCode;
        private String keyword;

        public String getBatchNum() {
            return batchNum;
        }

        public void setBatchNum(String batchNum) {
            this.batchNum = batchNum;
        }

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
    }
}