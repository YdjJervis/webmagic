package com.eccang.pojo.rank;

import com.eccang.pojo.BaseReqParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/2 14:08
 */
public class CusRankReq extends BaseReqParam {

    private List<KeywordRank> data = new ArrayList<>();

    public List<KeywordRank> getData() {
        return data;
    }

    public void setData(List<KeywordRank> data) {
        this.data = data;
    }

    public class KeywordRank {
        public String asin;
        public String siteCode;
        public String departmentCode;
        public String keyword;
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

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }
}