package com.eccang.pojo.business;

import com.eccang.pojo.BaseReqParam;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * 查询客户业务使用情况的对象
 * 2016/12/15 17:40
 */
public class CustomerBusinessReq extends BaseReqParam {


    private List<Business> data;

    public List<Business> getData() {
        return data;
    }

    public void setData(List<Business> data) {
        this.data = data;
    }

    public class Business {
        /**
         * businessCode : US
         */

        private String businessCode;

        public String getBusinessCode() {
            return businessCode;
        }

        public void setBusinessCode(String businessCode) {
            this.businessCode = businessCode;
        }
    }
}