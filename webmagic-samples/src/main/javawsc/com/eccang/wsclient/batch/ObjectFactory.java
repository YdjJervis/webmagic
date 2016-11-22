
package com.eccang.wsclient.batch;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.eccang.wsclient.batch package. 
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

    private final static QName _GetBatchInfoResponse_QNAME = new QName("http://cxf.eccang.com/", "getBatchInfoResponse");
    private final static QName _Auth_QNAME = new QName("http://cxf.eccang.com/", "auth");
    private final static QName _GetBatchInfo_QNAME = new QName("http://cxf.eccang.com/", "getBatchInfo");
    private final static QName _AuthResponse_QNAME = new QName("http://cxf.eccang.com/", "authResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.eccang.wsclient.batch
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetBatchInfoResponse }
     * 
     */
    public GetBatchInfoResponse createGetBatchInfoResponse() {
        return new GetBatchInfoResponse();
    }

    /**
     * Create an instance of {@link AuthResponse }
     * 
     */
    public AuthResponse createAuthResponse() {
        return new AuthResponse();
    }

    /**
     * Create an instance of {@link GetBatchInfo }
     * 
     */
    public GetBatchInfo createGetBatchInfo() {
        return new GetBatchInfo();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBatchInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "getBatchInfoResponse")
    public JAXBElement<GetBatchInfoResponse> createGetBatchInfoResponse(GetBatchInfoResponse value) {
        return new JAXBElement<GetBatchInfoResponse>(_GetBatchInfoResponse_QNAME, GetBatchInfoResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBatchInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cxf.eccang.com/", name = "getBatchInfo")
    public JAXBElement<GetBatchInfo> createGetBatchInfo(GetBatchInfo value) {
        return new JAXBElement<GetBatchInfo>(_GetBatchInfo_QNAME, GetBatchInfo.class, null, value);
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
