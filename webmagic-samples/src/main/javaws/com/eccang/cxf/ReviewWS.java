package com.eccang.cxf;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author Jervis
 * @version V0.20
 * @Description: ASIN操作服务
 * @date 2016/11/17 11:49
 */
@WebService
public interface ReviewWS extends SpiderWS{

    @WebMethod
    String addToMonitor(String json);
}