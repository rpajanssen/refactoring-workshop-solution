package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * MailboxCustomDTO 
 * Represents a mailbox of BankMail+ user.
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * TCS				01-10-2012	Initial version	  Mailbox Resource_2.5
 * </PRE>
 * @author 
 * @see
 */
public class MailboxCustomDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * Unique mailbox name. Client mailboxes have names in form "bc{bcNumber}". 
	 * Employee mailboxes have names in form "em{employeeId}". 
	 * Other mailbox names can be introduced for groups of 
	 * employees and external systems.
	 */
	private String name;
	
	/**
	 * Human-readable mailbox name. Contains the name of the mailbox owner.
	 */
	private String displayName;
	
	/**
	 * Signature which will be used with every new mailbox 
	 * message in this mailbox. A signature is only applicable 
	 * for bank employees. This signature will be a part of the 
	 * MailboxMessage.content.
	 */
	private String signature;
	
	/**
	 * Indicator whether the Mailbox owner can select 
	 * the subject of a new mailbox message.
	 */
	private Boolean isSubjectSelectable;
	
	/**
	 * Group to which this mailbox belongs to.
	 */
	private MailboxGroupCustomDTO group;
	
	/**
	 * @return name String:Unique mailbox name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name String:Unique mailbox name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return displayName String  Human-readable mailbox name.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * @param displayName String  Human-readable mailbox name.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return String Signature which will be used with every new mailbox 
	 * message in this mailbox.
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature String Signature which will be used with every new mailbox 
	 * message in this mailbox.
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return Boolean:Indicator whether the Mailbox owner can select 
	 * the subject of a new mailbox message.
	 */
	public Boolean getIsSubjectSelectable() {
		return isSubjectSelectable;
	}

	/**
	 * @param isSubjectSelectable Boolean:Indicator whether the Mailbox owner can select 
	 * the subject of a new mailbox message.
	 */
	public void setIsSubjectSelectable(Boolean isSubjectSelectable) {
		this.isSubjectSelectable = isSubjectSelectable;
	}

	/**
	 * @return MailboxGroupCustomDTO:Group to which this mailbox belongs to.
	 */
	public MailboxGroupCustomDTO getGroup() {
		return group;
	}

	/**
	 * @param group MailboxGroupCustomDTO:Group to which this mailbox belongs to.
	 */
	public void setGroup(MailboxGroupCustomDTO group) {
		this.group = group;
	}

}
