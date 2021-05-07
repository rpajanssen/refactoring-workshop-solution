package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

/**
 * BOMailboxTemplate This type of mailboxes are defined on BO level. All employees of the BO get access to it. Access
 * logic is not part of this class. Other requirements (certain MSec task, for example) may apply to provide access to
 * the mailbox.
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				01-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
public class BOMailboxTemplate extends EmployeeMailboxTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private static final String BO = "bo";
	/**
	 * Display name of the mailbox. This value has to be taken from Tridion and provided to object instance during
	 * construction. Value is frozen (cannot be changed).
	 */
	private String displayName;

	/**
	 * Signature of the employee. Automatically added by front-end to all new messages. This value has to be taken from
	 * Tridion and provided to object instance during construction. Value is frozen (cannot be changed).
	 */
	private String signature;

	/**
	 * Strategy to use when no BO is found. This value has to be taken from Tridion and provided to object instance
	 * during construction. Value is frozen (cannot be changed). Frozen
	 */
	private GenesysMailboxTemplate fallbackStrategy;

	/**
	 * Description Gets name of the mailbox
	 * @param boNumber Long bc no.
	 * @return Mailbox name String
	 */
	public String getMailboxName(Long boNumber) {
		return BOMailboxTemplate.BO + String.valueOf(boNumber.longValue()) + "";
	}

	/**
	 * Checks either provided mailbox name is BO mailbox name. Name must start with "bo".
	 * @param mailboxName mailbox Name
	 * @return Boolean true|false
	 */
	public Boolean isBOMailboxName(String mailboxName) {
		return mailboxName.startsWith(BOMailboxTemplate.BO);
	}

	/**
	 * @return String:Display name of the mailbox.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName String:Display name of the mailbox.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return String:Signature of the employee.
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature String:Signature of the employee.
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return fallbackStrategy GenesysMailboxTemplate:fallbackStrategy of the employee.
	 */
	public GenesysMailboxTemplate getFallbackStrategy() {
		return fallbackStrategy;
	}

	/**
	 * @param fallbackStrategy GenesysMailboxTemplate:fallbackStrategy of the employee.
	 */
	public void setFallbackStrategy(GenesysMailboxTemplate fallbackStrategy) {
		this.fallbackStrategy = fallbackStrategy;
	}

}
