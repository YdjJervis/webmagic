package com.eccang.cxf;

import com.eccang.R;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.CustomerAsinReq;
import com.eccang.pojo.CustomerAsinRsp;
import com.google.gson.Gson;
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

        CustomerAsinReq customerAsinReq = new Gson().fromJson(jsonArray, CustomerAsinReq.class);

        CustomerAsinRsp customerAsinRsp = new CustomerAsinRsp();
        customerAsinRsp.cutomerCode = baseRspParam.cutomerCode;
        customerAsinRsp.status = baseRspParam.status;
        customerAsinRsp.msg = baseRspParam.msg;

        try {
            for (CustomerAsinReq.Asin asin : customerAsinReq.data) {
                CustomerAsin customerAsin = new CustomerAsin(customerAsinRsp.cutomerCode, asin.siteCode, asin.asin);
                if ("open".equals(asin.crawl.trim())) {
                    customerAsin.status = 1;
                } else if ("close".equals(asin.crawl.trim())) {
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