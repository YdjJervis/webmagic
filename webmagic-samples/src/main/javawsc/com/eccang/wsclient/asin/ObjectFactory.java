
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

    private final static QName _Exception_QNAME = new QName("http://cxf.eccang.com/", "Exception");
    private final static QName _AddToCrawl_QNAME = new QName("http://cxf.eccang.com/", "addToCrawl");
    private final static QName _AddToCrawlResponse_QNAME = new QName("http://cxf.eccang.com/", "addToCrawlResponse");
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
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link AddToCrawlResponse }
     * 
     */
    public AddToCrawlResponse createAddToCrawlResponse() {
        return new AddToCrawlResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link AddToCrawlResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "addToCrawlResponse")
    public JAXBElement<AddToCrawlResponse> createAddToCrawlResponse(AddToCrawlResponse value) {
        return new JAXBElement<AddToCrawlResponse>(_AddToCrawlResponse_QNAME, AddToCrawlResponse.class, null, value);
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
