package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import java.util.List;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * MailMessageListDTO 
 * List of MailMessageCustomDTO. 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				01-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public class MailMessageListDTO extends AbstractDTO{
	private static final long serialVersionUID = 1L;
	/**
	 * List of MailMessageCustomDTO
	 */
	private List<MailMessageCustomDTO> mailMessages;

	/**
	 * @return List<MailMessageCustomDTO>
	 */
	public List<MailMessageCustomDTO> getMailMessages() {
		return mailMessages;
	}

	/**
	 * @param mailMessages List<MailMessageCustomDTO>
	 */
	public void setMailMessages(List<MailMessageCustomDTO> mailMessages) {
		this.mailMessages = mailMessages;
	}	

}
