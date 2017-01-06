package com.eccang.cxf.relation;

import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.business.CusBusinessReq;
import com.eccang.pojo.business.CusBusinessRsp;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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

        CusBusinessReq cusBusinessReq = parseRequestParam(jsonArray, baseRspParam, CusBusinessReq.class);
        if (cusBusinessReq == null) {
            return baseRspParam.toJson();
        }

        /*响应对象公共参数*/
        CusBusinessRsp cusBusinessRsp = new CusBusinessRsp();
        cusBusinessRsp.customerCode = baseRspParam.customerCode;
        cusBusinessRsp.status = baseRspParam.status;
        cusBusinessRsp.msg = baseRspParam.msg;

        try {
            /*需要查询的业务码对象集合*/
            List<CusBusinessReq.Business> businessList = cusBusinessReq.getData();

            if (businessList == null || businessList.size() == 0) {
                logger.info("查询所需要的业务码参数为空.");
                cusBusinessRsp.msg = "请求参数错误，查询业务码不能为空.";
                cusBusinessRsp.status = 413;
                return cusBusinessRsp.toJson();
            }

            /*查询到的客户下的业务详情*/
            List<CusBusinessRsp.CustomerBusiness> customerBusinessList = new ArrayList<CusBusinessRsp.CustomerBusiness>();
            CusBusinessRsp.CustomerBusiness cBusiness;
            for (CusBusinessReq.Business business : businessList) {
                /*业务码*/
                String businessCode = business.getBusinessCode();
                cBusiness = cusBusinessRsp.new CustomerBusiness();
                if (StringUtils.isNotEmpty(businessCode)) {
                    /*通过客户码查询客户业务关系表*/
                    CustomerBusiness customerBusiness = mCustomerBusinessService.findByCode(baseRspParam.customerCode, businessCode);
                    cBusiness.setBusinessCode(businessCode);
                    cBusiness.setMaxDataNum(customerBusiness.getMaxData());
                    cBusiness.setUsingDataNum(customerBusiness.getUseData());
                    cBusiness.setUsableDataNum(customerBusiness.getMaxData() - customerBusiness.getUseData());
                    customerBusinessList.add(cBusiness);
                } else {
                    logger.info("查询的业务码不能为空.");
                }
            }
            cusBusinessRsp.setCustomerBusiness(customerBusinessList);
        } catch (Exception e) {
            serverException(cusBusinessRsp, e);
        }
        return new Gson().toJson(cusBusinessRsp);
    }
}