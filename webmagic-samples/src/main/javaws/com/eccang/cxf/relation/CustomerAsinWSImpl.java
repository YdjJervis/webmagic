package com.eccang.cxf.relation;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.cxf.asin.AsinWSImpl;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.asin.CusAsinReq;
import com.eccang.pojo.asin.CusAsinRsp;
import com.eccang.spider.amazon.pojo.Business;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.service.BusinessService;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import java.util.HashMap;
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

    @Autowired
    private BusinessService mBusinessService;

    @Override
    public String setCrawl(String jsonArray) {

        BaseRspParam baseRspParam = auth(jsonArray);
        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        CusAsinReq cusAsinReq = parseRequestParam(jsonArray, baseRspParam, CusAsinReq.class);
        if (cusAsinReq == null) {
            return baseRspParam.toJson();
        }

        if (CollectionUtils.isEmpty(cusAsinReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        Map<String, String> checkResult = checkData(cusAsinReq);
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        try {
            /* 业务及套餐限制验证 */
            int reopenCount = 0;//重新打开的量 = 关闭状态调为打开的 - 打开状态调为关闭的
            for (CusAsinReq.Asin asin : cusAsinReq.data) {
                CustomerAsin customerAsin = new CustomerAsin(cusAsinReq.customerCode, asin.siteCode, asin.asin);
                customerAsin = mCustomerAsinService.find(customerAsin);

                if (customerAsin.crawl != asin.crawl) {
                    if (customerAsin.crawl == 0 && asin.crawl == 1) {
                        reopenCount++;
                    } else {
                        reopenCount--;
                    }
                }
            }

            /* 对业务限制量和套餐总量限制 */
            Business business = mBusinessService.findByCode(R.BusinessCode.ASIN_SPIDER);
            if (cusAsinReq.data.size() > business.getImportLimit()) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.BUSSINESS_LIMIT;
                return baseRspParam.toJson();
            }

            CustomerBusiness customerBusiness = mCustomerBusinessService.findByCode(cusAsinReq.customerCode, R.BusinessCode.ASIN_SPIDER);
            if (reopenCount > customerBusiness.maxData - customerBusiness.useData) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.PAY_PACKAGE_LIMIT;
                return baseRspParam.toJson();
            }
        } catch (Exception e) {
            serverException(baseRspParam, e);
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        CusAsinRsp cusAsinRsp = new CusAsinRsp();
        cusAsinRsp.customerCode = baseRspParam.customerCode;
        cusAsinRsp.status = baseRspParam.status;
        cusAsinRsp.msg = baseRspParam.msg;

        try {
            for (CusAsinReq.Asin asin : cusAsinReq.data) {
                CustomerAsin customerAsin = new CustomerAsin(cusAsinRsp.customerCode, asin.siteCode, asin.asin);
                customerAsin = mCustomerAsinService.find(customerAsin);
                customerAsin.crawl = asin.crawl;
                mCustomerAsinService.update(customerAsin);
            }
            /*可用asin的数据量*/
            Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(cusAsinRsp.customerCode, R.BusinessCode.ASIN_SPIDER);
            cusAsinRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
            cusAsinRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);
        } catch (Exception e) {
            serverException(cusAsinRsp, e);
        }

        return cusAsinRsp.toJson();
    }

    /**
     * 校验数据
     */
    private Map<String, String> checkData(CusAsinReq cusAsinReq) {
        Map<String, String> result = new HashMap<>();

        for (CusAsinReq.Asin asin : cusAsinReq.data) {
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