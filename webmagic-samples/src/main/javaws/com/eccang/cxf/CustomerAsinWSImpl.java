package com.eccang.cxf;

import com.eccang.R;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.CustomerAsinReq;
import com.eccang.pojo.CustomerAsinRsp;
import com.eccang.util.RegexUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.CustomerAsin;
import us.codecraft.webmagic.samples.amazon.service.CustomerAsinService;

import javax.jws.WebService;

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

    @Override
    public String setCrawl(String jsonArray) {

        BaseRspParam baseRspParam = auth(jsonArray);
        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        boolean isParamQualified = true;
        CustomerAsinReq customerAsinReq = new Gson().fromJson(jsonArray, CustomerAsinReq.class);
        for (CustomerAsinReq.Asin asin : customerAsinReq.data) {
            if (StringUtils.isEmpty(asin.asin)
                    || !RegexUtil.isSiteCodeQualified(asin.siteCode)
                    || !RegexUtil.isCrawlStatusQualified(asin.crawl)) {
                isParamQualified = false;
                break;
            }
        }
        if (!isParamQualified) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.LIST_PARAM_WRONG;
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
                if ("open".equals(asin.crawl.trim())) {
                    customerAsin.status = 1;
                } else {
                    customerAsin.status = 0;
                }
                mCustomerAsinService.update(customerAsin);
            }
        } catch (Exception e) {
            sLogger.error(e);
            customerAsinRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            customerAsinRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }

        return customerAsinRsp.toJson();
    }

}