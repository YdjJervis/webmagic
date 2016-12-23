
package com.eccang.wsclient.batch;

import us.codecraft.webmagic.samples.base.util.PropertyUtil;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.URL;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 */
@WebServiceClient(name = "BatchWSService", targetNamespace = "http://cxf.eccang.com/", wsdlLocation = "http://localhost:8080/eccang/ws/batch?wsdl")
public class BatchWSService
        extends Service {

    private final static URL BATCHWSSERVICE_WSDL_LOCATION;
    private final static WebServiceException BATCHWSSERVICE_EXCEPTION;
    private final static QName BATCHWSSERVICE_QNAME = new QName("http://cxf.eccang.com/", "BatchWSService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            String value = PropertyUtil.getValue("ws.properties", "spiderWSIPPort");
            url = new URL(value + "/eccang/ws/batch?wsdl");
        } catch (Exception ex) {
            e = new WebServiceException(ex);
        }
        BATCHWSSERVICE_WSDL_LOCATION = url;
        BATCHWSSERVICE_EXCEPTION = e;
    }

    public BatchWSService() {
        super(__getWsdlLocation(), BATCHWSSERVICE_QNAME);
    }

    public BatchWSService(WebServiceFeature... features) {
        super(__getWsdlLocation(), BATCHWSSERVICE_QNAME, features);
    }

    public BatchWSService(URL wsdlLocation) {
        super(wsdlLocation, BATCHWSSERVICE_QNAME);
    }

    public BatchWSService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, BATCHWSSERVICE_QNAME, features);
    }

    public BatchWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BatchWSService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * @return returns BatchWS
     */
    @WebEndpoint(name = "BatchWSPort")
    public BatchWS getBatchWSPort() {
        return super.getPort(new QName("http://cxf.eccang.com/", "BatchWSPort"), BatchWS.class);
    }

    /**
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns BatchWS
     */
    @WebEndpoint(name = "BatchWSPort")
    public BatchWS getBatchWSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://cxf.eccang.com/", "BatchWSPort"), BatchWS.class, features);
    }

    private static URL __getWsdlLocation() {
        if (BATCHWSSERVICE_EXCEPTION != null) {
            throw BATCHWSSERVICE_EXCEPTION;
        }
        return BATCHWSSERVICE_WSDL_LOCATION;
    }

}
