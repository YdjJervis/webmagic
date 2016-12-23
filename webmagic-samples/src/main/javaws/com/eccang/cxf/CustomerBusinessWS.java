package com.eccang.cxf;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author Hardy
 * @version V0.2
 * 客户下的业务操作服务
 * 2016/12/15 17:06
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface CustomerBusinessWS extends SpiderWS{

    /**
     *查询客户下的业务状态
     */
    @WebMethod
    String queryBusiness(String jsonArray);
}
