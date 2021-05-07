package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * SendBankmailPolicy Stores policy for a sending bankmail
 *
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 *
 * @author
 * @see
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SendBankmailPolicy extends Policy {
    private static final long serialVersionUID = 1L;
    /**
     * Message key in Tridion which contains the SLA information for the bankmail customer.
     */
    private String slaMessageKey;

    /**
     * True when the bankmail subject is selectable.
     */
    private Boolean isSubjectSelectable;

    /**
     * Destination mail contact details. In other words, the details of the mail contact to which a MailMessage will be
     * sent to.
     */
    private MailContact destination;

    /**
     * @return String slaMessageKey
     */
    public String getSlaMessageKey() {
        return slaMessageKey;
    }

    /**
     * @param slaMessageKey String
     */
    public void setSlaMessageKey(String slaMessageKey) {
        this.slaMessageKey = slaMessageKey;
    }

    /**
     * @return Boolean isSubjectSelectable true|false
     */
    public Boolean getIsSubjectSelectable() {
        return isSubjectSelectable;
    }

    /**
     * @param isSubjectSelectable Boolean:true|false
     */
    public void setIsSubjectSelectable(Boolean isSubjectSelectable) {
        this.isSubjectSelectable = isSubjectSelectable;
    }

    /**
     * @return MailContact destination
     */
    public MailContact getDestination() {
        return destination;
    }

    /**
     * @param destination MailContact
     */
    public void setDestination(MailContact destination) {
        this.destination = destination;
    }
}
