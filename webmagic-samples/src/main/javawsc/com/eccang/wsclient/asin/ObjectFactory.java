
package com.eccang.wsclient.asin;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.eccang.wsclient.asin package. 
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

    private final static QName _GetAsins_QNAME = new QName("http://cxf.eccang.com/", "getAsins");
    private final static QName _AddToCrawl_QNAME = new QName("http://cxf.eccang.com/", "addToCrawl");
    private final static QName _GetAsinsResponse_QNAME = new QName("http://cxf.eccang.com/", "getAsinsResponse");
    private final static QName _SetPriority_QNAME = new QName("http://cxf.eccang.com/", "setPriority");
    private final static QName _AddToCrawlResponse_QNAME = new QName("http://cxf.eccang.com/", "addToCrawlResponse");
    private final static QName _SetPriorityResponse_QNAME = new QName("http://cxf.eccang.com/", "setPriorityResponse");
    private final static QName _Auth_QNAME = new QName("http://cxf.eccang.com/", "auth");
    private final static QName _AuthResponse_QNAME = new QName("http://cxf.eccang.com/", "authResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.eccang.wsclient.asin
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddToCrawl }
     * 
     */
    public AddToCrawl createAddToCrawl() {
        return new AddToCrawl();
    }

    /**
     * Create an instance of {@link GetAsins }
     * 
     */
    public GetAsins createGetAsins() {
        return new GetAsins();
    }

    /**
     * Create an instance of {@link GetAsinsResponse }
     * 
     */
    public GetAsinsResponse createGetAsinsResponse() {
        return new GetAsinsResponse();
    }

    /**
     * Create an instance of {@link SetPriorityResponse }
     * 
     */
    public SetPriorityResponse createSetPriorityResponse() {
        return new SetPriorityResponse();
    }

    /**
     * Create an instance of {@link AddToCrawlResponse }
     * 
     */
    public AddToCrawlResponse createAddToCrawlResponse() {
        return new AddToCrawlResponse();
    }

    /**
     * Create an instance of {@link SetPriority }
     * 
     */
    public SetPriority createSetPriority() {
        return new SetPriority();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAsins }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "getAsins")
    public JAXBElement<GetAsins> createGetAsins(GetAsins value) {
        return new JAXBElement<GetAsins>(_GetAsins_QNAME, GetAsins.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddToCrawl }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "addToCrawl")
    public JAXBElement<AddToCrawl> createAddToCrawl(AddToCrawl value) {
        return new JAXBElement<AddToCrawl>(_AddToCrawl_QNAME, AddToCrawl.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAsinsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "getAsinsResponse")
    public JAXBElement<GetAsinsResponse> createGetAsinsResponse(GetAsinsResponse value) {
        return new JAXBElement<GetAsinsResponse>(_GetAsinsResponse_QNAME, GetAsinsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetPriority }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "setPriority")
    public JAXBElement<SetPriority> createSetPriority(SetPriority value) {
        return new JAXBElement<SetPriority>(_SetPriority_QNAME, SetPriority.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddToCrawlResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "addToCrawlResponse")
    public JAXBElement<AddToCrawlResponse> createAddToCrawlResponse(AddToCrawlResponse value) {
        return new JAXBElement<AddToCrawlResponse>(_AddToCrawlResponse_QNAME, AddToCrawlResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetPriorityResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "setPriorityResponse")
    public JAXBElement<SetPriorityResponse> createSetPriorityResponse(SetPriorityResponse value) {
        return new JAXBElement<SetPriorityResponse>(_SetPriorityResponse_QNAME, SetPriorityResponse.class, null, value);
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
