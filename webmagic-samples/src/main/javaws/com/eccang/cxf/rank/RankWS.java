package com.eccang.cxf.rank;

import com.eccang.cxf.SpiderWS;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/2 13:56
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface RankWS extends SpiderWS {

    @WebMethod
    String addToMonitor(String json);

    @WebMethod
    String setStatus(String json);

    @WebMethod
    String getKeywordRankInfo(String json);

    @WebMethod
    String getMonitorList(String json);
}
