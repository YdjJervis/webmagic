
package com.eccang.wsclient.review;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ReviewWS", targetNamespace = "http://cxf.eccang.com/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ReviewWS {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addToMonitor", targetNamespace = "http://cxf.eccang.com/", className = "com.eccang.wsclient.review.AddToMonitor")
    @ResponseWrapper(localName = "addToMonitorResponse", targetNamespace = "http://cxf.eccang.com/", className = "com.eccang.wsclient.review.AddToMonitorResponse")
    public String addToMonitor(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns com.eccang.wsclient.review.BaseRspParam
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "auth", targetNamespace = "http://cxf.eccang.com/", className = "com.eccang.wsclient.review.Auth")
    @ResponseWrapper(localName = "authResponse", targetNamespace = "http://cxf.eccang.com/", className = "com.eccang.wsclient.review.AuthResponse")
    public BaseRspParam auth(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}
