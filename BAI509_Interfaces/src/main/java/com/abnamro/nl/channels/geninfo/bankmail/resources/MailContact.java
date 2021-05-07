package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.abnamro.nl.dto.util.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * MailContact: Represents a contact that can be addressed within BankMail+.
 *
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 *
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MailContact extends AbstractDTO {
    private static final long serialVersionUID = 1L;
    /**
     * BankMail+ address. This is the unique identifier of the resource. Following conventions are used to determine the
     * address: 1. Address of a customer consists of prefix "bc" followed by BC number of the client. 2. Address of a bank
     * employee has prefix "em" followed by login name of employee. Address of employee is not exposed to IB clients.
     * Conventions for other BankMail+ addresses (groups, applications) will be defined later.
     *
     */
    private String address;

    /**
     * Display name of the contact
     */
    private String displayName;

    /**
     * @return String:BankMail+ address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address String:BankMail+ address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return String:Display name of the contact
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName String:Display name of the contact
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
