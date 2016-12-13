
package com.eccang.wsclient.api;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.eccang.wsclient.api package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.eccang.wsclient.api
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PushMessage }
     * 
     */
    public PushMessage createPushMessage() {
        return new PushMessage();
    }

    /**
     * Create an instance of {@link PushMessageResponse }
     * 
     */
    public PushMessageResponse createPushMessageResponse() {
        return new PushMessageResponse();
    }

}
