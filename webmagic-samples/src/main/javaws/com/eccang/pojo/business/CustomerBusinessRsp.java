package com.eccang.pojo.business;

import com.eccang.pojo.BaseRspParam;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/15 17:46
 */
public class CustomerBusinessRsp extends BaseRspParam {


    private List<CustomerBusiness> CustomerBusiness;

    public List<CustomerBusiness> getCustomerBusiness() {
        return CustomerBusiness;
    }

    public void setCustomerBusiness(List<CustomerBusiness> CustomerBusiness) {
        this.CustomerBusiness = CustomerBusiness;
    }

    public class CustomerBusiness {
        /**
         * businessCode : MS
         * maxDataNum : 1000
         * usingDataNum : 200
         * usableDataNum : 800
         */

        private String businessCode;
        private int maxDataNum;
        private int usingDataNum;
        private int usableDataNum;

        public String getBusinessCode() {
            return businessCode;
        }

        public void setBusinessCode(String businessCode) {
            this.businessCode = businessCode;
        }

        public int getMaxDataNum() {
            return maxDataNum;
        }

        public void setMaxDataNum(int maxDataNum) {
            this.maxDataNum = maxDataNum;
        }

        public int getUsingDataNum() {
            return usingDataNum;
        }

        public void setUsingDataNum(int usingDataNum) {
            this.usingDataNum = usingDataNum;
        }

        public int getUsableDataNum() {
            return usableDataNum;
        }

        public void setUsableDataNum(int usableDataNum) {
            this.usableDataNum = usableDataNum;
        }
    }
}