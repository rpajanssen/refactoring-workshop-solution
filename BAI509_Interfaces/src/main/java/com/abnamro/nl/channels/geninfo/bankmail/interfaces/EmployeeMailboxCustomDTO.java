package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

/**
 * EmployeeMailboxCustomDTO:
 * Employee-specific information about a mailbox. Is not exposed to the REST layer.
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	01-10-2012	Initial version	  Release 1.0 
 * </PRE>
 * @author 
 * @see
 */
public class EmployeeMailboxCustomDTO extends MailboxCustomDTO{
	private static final long serialVersionUID = 1L;
	/**
	 * Reach check has to be performed when 
	 * accessing messages in the mailbox.
	 */
	private Boolean isReachCheckNeeded;
	
	/**
	 * Employee is allowed to create new 
	 * messages in this mailbox.
	 */
	private Boolean isCreateAllowed;
	
	/**
	 * Overview of messages in this mailbox 
	 * can be retrieved.
	 */
	private Boolean isOverviewAllowed;
	
	/**
	 * Employee is allowed to delete messages 
	 * from this mailbox
	 */
	private Boolean isDeleteAllowed;
		
	/**
	 * Display name used for new messages 
	 * (in "From" attribute of the message).
	 */
	private String fullDisplayName;

	/**
	 * @return Boolean:Reach check has to be performed when 
	 * accessing messages in the mailbox
	 */
	public Boolean getIsReachCheckNeeded() {
		return isReachCheckNeeded;
	}

	/**
	 * @param isReachCheckNeeded Boolean:Reach check has to be performed when 
	 * accessing messages in the mailbox
	 */
	public void setIsReachCheckNeeded(Boolean isReachCheckNeeded) {
		this.isReachCheckNeeded = isReachCheckNeeded;
	}

	/**
	 * @return Boolean:Employee is allowed to create new 
	 * messages in this mailbox.
	 */
	public Boolean getIsCreateAllowed() {
		return isCreateAllowed;
	}

	/**
	 * @param isCreateAllowed Boolean:Employee is allowed to create new 
	 * messages in this mailbox.
	 */
	public void setIsCreateAllowed(Boolean isCreateAllowed) {
		this.isCreateAllowed = isCreateAllowed;
	}

	/**
	 * @return Boolean: Overview of messages in this mailbox 
	 * can be retrieved
	 */
	public Boolean getIsOverviewAllowed() {
		return isOverviewAllowed;
	}

	/**
	 * @param isOverviewAllowed Boolean: Overview of messages in this mailbox 
	 * can be retrieved
	 */
	public void setIsOverviewAllowed(Boolean isOverviewAllowed) {
		this.isOverviewAllowed = isOverviewAllowed;
	}

	/**
	 * @return Boolean: Employee is allowed to delete messages 
	 * from this mailbox
	 */
	public Boolean getIsDeleteAllowed() {
		return isDeleteAllowed;
	}

	/**
	 * @param isDeleteAllowed Boolean: Employee is allowed to delete messages 
	 * from this mailbox
	 */
	public void setIsDeleteAllowed(Boolean isDeleteAllowed) {
		this.isDeleteAllowed = isDeleteAllowed;
	}

	/**
	 * @return String:Display name used for new messages 
	 * (in "From" attribute of the message).
	 */
	public String getFullDisplayName() {
		return fullDisplayName;
	}

	/**
	 * @param fullDisplayName String:Display name used for new messages 
	 * (in "From" attribute of the message).
	 */
	public void setFullDisplayName(String fullDisplayName) {
		this.fullDisplayName = fullDisplayName;
	}
}
