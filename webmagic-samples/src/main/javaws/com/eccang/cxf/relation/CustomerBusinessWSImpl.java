package com.eccang.cxf.relation;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.business.CustomerBusinessReq;
import com.eccang.pojo.business.CustomerBusinessRsp;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 *          客户查询业务功能详情
 * @date 2016/12/15 17:30
 */
@WebService
public class CustomerBusinessWSImpl extends AbstractSpiderWS implements CustomerBusinessWS {

    @Autowired
    CustomerBusinessService mCustomerBusinessService;

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public String queryBusiness(String jsonArray) {
        BaseRspParam baseRspParam = auth(jsonArray);
        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CustomerBusinessReq customerBusinessReq;
        try {
            customerBusinessReq = new Gson().fromJson(jsonArray, CustomerBusinessReq.class);
        } catch (Exception e) {
            logger.info(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();

        }

        /*响应对象公共参数*/
        CustomerBusinessRsp customerBusinessRsp = new CustomerBusinessRsp();
        customerBusinessRsp.cutomerCode = baseRspParam.cutomerCode;
        customerBusinessRsp.status = baseRspParam.status;
        customerBusinessRsp.msg = baseRspParam.msg;

        try {
            /*需要查询的业务码对象集合*/
            List<CustomerBusinessReq.Business> businessList = customerBusinessReq.getData();

            if (businessList == null || businessList.size() == 0) {
                logger.info("查询所需要的业务码参数为空.");
                customerBusinessRsp.msg = "请求参数错误，查询业务码不能为空.";
                customerBusinessRsp.status = 413;
                return customerBusinessRsp.toJson();
            }

            /*查询到的客户下的业务详情*/
            List<CustomerBusinessRsp.CustomerBusiness> customerBusinessList = new ArrayList<CustomerBusinessRsp.CustomerBusiness>();
            CustomerBusinessRsp.CustomerBusiness cBusiness;
            for (CustomerBusinessReq.Business business : businessList) {
                /*业务码*/
                String businessCode = business.getBusinessCode();
                cBusiness = customerBusinessRsp.new CustomerBusiness();
                if (StringUtils.isNotEmpty(businessCode)) {
                    /*通过客户码查询客户业务关系表*/
                    CustomerBusiness customerBusiness = mCustomerBusinessService.findByCode(baseRspParam.cutomerCode, businessCode);
                    cBusiness.setBusinessCode(businessCode);
                    cBusiness.setMaxDataNum(customerBusiness.getMaxData());
                    cBusiness.setUsingDataNum(customerBusiness.getUseData());
                    cBusiness.setUsableDataNum(customerBusiness.getMaxData() - customerBusiness.getUseData());
                    customerBusinessList.add(cBusiness);
                } else {
                    logger.info("查询的业务码不能为空.");
                }
            }
            customerBusinessRsp.setCustomerBusiness(customerBusinessList);
        } catch (Exception e) {
            serverException(customerBusinessRsp, e);
        }
        return new Gson().toJson(customerBusinessRsp);
    }
}