//
// Generated By:JAX-WS RI IBM 2.0_03-07/07/2008 01:00 PM(foreman)-fcs (JAXB RI IBM 2.0.5-09/06/2010 03:32 PM(foreman)-fcs)
//


package com.siebel.xml.aabr_20bankmail_20calendar_20io_ext;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.siebel.xml.aabr_20bankmail_20calendar_20io_ext package. 
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

    private final static QName _ListOfAabrBankmailCalendarIoExt_QNAME = new QName("http://www.siebel.com/xml/AABR%20Bankmail%20Calendar%20IO_Ext", "ListOfAabrBankmailCalendarIo_Ext");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.siebel.xml.aabr_20bankmail_20calendar_20io_ext
     * 
     */
    public ObjectFactory() {
    	//empty constructor
    }

    /**
     * Create an instance of {@link Users }
     * @return Users users
     */
    public Users createUsers() {
        return new Users();
    }

    /**
     * Create an instance of {@link UserAccess }
     * @return UserAccess userAccess
     */
    public UserAccess createUserAccess() {
        return new UserAccess();
    }

    /**
     * Create an instance of {@link ListOfAabrBankmailCalendarIoExtTopElmt }
     * @return ListOfAabrBankmailCalendarIoExtTopElmt ListOfAabrBankmailCalendarIoExtTopElmt
     */
    public ListOfAabrBankmailCalendarIoExtTopElmt createListOfAabrBankmailCalendarIoExtTopElmt() {
        return new ListOfAabrBankmailCalendarIoExtTopElmt();
    }

    /**
     * Create an instance of {@link ListOfAabrBankmailCalendarIoExt }
     * @return ListOfAabrBankmailCalendarIoExt ListOfAabrBankmailCalendarIoExt
     */
    public ListOfAabrBankmailCalendarIoExt createListOfAabrBankmailCalendarIoExt() {
        return new ListOfAabrBankmailCalendarIoExt();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListOfAabrBankmailCalendarIoExt }{@code >}}
     * @return JAXBElement<ListOfAabrBankmailCalendarIoExt> JAXBElement<ListOfAabrBankmailCalendarIoExt>
     */
    @XmlElementDecl(namespace = "http://www.siebel.com/xml/AABR%20Bankmail%20Calendar%20IO_Ext", name = "ListOfAabrBankmailCalendarIo_Ext")
    public JAXBElement<ListOfAabrBankmailCalendarIoExt> createListOfAabrBankmailCalendarIoExt(ListOfAabrBankmailCalendarIoExt value) {
        return new JAXBElement<ListOfAabrBankmailCalendarIoExt>(_ListOfAabrBankmailCalendarIoExt_QNAME, ListOfAabrBankmailCalendarIoExt.class, null, value);
    }

}
