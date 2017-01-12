package com.eccang.cxf.batch;

import com.eccang.cxf.SpiderWS;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次WebService
 * @date 2016/11/17 11:49
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface BatchWS extends SpiderWS {

    /**
     * 获取批次详细信息
     */
    @WebMethod
    String getBatchInfo(String json);

    @WebMethod
    String getBatches(String json);
}