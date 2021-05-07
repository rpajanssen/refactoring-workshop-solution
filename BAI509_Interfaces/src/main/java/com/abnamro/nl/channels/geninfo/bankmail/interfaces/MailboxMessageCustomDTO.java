package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import java.util.Date;

import com.abnamro.nl.enumeration.MailboxFolderName;

/**
 * MailboxMessageCustomDTO 
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

public class MailboxMessageCustomDTO extends MailMessageCustomDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * Folder where this message is placed within a mailbox.
	 *  Only folder name is used.
	 */
	private FolderCustomDTO folder;
	/**
	 * Boolean Flag if message is seen.
	 */
	private Boolean isSeen;
	
	/**
	 * Boolean Flag if message is answered.
	 */
	private Boolean isAnswered;
		
	/**
	 * Date/time when message was received.
	 */
	private Date receivedDate;
	
	/**
	 * When message is moved to "Deleted" folder.
	 */
	private Date deleteAfterDate;
	
	/**
	 * Folder name where the message was placed before it was deleted
	 */
	private MailboxFolderName deletedFromFolder;
		
	/**
	 * @return FolderCustomDTO:Folder where this message 
	 * is placed within a mailbox
	 */
	public FolderCustomDTO getFolder() {
		return folder;
	}

	/**
	 * @param folder FolderCustomDTO:Folder where this 
	 * message is placed within a mailbox
	 */
	public void setFolder(FolderCustomDTO folder) {
		this.folder = folder;
	}
	
	
	/**
	 * @return Boolean: Flag if message is seen.
	 */
	public Boolean getIsSeen() {
		return isSeen;
	}

	/**
	 * @param isSeen Boolean: Flag if message is seen.
	 */
	public void setIsSeen(Boolean isSeen) {
		this.isSeen = isSeen;
	}
	
	
	/**
	 * @return Boolean: Flag if message is answered.
	 */
	public Boolean getIsAnswered() {
		return isAnswered;
	}

	/** 
	 * @param isAnswered Boolean: Flag if message is answered.
	 */
	public void setIsAnswered(Boolean isAnswered) {
		this.isAnswered = isAnswered;
	}

	/**
	 * @return receivedDate Date
	 */
	public Date getReceivedDate() {
		return receivedDate;
	}
	
	/**
	 * @param receivedDate Date
	 */
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	/**
	 * @return deleteAfterDate Date
	 */
	public Date getDeleteAfterDate() {
		return deleteAfterDate;
	}
	
	/**
	 * @param deleteAfterDate Date
	 */
	public void setDeleteAfterDate(Date deleteAfterDate) {
		this.deleteAfterDate = deleteAfterDate;
	}
	
	/**
	 * @return deletedFromFolder MailboxFolderName
	 */
	public MailboxFolderName getDeletedFromFolder() {
		return deletedFromFolder;
	}
	
	/**
	 * @param deletedFromFolder MailboxFolderName
	 */
	public void setDeletedFromFolder(MailboxFolderName deletedFromFolder) {
		this.deletedFromFolder = deletedFromFolder;
	}
		
}
