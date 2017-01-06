
package com.eccang.wsclient.rank;

import com.eccang.spider.base.util.PropertyUtil;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.URL;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "RankWSService", targetNamespace = "http://rank.cxf.eccang.com/", wsdlLocation = "http://192.168.100.109:8080/eccang/ws/rank?wsdl")
public class RankWSService
    extends Service
{

    private final static URL RANKWSSERVICE_WSDL_LOCATION;
    private final static WebServiceException RANKWSSERVICE_EXCEPTION;
    private final static QName RANKWSSERVICE_QNAME = new QName("http://rank.cxf.eccang.com/", "RankWSService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            String value = PropertyUtil.getValue("ws.properties", "wsdlLocation");
            url = new URL(value + "/eccang/ws/rank?wsdl");
        } catch (Exception ex) {
            e = new WebServiceException(ex);
        }
        RANKWSSERVICE_WSDL_LOCATION = url;
        RANKWSSERVICE_EXCEPTION = e;
    }

    public RankWSService() {
        super(__getWsdlLocation(), RANKWSSERVICE_QNAME);
    }

    public RankWSService(WebServiceFeature... features) {
        super(__getWsdlLocation(), RANKWSSERVICE_QNAME, features);
    }

    public RankWSService(URL wsdlLocation) {
        super(wsdlLocation, RANKWSSERVICE_QNAME);
    }

    public RankWSService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, RANKWSSERVICE_QNAME, features);
    }

    public RankWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RankWSService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns RankWS
     */
    @WebEndpoint(name = "RankWSPort")
    public RankWS getRankWSPort() {
        return super.getPort(new QName("http://rank.cxf.eccang.com/", "RankWSPort"), RankWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RankWS
     */
    @WebEndpoint(name = "RankWSPort")
    public RankWS getRankWSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://rank.cxf.eccang.com/", "RankWSPort"), RankWS.class, features);
    }

    private static URL __getWsdlLocation() {
        if (RANKWSSERVICE_EXCEPTION!= null) {
            throw RANKWSSERVICE_EXCEPTION;
        }
        return RANKWSSERVICE_WSDL_LOCATION;
    }

}
