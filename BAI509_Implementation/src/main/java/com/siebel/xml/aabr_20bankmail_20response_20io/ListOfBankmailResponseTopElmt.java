//
// Generated By:JAX-WS RI IBM 2.0_03-07/07/2008 01:00 PM(foreman)-fcs (JAXB RI IBM 2.0.5-09/06/2010 03:32 PM(foreman)-fcs)
//


package com.siebel.xml.aabr_20bankmail_20response_20io;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListOfBankmailResponseTopElmt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ListOfBankmailResponseTopElmt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ListOfBankmailResponse" type="{http://www.siebel.com/xml/AABR%20Bankmail%20Response%20IO}ListOfBankmailResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListOfBankmailResponseTopElmt", namespace = "http://www.siebel.com/xml/AABR%20Bankmail%20Response%20IO", propOrder = {
    "listOfBankmailResponse"
})
public class ListOfBankmailResponseTopElmt {

    @XmlElement(name = "ListOfBankmailResponse", required = true)
    protected ListOfBankmailResponse listOfBankmailResponse;

    /**
     * Gets the value of the listOfBankmailResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfBankmailResponse }
     *     
     */
    public ListOfBankmailResponse getListOfBankmailResponse() {
        return listOfBankmailResponse;
    }

    /**
     * Sets the value of the listOfBankmailResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfBankmailResponse }
     *     
     */
    public void setListOfBankmailResponse(ListOfBankmailResponse value) {
        this.listOfBankmailResponse = value;
    }

}
