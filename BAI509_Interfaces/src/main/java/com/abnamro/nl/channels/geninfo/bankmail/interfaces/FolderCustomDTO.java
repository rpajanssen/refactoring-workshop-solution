package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * FolderCustomDTO 
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public class FolderCustomDTO extends AbstractDTO{
	private static final long serialVersionUID = 1L;
	/**
	 * Name of the folder.
	 */
	private String name;
	
	
	/**
	 * Number of unseen messages in the folder
	 */
	private Integer unseenMessagesCount;
	
	/**
	 * Number of unseen alert messages in the folder
	 */
	private Integer unseenAlertMessagesCount;
	
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return unseenMessagesCount
	 */
	public Integer getUnseenMessagesCount() {
		return unseenMessagesCount;
	}
	
	/**
	 * @param unseenMessagesCount Integer
	 */
	public void setUnseenMessagesCount(Integer unseenMessagesCount) {
		this.unseenMessagesCount = unseenMessagesCount;
	}
	
	/**
	 * @return unseenAlertMessagesCount
	 */
	public Integer getUnseenAlertMessagesCount() {
		return unseenAlertMessagesCount;
	}
	
	/**
	 * @param unseenAlertMessagesCount Integer
	 */
	public void setUnseenAlertMessagesCount(Integer unseenAlertMessagesCount) {
		this.unseenAlertMessagesCount = unseenAlertMessagesCount;
	}

}
