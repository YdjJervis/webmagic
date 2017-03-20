package com.eccang.cxf.review;

import com.eccang.cxf.SpiderWS;

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
public interface ReviewWS extends SpiderWS {

    @WebMethod
    String addToMonitor(String json);

    @WebMethod
    String addToMonitorImmediate(String json);

    @WebMethod
    String setCrawl(String json);

    @WebMethod
    String getReviewsByAsin(String asinJson);

    @WebMethod
    String getReviewById(String json);

    @WebMethod
    String getReviewsStatus(String json);

}