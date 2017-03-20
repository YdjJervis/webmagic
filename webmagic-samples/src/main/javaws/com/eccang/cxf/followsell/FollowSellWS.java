package com.eccang.cxf.followsell;

import com.eccang.cxf.SpiderWS;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author Hardy
 * @version V0.2
 * @Description: 跟卖WebService接口
 * @date 2017/1/2 13:56
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface FollowSellWS extends SpiderWS {

    @WebMethod
    String addToMonitor(String json);

    @WebMethod
    String addToMonitorImmediate(String json);

    @WebMethod
    String setStatus(String json);

    @WebMethod
    String getMonitorList(String json);

    @WebMethod
    String getFollowSellList(String json);

}
