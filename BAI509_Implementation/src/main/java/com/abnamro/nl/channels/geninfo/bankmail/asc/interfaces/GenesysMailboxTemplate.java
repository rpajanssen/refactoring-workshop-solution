package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

/**
 * GenesysMailboxTemplate Provides information about employee mailbox used for messages routed through Genesys.
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
public class GenesysMailboxTemplate extends EmployeeMailboxTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Mailbox name. Is the same for all employees using Genesys.
	 */
	public static final String MAILBOX_NAME = "grGenesys";
	
	public static final String MAILBOX_NAME_GR_OPS = "grOPS";

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
	 * @return String:Signature of the employee
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature String:Signature of the employee
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
}
