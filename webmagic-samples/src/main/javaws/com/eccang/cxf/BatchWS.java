package com.eccang.cxf;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次WebService
 * @date 2016/11/17 11:49
 */
@WebService
public interface BatchWS extends SpiderWS{

    /**
     * 获取批次详细信息
     */
    @WebMethod
    String getBatchInfo(String json);
}