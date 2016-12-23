
package com.eccang.wsclient.validate;

import us.codecraft.webmagic.samples.base.util.PropertyUtil;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 */
@WebServiceClient(name = "ImageOCRService", targetNamespace = "http://tempuri.org/", wsdlLocation = "http://119.28.63.234:1800/ImageOCRService.svc?singleWsdl")
public class ImageOCRService
        extends Service {

    private final static URL IMAGEOCRSERVICE_WSDL_LOCATION;
    private final static WebServiceException IMAGEOCRSERVICE_EXCEPTION;
    private final static QName IMAGEOCRSERVICE_QNAME = new QName("http://tempuri.org/", "ImageOCRService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            String value = PropertyUtil.getValue("ws.properties", "wsdlLocation");
            url = new URL(value + "/ImageOCRService.svc?singleWsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        } catch (Exception ex) {
            e = new WebServiceException(ex);
        }
        IMAGEOCRSERVICE_WSDL_LOCATION = url;
        IMAGEOCRSERVICE_EXCEPTION = e;
    }

    public ImageOCRService() {
        super(__getWsdlLocation(), IMAGEOCRSERVICE_QNAME);
    }

    public ImageOCRService(WebServiceFeature... features) {
        super(__getWsdlLocation(), IMAGEOCRSERVICE_QNAME, features);
    }

    public ImageOCRService(URL wsdlLocation) {
        super(wsdlLocation, IMAGEOCRSERVICE_QNAME);
    }

    public ImageOCRService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, IMAGEOCRSERVICE_QNAME, features);
    }

    public ImageOCRService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ImageOCRService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * @return returns IImageOCRService
     */
    @WebEndpoint(name = "BasicHttpBinding_IImageOCRService")
    public IImageOCRService getBasicHttpBindingIImageOCRService() {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_IImageOCRService"), IImageOCRService.class);
    }

    /**
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns IImageOCRService
     */
    @WebEndpoint(name = "BasicHttpBinding_IImageOCRService")
    public IImageOCRService getBasicHttpBindingIImageOCRService(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_IImageOCRService"), IImageOCRService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (IMAGEOCRSERVICE_EXCEPTION != null) {
            throw IMAGEOCRSERVICE_EXCEPTION;
        }
        return IMAGEOCRSERVICE_WSDL_LOCATION;
    }

}
