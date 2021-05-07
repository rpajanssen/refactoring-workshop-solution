//
// Generated By:JAX-WS RI IBM 2.0_03-07/07/2008 01:00 PM(foreman)-fcs (JAXB RI IBM 2.0.5-09/06/2010 03:32 PM(foreman)-fcs)
//


package com.siebel.xml.aabr_20bankmail_20response_20io;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Users complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Users">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RecExists" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReturnCodes" type="{http://www.siebel.com/xml/AABR%20Bankmail%20Response%20IO}ReturnCodes" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AccessGrant" type="{http://www.siebel.com/xml/AABR%20Bankmail%20Response%20IO}AccessGrant" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="USERSBTID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Users", namespace = "http://www.siebel.com/xml/AABR%20Bankmail%20Response%20IO", propOrder = {
    "recExists",
    "returnCodes",
    "accessGrant",
    "usersbtid"
})
public class Users {

    @XmlElement(name = "RecExists")
    protected String recExists;
    @XmlElement(name = "ReturnCodes")
    protected List<ReturnCodes> returnCodes;
    @XmlElement(name = "AccessGrant")
    protected List<AccessGrant> accessGrant;
    @XmlElement(name = "USERSBTID")
    protected String usersbtid;

    /**
     * Gets the value of the recExists property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecExists() {
        return recExists;
    }

    /**
     * Sets the value of the recExists property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecExists(String value) {
        this.recExists = value;
    }

    /**
     * Gets the value of the returnCodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the returnCodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReturnCodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReturnCodes }
     * 
     * 
     */
    public List<ReturnCodes> getReturnCodes() {
        if (returnCodes == null) {
            returnCodes = new ArrayList<ReturnCodes>();
        }
        return this.returnCodes;
    }

    /**
     * Gets the value of the accessGrant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the accessGrant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccessGrant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccessGrant }
     * 
     * 
     */
    public List<AccessGrant> getAccessGrant() {
        if (accessGrant == null) {
            accessGrant = new ArrayList<AccessGrant>();
        }
        return this.accessGrant;
    }

    /**
     * Gets the value of the usersbtid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSERSBTID() {
        return usersbtid;
    }

    /**
     * Sets the value of the usersbtid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSERSBTID(String value) {
        this.usersbtid = value;
    }

}
