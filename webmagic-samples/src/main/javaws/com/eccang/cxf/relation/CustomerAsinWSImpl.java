package com.eccang.cxf.relation;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.cxf.asin.AsinWSImpl;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.asin.CustomerAsinReq;
import com.eccang.pojo.asin.CustomerAsinRsp;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 添加ASIN批次的WebService调用实现
 * @date 2016/11/17 11:51
 */
@WebService
public class CustomerAsinWSImpl extends AbstractSpiderWS implements CustomerAsinWS {

    @Autowired
    private CustomerAsinService mCustomerAsinService;

    @Autowired
    private CustomerBusinessService mCustomerBusinessService;

    @Override
    public String setCrawl(String jsonArray) {

        BaseRspParam baseRspParam = auth(jsonArray);
        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        CustomerAsinReq customerAsinReq = parseRequestParam(jsonArray, baseRspParam, CustomerAsinReq.class);
        if (customerAsinReq == null) {
            return baseRspParam.toJson();
        }

        if (CollectionUtils.isEmpty(customerAsinReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_NULL_ERROR;
            return baseRspParam.toJson();
        }

        Map<String, String> checkResult = checkData(customerAsinReq.data);
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        CustomerAsinRsp customerAsinRsp = new CustomerAsinRsp();
        customerAsinRsp.cutomerCode = baseRspParam.cutomerCode;
        customerAsinRsp.status = baseRspParam.status;
        customerAsinRsp.msg = baseRspParam.msg;

        try {
            for (CustomerAsinReq.Asin asin : customerAsinReq.data) {
                CustomerAsin customerAsin = new CustomerAsin(customerAsinRsp.cutomerCode, asin.siteCode, asin.asin);
                customerAsin = mCustomerAsinService.find(customerAsin);
                customerAsin.crawl = asin.crawl;
                mCustomerAsinService.update(customerAsin);
            }
            /*可用asin的数据量*/
            Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(customerAsinRsp.cutomerCode, R.BusinessCode.ASIN_SPIDER);
            customerAsinRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
            customerAsinRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);
        } catch (Exception e) {
            serverException(customerAsinRsp, e);
        }

        return customerAsinRsp.toJson();
    }

    /**
     * 校验数据
     */
    private Map<String, String> checkData(List<CustomerAsinReq.Asin> asins) {
        Map<String, String> result = new HashMap<>();

        for (CustomerAsinReq.Asin asin : asins) {
            result.put(IS_SUCCESS, "0");

            if (asin == null) {
                result.put(AsinWSImpl.MESSAGE, R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR);
                return result;
            }

            if (StringUtils.isEmpty(asin.asin)) {
                result.put(AsinWSImpl.MESSAGE, R.RequestMsg.PARAMETER_ASIN_LIST_ERROR);
                return result;
            }

            if (!RegexUtil.isSiteCodeQualified(asin.siteCode)) {
                result.put(AsinWSImpl.MESSAGE, R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR);
                return result;
            }
        }
        result.put(IS_SUCCESS, "1");
        result.put(AsinWSImpl.MESSAGE, R.RequestMsg.SUCCESS);
        return result;
    }
}