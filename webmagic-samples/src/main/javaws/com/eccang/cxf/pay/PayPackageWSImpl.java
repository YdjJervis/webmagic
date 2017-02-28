package com.eccang.cxf.pay;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseReqParam;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.ValidateMsg;
import com.eccang.pojo.pay.*;
import com.eccang.spider.amazon.pojo.dict.PayProfile;
import com.eccang.spider.amazon.pojo.pay.PayPackage;
import com.eccang.spider.amazon.pojo.pay.PayPackageLog;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
import com.eccang.spider.amazon.service.dict.PayProfileService;
import com.eccang.spider.amazon.service.pay.PayCalculatorImpl;
import com.eccang.spider.amazon.service.pay.PayPackageLogService;
import com.eccang.spider.amazon.service.pay.PayPackageService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.*;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 订购套餐WebService接口实现类
 * @date 2017/1/5 14:28
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class PayPackageWSImpl extends AbstractSpiderWS implements PayPackageWS {

    @Autowired
    private PayPackageService mPayPackageService;

    @Autowired
    private PayCalculatorImpl mPayCalculator;
    @Autowired
    private CustomerBusinessService mCustomerBusinessService;
    @Autowired
    private PayPackageLogService mPayPackageLogService;
    @Autowired
    private PayProfileService mPayProfileService;

    @Override
    public String buy(String json) {

        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusPayAddReq payPackageReq = parseRequestParam(json, baseRspParam, CusPayAddReq.class);
        if (payPackageReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        ValidateMsg validateMsg = checkData(payPackageReq);
        if (!validateMsg.isSuccess) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = validateMsg.msg;
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        CusPayAddRsp payPackageRsp = new CusPayAddRsp();
        payPackageRsp.customerCode = payPackageReq.customerCode;
        payPackageRsp.status = baseRspParam.status;
        payPackageRsp.msg = baseRspParam.msg;

        try {
            /* 把客户和套餐的关系保存起来 */
            CustomerPayPackage cusPayPackage = new CustomerPayPackage();
            cusPayPackage.customerCode = payPackageReq.customerCode;
            cusPayPackage.packageCode = payPackageReq.data.payPackageCode;

            if (mCustomerPayPackageService.isExist(cusPayPackage.customerCode, cusPayPackage.packageCode)) {
                payPackageRsp.status = R.HttpStatus.BUY_ERROR;
                payPackageRsp.msg = R.RequestMsg.PAY_PACKAGE_BUIED;
                return payPackageRsp.toJson();
            }
            if (!mPayPackageService.isExist(cusPayPackage.packageCode)) {
                payPackageRsp.status = R.HttpStatus.BUY_ERROR;
                payPackageRsp.msg = R.RequestMsg.PAY_PACKAGE_MISSED;
                return payPackageRsp.toJson();
            }

            mCustomerPayPackageService.add(cusPayPackage);

            /* 最后记录套餐操作日志 */
            PayPackageLog log = new PayPackageLog(payPackageReq.customerCode);
            log.curd = PayPackageLog.Curd.ADD;
            log.describe = PayPackageLog.Describe.MSG_1;
            log.price = mPayPackageStubService.findTotalPrice(cusPayPackage.packageCode);
            mPayPackageLogService.add(log);

        } catch (Exception e) {
            serverException(baseRspParam, e);
        }

        return payPackageRsp.toJson();
    }

    @Override
    public String buyCustom(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusPayCustomAddReq payPackageReq = parseRequestParam(json, baseRspParam, CusPayCustomAddReq.class);
        if (payPackageReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        ValidateMsg validateMsg = checkData(payPackageReq);
        if (!validateMsg.isSuccess) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = validateMsg.msg;
            return baseRspParam.toJson();
        }

        try {
            /* 数据有效性二次校验 */

            /* 检查是否有不存在的业务 */
            for (CusPayCustomAddReq.PayPackageStub payPackageStub : payPackageReq.data) {
                if (!mBusinessService.isExist(payPackageStub.businessCode)) {
                    baseRspParam.status = R.HttpStatus.BUY_ERROR;
                    baseRspParam.msg = R.RequestMsg.BUSINESS_MISSED;
                    return baseRspParam.toJson();
                }
            }

            /* 检查是否有相同的业务 */
            int total = payPackageReq.data.size();
            Set<String> businessCodeSet = new HashSet<>();
            for (CusPayCustomAddReq.PayPackageStub payPackageStub : payPackageReq.data) {
                businessCodeSet.add(payPackageStub.businessCode);
            }
            if (total != businessCodeSet.size()) {
                baseRspParam.status = R.HttpStatus.BUY_ERROR;
                baseRspParam.msg = R.RequestMsg.SAME_BUSINESS;
                return baseRspParam.toJson();
            }

            /* 检查购买数量限制 */
            for (CusPayCustomAddReq.PayPackageStub payPackageStub : payPackageReq.data) {
                PayProfile payProfile = mPayProfileService.findByCode(payPackageStub.businessCode);
                if (payPackageStub.day > payProfile.dayLimit) {
                    baseRspParam.status = R.HttpStatus.BUY_ERROR;
                    baseRspParam.msg = R.RequestMsg.DAY_LIMIT;
                    return baseRspParam.toJson();
                }
                if (payPackageStub.count > payProfile.countLimit) {
                    baseRspParam.status = R.HttpStatus.BUY_ERROR;
                    baseRspParam.msg = R.RequestMsg.COUNT_LIMIT;
                    return baseRspParam.toJson();
                }
            }

        } catch (Exception e) {
            serverException(baseRspParam, e);
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        CusPayCustomAddRsp payPackageRsp = new CusPayCustomAddRsp();
        payPackageRsp.customerCode = payPackageReq.customerCode;
        payPackageRsp.status = baseRspParam.status;
        payPackageRsp.msg = baseRspParam.msg;

        try {
            String payPackageCode = generatePayPackageCode();

            /* 子业务套餐入库 */
            List<PayPackageStub> payPackageStubList = new ArrayList<>();
            PayPackageStub stub;
            for (CusPayCustomAddReq.PayPackageStub payPackageStub : payPackageReq.data) {
                stub = new PayPackageStub();
                stub.stubCode = generateStubCode();
                stub.payPackageCode = payPackageCode;
                stub.businessCode = payPackageStub.businessCode;
                stub.day = payPackageStub.day;
                stub.priority = payPackageStub.priority;
                stub.frequency = payPackageStub.frequency;
                stub.count = payPackageStub.count;
                stub.price = mPayCalculator.calculate(stub, 1);
                payPackageStubList.add(stub);

                /* 同步到限制表 */
                CustomerBusiness cusBussiness = new CustomerBusiness();
                cusBussiness.customerCode = payPackageRsp.customerCode;
                cusBussiness.businessCode = stub.businessCode;
                cusBussiness.maxData = stub.count;
                mCustomerBusinessService.add(cusBussiness);
            }
            mPayPackageStubService.addAll(payPackageStubList);

            /* 总业务套餐入库 */
            PayPackage payPackage = new PayPackage();
            payPackage.code = payPackageCode;
            payPackage.custom = 1;
            mPayPackageService.add(payPackage);

            /* 让该客户购买的所有套餐失效 */
            mCustomerPayPackageService.cancelAll(payPackageReq.customerCode);

            /* 把客户和套餐的关系保存起来 */
            CustomerPayPackage cusPayPackage = new CustomerPayPackage();
            cusPayPackage.customerCode = payPackageReq.customerCode;
            cusPayPackage.packageCode = payPackageCode;
            mCustomerPayPackageService.add(cusPayPackage);

            /* 把套餐码返回给调用处 */
            payPackageRsp.data.payPackageCode = payPackageCode;

            /* 最后记录套餐操作日志 */
            PayPackageLog log = new PayPackageLog(payPackageReq.customerCode);
            log.curd = PayPackageLog.Curd.ADD;
            log.describe = PayPackageLog.Describe.MSG_2;
            log.price = mPayPackageStubService.findTotalPrice(cusPayPackage.packageCode);
            mPayPackageLogService.add(log);
        } catch (Exception e) {
            serverException(baseRspParam, e);
        }

        return payPackageRsp.toJson();
    }

    @Override
    public String getList(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        PayPackageQueryReq payPackageQueryReq = parseRequestParam(json, baseRspParam, PayPackageQueryReq.class);
        if (payPackageQueryReq == null) {
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        PayPackageQueryRsp payPackageRsp = new PayPackageQueryRsp();
        payPackageRsp.customerCode = payPackageQueryReq.customerCode;
        payPackageRsp.status = baseRspParam.status;
        payPackageRsp.msg = baseRspParam.msg;

        try {
            /* 查询内建的套餐 */
            List<PayPackage> payPackageList = mPayPackageService.findBuildIn();
            parsePayPackage(payPackageRsp, payPackageList);

        } catch (Exception e) {
            serverException(baseRspParam, e);
        }

        return payPackageRsp.toJson();
    }

    private void parsePayPackage(PayPackageQueryRsp payPackageRsp, List<PayPackage> payPackageList) {
        for (PayPackage pay : payPackageList) {
            List<PayPackageStub> stubList = mPayPackageStubService.findByPayPackage(pay.code);

            PayPackageQueryRsp.PayPackage payPackage = payPackageRsp.new PayPackage();
            payPackage.payPackageCode = pay.code;
            payPackage.status = pay.status;

            for (PayPackageStub packageStub : stubList) {
                PayPackageQueryRsp.PayPackage.PayPackageStub payPackageStub = payPackage.new PayPackageStub();

                payPackageStub.businessCode = packageStub.businessCode;
                payPackageStub.day = packageStub.day;
                payPackageStub.priority = packageStub.priority;
                payPackageStub.frequency = packageStub.frequency;
                payPackageStub.count = packageStub.count;
                payPackageStub.averageTime = packageStub.averageTime;
                payPackageStub.price = packageStub.price;

                payPackage.price += packageStub.price;

                payPackage.stubs.add(payPackageStub);
            }
            payPackageRsp.data.add(payPackage);
        }
    }

    @Override
    public String getPaied(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        PaiedQueryReq paiedQueryReq = parseRequestParam(json, baseRspParam, PaiedQueryReq.class);
        if (paiedQueryReq == null) {
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        PayPackageQueryRsp payPackageRsp = new PayPackageQueryRsp();
        payPackageRsp.customerCode = paiedQueryReq.customerCode;
        payPackageRsp.status = baseRspParam.status;
        payPackageRsp.msg = baseRspParam.msg;

        try {
            /* 查询客户订购过的所有套餐 */
            List<CustomerPayPackage> customerPayPackageList = mCustomerPayPackageService.findByCustomerCode(paiedQueryReq.customerCode);

            List<PayPackage> payPackageList = new ArrayList<>();
            for (CustomerPayPackage customerPayPackage : customerPayPackageList) {
                PayPackage payPackage = mPayPackageService.findByCode(customerPayPackage.packageCode);
                payPackage.status = customerPayPackage.status;
                payPackageList.add(payPackage);
            }

            /* 查询内建的套餐 */
            parsePayPackage(payPackageRsp, payPackageList);

        } catch (Exception e) {
            serverException(baseRspParam, e);
        }

        return payPackageRsp.toJson();
    }

    private ValidateMsg checkData(BaseReqParam baseReqParam) {

        if (baseReqParam instanceof CusPayCustomAddReq) {
            CusPayCustomAddReq payPackageReq = (CusPayCustomAddReq) baseReqParam;

            if (CollectionUtils.isEmpty(payPackageReq.data)) {
                return getValidateMsg(false, R.RequestMsg.PARAMETER_DATA_NULL_ERROR);
            }

            for (CusPayCustomAddReq.PayPackageStub payPackageStub : payPackageReq.data) {
                if (!RegexUtil.isPriorityQualified(payPackageStub.priority)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_ASIN_PRIORITY_ERROR);
                }
                if (!RegexUtil.isPositiveNumQualified(payPackageStub.frequency)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_POSITIVE_NUM_ERROR);
                }
                if (!RegexUtil.isPositiveNumQualified(payPackageStub.day)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_POSITIVE_NUM_ERROR);
                }
                if (!RegexUtil.isPositiveNumQualified(payPackageStub.count)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_POSITIVE_NUM_ERROR);
                }
            }

        } else if (baseReqParam instanceof CusPayAddReq) {
            CusPayAddReq payPackageReq = (CusPayAddReq) baseReqParam;

            if (StringUtils.isEmpty(payPackageReq.data.payPackageCode)) {
                return getValidateMsg(false, R.RequestMsg.PAY_PACKAGE_NULL);
            }

        }

        return getValidateMsg(true, R.RequestMsg.SUCCESS);
    }


    private String generatePayPackageCode() {
        String code = UUID.randomUUID().toString().substring(0, 6);
        if (mPayPackageService.findByCode(code) != null) {
            return generatePayPackageCode();
        } else {
            return code;
        }
    }

    private String generateStubCode() {
        String code = UUID.randomUUID().toString().substring(0, 6);
        if (mPayPackageStubService.findByCode(code) != null) {
            return generateStubCode();
        } else {
            return code;
        }
    }

}
