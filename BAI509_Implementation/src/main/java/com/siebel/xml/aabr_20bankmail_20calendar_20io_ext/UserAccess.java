//
// Generated By:JAX-WS RI IBM 2.0_03-07/07/2008 01:00 PM(foreman)-fcs (JAXB RI IBM 2.0.5-09/06/2010 03:32 PM(foreman)-fcs)
//


package com.siebel.xml.aabr_20bankmail_20calendar_20io_ext;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserAccess complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserAccess">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccessSBTID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserAccess", namespace = "http://www.siebel.com/xml/AABR%20Bankmail%20Calendar%20IO_Ext", propOrder = {
    "accessSBTID"
})
public class UserAccess {

    @XmlElement(name = "AccessSBTID")
    protected String accessSBTID;

    /**
     * Gets the value of the accessSBTID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessSBTID() {
        return accessSBTID;
    }

    /**
     * Sets the value of the accessSBTID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessSBTID(String value) {
        this.accessSBTID = value;
    }

}
