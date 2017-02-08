package com.eccang.cxf.pay;

import com.eccang.cxf.SpiderWS;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author Hardy
 * @version V0.2
 * @Description: 套餐订购WebService接口
 * @date 2017/1/2 13:56
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface PayPackageWS extends SpiderWS {

    /**
     * 购买固定套餐
     */
    @WebMethod
    String buy(String json);

    /**
     * 购买自定义套餐
     */
    @WebMethod
    String buyCustom(String json);

    /**
     * 查询固定套餐列表
     */
    @WebMethod
    String getList(String json);

    /**
     * 查询自己订购的套餐
     */
    @WebMethod
    String getPaied(String json);
}
