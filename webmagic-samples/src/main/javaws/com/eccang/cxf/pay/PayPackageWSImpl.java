package com.eccang.cxf.pay;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseReqParam;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.ValidateMsg;
import com.eccang.pojo.pay.CusPayAddReq;
import com.eccang.pojo.pay.CusPayAddRsp;
import com.eccang.spider.amazon.pojo.pay.PayPackage;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
import com.eccang.spider.amazon.service.pay.PayCalculatorImpl;
import com.eccang.spider.amazon.service.pay.PayPackageService;
import com.eccang.spider.amazon.service.pay.PayPackageStubService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.spider.amazon.service.relation.CustomerPayPackageService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private PayPackageStubService mPayPackageStubService;
    @Autowired
    private CustomerPayPackageService mCustomerPayPackageService;

    @Autowired
    private PayCalculatorImpl mPayCalculator;
    @Autowired
    private CustomerBusinessService mCustomerBusinessService;

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

        /*  参数验证阶段 */
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
            String payPackageCode = generatePayPackageCode();

            /* 子业务套餐入库 */
            List<PayPackageStub> payPackageStubList = new ArrayList<>();
            PayPackageStub stub;
            for (CusPayAddReq.PayPackageStub payPackageStub : payPackageReq.data) {
                stub = new PayPackageStub();
                stub.code = generateStubCode();
                stub.custom = 1;
                stub.businessCode = payPackageStub.businessCode;
                stub.day = payPackageStub.day;
                stub.priority = payPackageStub.priority;
                stub.frequency = payPackageStub.frequency;
                stub.count = payPackageStub.count;
                stub.price = mPayCalculator.calculate(stub);
                payPackageStubList.add(stub);

                /* 总业务套餐入库 */
                PayPackage payPackage = new PayPackage();
                payPackage.code = payPackageCode;
                payPackage.stubCode = stub.code;
                mPayPackageService.add(payPackage);

                /* 同步到限制表 */
                CustomerBusiness cusBussiness = new CustomerBusiness();
                cusBussiness.customerCode = payPackageRsp.customerCode;
                cusBussiness.businessCode = stub.businessCode;
                cusBussiness.maxData = stub.count;
                mCustomerBusinessService.add(cusBussiness);
            }
            mPayPackageStubService.addAll(payPackageStubList);

            /* 把客户和套餐的关系保存起来 */
            CustomerPayPackage cusPayPackage = new CustomerPayPackage();
            cusPayPackage.customerCode = payPackageReq.customerCode;
            cusPayPackage.packageCode = payPackageCode;
            mCustomerPayPackageService.add(cusPayPackage);

        } catch (Exception e) {
            serverException(baseRspParam, e);
        }

        return payPackageRsp.toJson();

    }

    private ValidateMsg checkData(BaseReqParam baseReqParam) {

        if (baseReqParam instanceof CusPayAddReq) {
            CusPayAddReq payPackageReq = (CusPayAddReq) baseReqParam;

            if (CollectionUtils.isEmpty(payPackageReq.data)) {
                return getValidateMsg(false, R.RequestMsg.PARAMETER_DATA_NULL_ERROR);
            }

            for (CusPayAddReq.PayPackageStub payPackageStub : payPackageReq.data) {
                break;
            }

        }

        return getValidateMsg(true, R.RequestMsg.SUCCESS);
    }

    private String generatePayPackageCode() {
        String code = UUID.randomUUID().toString().substring(0, 6);
        if (CollectionUtils.isNotEmpty(mPayPackageService.findByPayPackageCode(code))) {
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
