package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * MailboxGroupCustomDTO:
 * Represents a group of mailboxes. If a mailbox is in a group, 
 * then all members of that group have access to the mailbox. 
 * Examples of mailbox groups are BO.
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				01-10-2012	Initial version	  Release 1.0 
 * </PRE>
 * @author 
 * @see
 */
public class MailboxGroupCustomDTO  extends AbstractDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * Name of the group
	 */
	private String name;
	
	/**
	 * The group code
	 */
	private String code;
	
	/**
	 * @return String:Name of the group
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name String:Name of the group
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return String:The group code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code String:The group code
	 */
	public void setCode(String code) {
		this.code = code;
	}
}
