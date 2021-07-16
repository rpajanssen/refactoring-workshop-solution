package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.abnamro.nl.dto.util.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * Mailbox Stores message and other parameters for a mail
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Mailbox extends AbstractDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Unique mailbox name. Client mailboxes have names in form "bc{bcNumber}". Employee mailboxes have names in form
     * "em{employeeId}".
     */
    private String name;

    /**
     * Human-readable mailbox name. Contains the name of the mailbox owner.
     */
    private String displayName;

    /**
     * Indicator whether the Mailbox owner can select the subject of a new mailbox message.
     */
    private Boolean isSubjectSelectable;

    /**
     * Signature which will be used with every new mailbox message in this mailbox. A signature is only applicable for
     * bank employees. This signature will be a part of the MailboxMessage.content.
     */
    private String signature;

    /**
     * Group this Mailbox belongs to.
     */
    private MailboxGroup group;

    /**
     * @return String:mailbox name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name String:set mailbox name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Human-readable mailbox name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName String:set Human-readable mailbox name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return Boolean:Indicator whether the Mailbox owner can select the subject of a new mailbox message
     */
    public Boolean getIsSubjectSelectable() {
        return isSubjectSelectable;
    }

    /**
     * @param isSubjectSelectable Boolean:Indicator whether the Mailbox owner can select the subject of a new mailbox
     *        message
     */
    public void setIsSubjectSelectable(Boolean isSubjectSelectable) {
        this.isSubjectSelectable = isSubjectSelectable;
    }

    /**
     * @return String:Signature which will be used with every new mailbox message in this mailbox.
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature String:Signature which will be used with every new mailbox message in this mailbox.
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * @return MailboxGroup:Group this Mailbox belongs to.
     */
    public MailboxGroup getGroup() {
        return group;
    }

    /**
     * @param group MailboxGroup:Group this Mailbox belongs to.
     */
    public void setGroup(MailboxGroup group) {
        this.group = group;
    }

}
