package com.eccang.cxf;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author Jervis
 * @version V0.20
 * @Description: ASIN操作服务
 * @date 2016/11/17 11:49
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface CustomerAsinWS extends SpiderWS{

    /**
     * 添加ASIN爬取批次
     */
    @WebMethod
    String setCrawl(String jsonArray);
}