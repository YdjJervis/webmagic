
package com.eccang.wsclient.review;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.eccang.wsclient.review package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddToMonitor_QNAME = new QName("http://cxf.eccang.com/", "addToMonitor");
    private final static QName _AddToMonitorResponse_QNAME = new QName("http://cxf.eccang.com/", "addToMonitorResponse");
    private final static QName _GetReviews_QNAME = new QName("http://cxf.eccang.com/", "getReviews");
    private final static QName _GetReviewsResponse_QNAME = new QName("http://cxf.eccang.com/", "getReviewsResponse");
    private final static QName _Auth_QNAME = new QName("http://cxf.eccang.com/", "auth");
    private final static QName _AuthResponse_QNAME = new QName("http://cxf.eccang.com/", "authResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.eccang.wsclient.review
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddToMonitorResponse }
     * 
     */
    public AddToMonitorResponse createAddToMonitorResponse() {
        return new AddToMonitorResponse();
    }

    /**
     * Create an instance of {@link AddToMonitor }
     * 
     */
    public AddToMonitor createAddToMonitor() {
        return new AddToMonitor();
    }

    /**
     * Create an instance of {@link GetReviews }
     * 
     */
    public GetReviews createGetReviews() {
        return new GetReviews();
    }

    /**
     * Create an instance of {@link GetReviewsResponse }
     * 
     */
    public GetReviewsResponse createGetReviewsResponse() {
        return new GetReviewsResponse();
    }

    /**
     * Create an instance of {@link AuthResponse }
     * 
     */
    public AuthResponse createAuthResponse() {
        return new AuthResponse();
    }

    /**
     * Create an instance of {@link Auth }
     * 
     */
    public Auth createAuth() {
        return new Auth();
    }

    /**
     * Create an instance of {@link BaseRspParam }
     * 
     */
    public BaseRspParam createBaseRspParam() {
        return new BaseRspParam();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddToMonitor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "addToMonitor")
    public JAXBElement<AddToMonitor> createAddToMonitor(AddToMonitor value) {
        return new JAXBElement<AddToMonitor>(_AddToMonitor_QNAME, AddToMonitor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddToMonitorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "addToMonitorResponse")
    public JAXBElement<AddToMonitorResponse> createAddToMonitorResponse(AddToMonitorResponse value) {
        return new JAXBElement<AddToMonitorResponse>(_AddToMonitorResponse_QNAME, AddToMonitorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReviews }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "getReviews")
    public JAXBElement<GetReviews> createGetReviews(GetReviews value) {
        return new JAXBElement<GetReviews>(_GetReviews_QNAME, GetReviews.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReviewsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "getReviewsResponse")
    public JAXBElement<GetReviewsResponse> createGetReviewsResponse(GetReviewsResponse value) {
        return new JAXBElement<GetReviewsResponse>(_GetReviewsResponse_QNAME, GetReviewsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Auth }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "auth")
    public JAXBElement<Auth> createAuth(Auth value) {
        return new JAXBElement<Auth>(_Auth_QNAME, Auth.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "authResponse")
    public JAXBElement<AuthResponse> createAuthResponse(AuthResponse value) {
        return new JAXBElement<AuthResponse>(_AuthResponse_QNAME, AuthResponse.class, null, value);
    }

}
