package com.eccang.cxf;

import com.eccang.pojo.BaseReqParam;
import com.eccang.pojo.BaseRspParam;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.API;
import us.codecraft.webmagic.samples.amazon.pojo.Customer;
import us.codecraft.webmagic.samples.amazon.pojo.Platform;
import us.codecraft.webmagic.samples.amazon.service.APIService;
import us.codecraft.webmagic.samples.amazon.service.CustomerService;
import us.codecraft.webmagic.samples.amazon.service.PlatformService;

/**
 * @author Jervis
 * @version V0.20
 * @Description:
 * @date 2016/11/17 14:21
 */
@Service
public abstract class AbstractSpiderWS implements SpiderWS {

    @Autowired
    private CustomerService mCustomerService;
    @Autowired
    private APIService mAPIService;
    @Autowired
    private PlatformService mPlatformService;

    protected Logger sLogger = Logger.getLogger(getClass());

    public BaseRspParam auth(String json) {
        sLogger.info(json);
        BaseReqParam baseReqParam = new Gson().fromJson(json, BaseReqParam.class);
        BaseRspParam baseRspParam = new BaseRspParam();

        if (baseReqParam == null) {
            sLogger.warn("Param is null.");
            baseRspParam.setSuccess(false);
            baseRspParam.status = 413;
            baseRspParam.msg = "接受到的参数为空.";
            return baseRspParam;
        }

        baseRspParam.cutomerCode = baseReqParam.cutomerCode;

        /* 授权信息必须存在 */
        if (StringUtils.isEmpty(baseReqParam.cutomerCode) ||
                StringUtils.isEmpty(baseReqParam.platformCode) || StringUtils.isEmpty(baseReqParam.token)) {
            sLogger.warn("permission info is not complete !!!");
            sLogger.warn(baseReqParam);
            baseRspParam.setSuccess(false);
            baseRspParam.status = 401;
            baseRspParam.msg = "授权信息不完整";
            return baseRspParam;
        }

        /* 客户信息授权校验 */
        Customer customer = mCustomerService.findByCode(baseReqParam.cutomerCode);
        if (customer == null) {
            sLogger.warn("customer is not exist !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = 401;
            baseRspParam.msg = "客户不存在";
            return baseRspParam;
        }
        if (customer.status == 0) {
            sLogger.warn("customer is limited !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = 401;
            baseRspParam.msg = "客户没有被启用";
        }

        /* API授权校验 */
        API api = mAPIService.findByCode(customer.code);
        if (api == null) {
            sLogger.warn("api is not exist !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = 401;
            baseRspParam.msg = "没有进行API授权";
            return baseRspParam;
        }
        if (api.status == 0) {
            sLogger.warn("api is limited !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = 401;
            baseRspParam.msg = "API授权状态未启用";
            return baseRspParam;
        }
        if (!api.token.equals(baseReqParam.token)) {
            sLogger.warn("api is limited !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = 401;
            baseRspParam.msg = "Token错误,授权失败";
            return baseRspParam;
        }

        /* 平台信息授权校验 */
        Platform platform = mPlatformService.findByCode(baseReqParam.platformCode);
        if (platform == null) {
            sLogger.warn("platform is not exist !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = 401;
            baseRspParam.msg = "没有此平台";
            return baseRspParam;
        }
        if (platform.status == 0) {
            sLogger.warn("platform is not exist !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = 401;
            baseRspParam.msg = "当前平台无法调用该接口";
            return baseRspParam;
        }

        baseRspParam.setSuccess(true);
        baseRspParam.status = 200;
        baseRspParam.msg = "操作成功";

        return baseRspParam;
    }
}