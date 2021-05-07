package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

/**
 * CCAMailboxTemplate Provides information about mailboxes used in combination with CCA
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	01-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
public abstract class CCAMailboxTemplate extends EmployeeMailboxTemplate {

	private static final String CC = "cc";

	/**
	 * Employee name is used as display name of the mailbox (shown to employee). When used for addressing, employee name
	 * is prefixed with value from this property. This value has to be taken from Tridion and provided to object instance
	 * during construction. Value is frozen (cannot be changed).
	 */
	private String displayNamePrefix;

	/**
	 * Strategy to use when no CCA with given role found. This value has to be taken from Tridion and provided to object
	 * instance during construction. Value is frozen (cannot be changed).
	 */
	private GenesysMailboxTemplate fallbackStrategy;

	/**
	 * Signature template of the employee. Employee name is the only parameter of this template. This value has to be
	 * taken from Tridion and provided to object instance during construction. Value is frozen (cannot be changed).
	 */
	private String signatureTemplate;

	/**
	 * Given CCA code as input, returns the mailbox name. Mailbox name is "cc" + CCA code.
	 * @param ccaCode cca Code
	 * @return String mailbox name
	 */
	public String getMailboxName(String ccaCode) {
		return CCAMailboxTemplate.CC + ccaCode;
	}

	/**
	 * Checks either provided mailbox name is CCA mailbox name. Name must start with "cc".
	 * @param mailboxName mailbox name
	 * @return Boolean boolean for mailbox name is CCA mailbox name or not
	 */
	public Boolean isCCAMailboxName(String mailboxName) {
		return mailboxName.startsWith(CCAMailboxTemplate.CC);

	}

	/**
	 * @return String:Employee name is used as display name of the mailbox
	 */
	public String getDisplayNamePrefix() {
		return displayNamePrefix;
	}

	/**
	 * @param displayNamePrefix String:Employee name is used as display name of the mailbox
	 */
	public void setDisplayNamePrefix(String displayNamePrefix) {
		this.displayNamePrefix = displayNamePrefix;
	}

	/**
	 * @return GenesysMailboxTemplate:Strategy to use when no CCA with given role found.
	 */
	public GenesysMailboxTemplate getFallbackStrategy() {
		return fallbackStrategy;
	}

	/**
	 * @param fallbackStrategy GenesysMailboxTemplate:Strategy to use when no CCA with given role found.
	 */
	public void setFallbackStrategy(GenesysMailboxTemplate fallbackStrategy) {
		this.fallbackStrategy = fallbackStrategy;
	}

	/**
	 * @return String:Signature template of the employee.
	 */
	public String getSignatureTemplate() {
		return signatureTemplate;
	}

	/**
	 * @param signatureTemplate String:Signature template of the employee.
	 */
	public void setSignatureTemplate(String signatureTemplate) {
		this.signatureTemplate = signatureTemplate;
	}
}