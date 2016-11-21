
package com.eccang.wsclient.validate;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetVerCodeFromImageStringResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getVerCodeFromImageStringResult"
})
@XmlRootElement(name = "GetVerCodeFromImageStringResponse")
public class GetVerCodeFromImageStringResponse {

    @XmlElementRef(name = "GetVerCodeFromImageStringResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> getVerCodeFromImageStringResult;

    /**
     * Gets the value of the getVerCodeFromImageStringResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGetVerCodeFromImageStringResult() {
        return getVerCodeFromImageStringResult;
    }

    /**
     * Sets the value of the getVerCodeFromImageStringResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGetVerCodeFromImageStringResult(JAXBElement<String> value) {
        this.getVerCodeFromImageStringResult = value;
    }

}
