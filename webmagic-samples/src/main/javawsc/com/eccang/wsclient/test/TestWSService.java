
package com.eccang.wsclient.test;

import us.codecraft.webmagic.samples.base.util.PropertyUtil;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.URL;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "TestWSService", targetNamespace = "http://cxf.eccang.com/", wsdlLocation = "http://localhost:8080/eccang/ws/test?wsdl")
public class TestWSService
    extends Service
{

    private final static URL TESTWSSERVICE_WSDL_LOCATION;
    private final static WebServiceException TESTWSSERVICE_EXCEPTION;
    private final static QName TESTWSSERVICE_QNAME = new QName("http://cxf.eccang.com/", "TestWSService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            String value = PropertyUtil.getValue("ws.properties", "spiderWSIPPort");
            url = new URL(value + "/eccang/ws/test?wsdl");
        } catch (Exception ex) {
            e = new WebServiceException(ex);
        }
        TESTWSSERVICE_WSDL_LOCATION = url;
        TESTWSSERVICE_EXCEPTION = e;
    }

    public TestWSService() {
        super(__getWsdlLocation(), TESTWSSERVICE_QNAME);
    }

    public TestWSService(WebServiceFeature... features) {
        super(__getWsdlLocation(), TESTWSSERVICE_QNAME, features);
    }

    public TestWSService(URL wsdlLocation) {
        super(wsdlLocation, TESTWSSERVICE_QNAME);
    }

    public TestWSService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, TESTWSSERVICE_QNAME, features);
    }

    public TestWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TestWSService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns TestWS
     */
    @WebEndpoint(name = "TestWSPort")
    public TestWS getTestWSPort() {
        return super.getPort(new QName("http://cxf.eccang.com/", "TestWSPort"), TestWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TestWS
     */
    @WebEndpoint(name = "TestWSPort")
    public TestWS getTestWSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://cxf.eccang.com/", "TestWSPort"), TestWS.class, features);
    }

    private static URL __getWsdlLocation() {
        if (TESTWSSERVICE_EXCEPTION!= null) {
            throw TESTWSSERVICE_EXCEPTION;
        }
        return TESTWSSERVICE_WSDL_LOCATION;
    }

}
