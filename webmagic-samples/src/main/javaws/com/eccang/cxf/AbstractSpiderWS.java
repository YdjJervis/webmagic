package com.eccang.cxf;

import com.eccang.R;
import com.eccang.pojo.BaseReqParam;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.ValidateMsg;
import com.eccang.spider.amazon.pojo.dict.API;
import com.eccang.spider.amazon.pojo.dict.Customer;
import com.eccang.spider.amazon.pojo.dict.Platform;
import com.eccang.spider.amazon.service.dict.APIService;
import com.eccang.spider.amazon.service.dict.CustomerService;
import com.eccang.spider.amazon.service.dict.PlatformService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    protected CustomerBusinessService mCustomerBusinessService;

    protected static final String IS_SUCCESS = "isSuccess";
    protected static final String MESSAGE = "message";

    protected Logger sLogger = Logger.getLogger(getClass());

    public BaseRspParam auth(String json) {
        sLogger.info(json);
        BaseRspParam baseRspParam = new BaseRspParam();

        if (StringUtils.isEmpty(json)) {
            sLogger.warn("Param is null.");
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.EMPTY_PARAM;
            return baseRspParam;
        }

        BaseReqParam baseReqParam;
        try {
            baseReqParam = new Gson().fromJson(json, BaseReqParam.class);
        } catch (Exception e) {
            sLogger.warn("Parameter format error.");
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam;
        }

        baseRspParam.customerCode = baseReqParam.customerCode;

        /* 授权信息必须存在 */
        if (StringUtils.isEmpty(baseReqParam.customerCode) ||
                StringUtils.isEmpty(baseReqParam.platformCode) || StringUtils.isEmpty(baseReqParam.token)) {
            sLogger.warn("permission info is not complete !!!");
            sLogger.warn(baseReqParam);
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.AUTH_FAILURE;
            baseRspParam.msg = R.RequestMsg.AUTH_INFO;
            return baseRspParam;
        }

        /* 客户信息授权校验 */
        Customer customer = mCustomerService.findByCode(baseReqParam.customerCode);
        if (customer == null) {
            sLogger.warn("customer is not exist !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.AUTH_FAILURE;
            baseRspParam.msg = R.RequestMsg.CUSTOMER_WRONG;
            return baseRspParam;
        }
        if (customer.status == 0) {
            sLogger.warn("customer is limited !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.AUTH_FAILURE;
            baseRspParam.msg = R.RequestMsg.CUSTOMER_NOT_OPEN;
        }

        /* API授权校验 */
        API api = mAPIService.findByCode(customer.code);
        if (api == null) {
            sLogger.warn("api is not exist !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.AUTH_FAILURE;
            baseRspParam.msg = R.RequestMsg.API_NOT_AUTH;
            return baseRspParam;
        }
        if (api.status == 0) {
            sLogger.warn("api is limited !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.AUTH_FAILURE;
            baseRspParam.msg = R.RequestMsg.API_NOT_OPEN;
            return baseRspParam;
        }
        if (!api.token.equals(baseReqParam.token)) {
            sLogger.warn("api is limited !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.AUTH_FAILURE;
            baseRspParam.msg = R.RequestMsg.TOKEN_ERROR;
            return baseRspParam;
        }

        /* 平台信息授权校验 */
        Platform platform = mPlatformService.findByCode(baseReqParam.platformCode);
        if (platform == null) {
            sLogger.warn("platform is not exist !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.AUTH_FAILURE;
            baseRspParam.msg = R.RequestMsg.WRONG_PLATFORM;
            return baseRspParam;
        }
        if (platform.status == 0) {
            sLogger.warn("platform is not exist !!!");
            baseRspParam.setSuccess(false);
            baseRspParam.status = R.HttpStatus.AUTH_FAILURE;
            baseRspParam.msg = R.RequestMsg.PLATFORM_CANNOT_CALL;
            return baseRspParam;
        }

        baseRspParam.setSuccess(true);
        baseRspParam.status = R.HttpStatus.SUCCESS;
        baseRspParam.msg = R.RequestMsg.SUCCESS;

        return baseRspParam;
    }

    protected void serverException(BaseRspParam baseRspParam, Exception e) {
        sLogger.error(e);
        baseRspParam.status = R.HttpStatus.SERVER_EXCEPTION;
        baseRspParam.msg = R.RequestMsg.SERVER_EXCEPTION;
    }

    protected <T> T parseRequestParam(String json, BaseRspParam baseRspParam, Class<T> clazz) {
        T t = null;
        try {
            t = new Gson().fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            sLogger.error(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_FORMAT_ERROR;
        }
        return t;
    }

    protected ValidateMsg getValidateMsg(boolean isSuccess, String msg) {
        return new ValidateMsg(isSuccess, msg);
    }
}