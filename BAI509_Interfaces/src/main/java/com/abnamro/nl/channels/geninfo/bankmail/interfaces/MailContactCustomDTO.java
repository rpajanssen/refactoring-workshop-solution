package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * MailContactCustomDTO 
 * 
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
public class MailContactCustomDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * BankMail+ address. Address of bank client consists of prefix 
	 * "bc" followed by BC number of the client. Address of employee 
	 * has prefix "em" followed by login name of employee. Address 
	 * of employee is not exposed to IB clients. If address is not a 
	 * person (ASC, marketing etc) than address "bank" is used.
	 */
	private String address;
	
	/**
	 * display name of the contact
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
	 * @return String:display name of the contact
	 */
	public String getDisplayName() {
		return displayName;
	}


	/**
	 * @param displayName String:display name of the contact
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
