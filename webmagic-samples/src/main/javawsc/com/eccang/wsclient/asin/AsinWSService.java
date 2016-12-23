
package com.eccang.wsclient.asin;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "AsinWSService", targetNamespace = "http://cxf.eccang.com/", wsdlLocation = "http://192.168.100.109:8080/eccang/ws/asin?wsdl")
public class AsinWSService
    extends Service
{

    private final static URL ASINWSSERVICE_WSDL_LOCATION;
    private final static WebServiceException ASINWSSERVICE_EXCEPTION;
    private final static QName ASINWSSERVICE_QNAME = new QName("http://cxf.eccang.com/", "AsinWSService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://192.168.100.109:8080/eccang/ws/asin?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        ASINWSSERVICE_WSDL_LOCATION = url;
        ASINWSSERVICE_EXCEPTION = e;
    }

    public AsinWSService() {
        super(__getWsdlLocation(), ASINWSSERVICE_QNAME);
    }

    public AsinWSService(WebServiceFeature... features) {
        super(__getWsdlLocation(), ASINWSSERVICE_QNAME, features);
    }

    public AsinWSService(URL wsdlLocation) {
        super(wsdlLocation, ASINWSSERVICE_QNAME);
    }

    public AsinWSService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, ASINWSSERVICE_QNAME, features);
    }

    public AsinWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AsinWSService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns AsinWS
     */
    @WebEndpoint(name = "AsinWSPort")
    public AsinWS getAsinWSPort() {
        return super.getPort(new QName("http://cxf.eccang.com/", "AsinWSPort"), AsinWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AsinWS
     */
    @WebEndpoint(name = "AsinWSPort")
    public AsinWS getAsinWSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://cxf.eccang.com/", "AsinWSPort"), AsinWS.class, features);
    }

    private static URL __getWsdlLocation() {
        if (ASINWSSERVICE_EXCEPTION!= null) {
            throw ASINWSSERVICE_EXCEPTION;
        }
        return ASINWSSERVICE_WSDL_LOCATION;
    }

}
