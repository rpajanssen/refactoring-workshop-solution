package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import java.util.List;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * MailboxMessageListDTO
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
public class MailboxMessageListDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * List of MailboxMessageCustomDTO
	 */
	private List<MailboxMessageCustomDTO> mailMessages;

	/**
	 * @return MailboxMessageCustomDTO[] List of MailboxMessageCustomDTO
	 */
	/**
	 * @return List<MailboxMessageCustomDTO> List of MailboxMessageCustomDTO
	 */
	public List<MailboxMessageCustomDTO> getMailMessages() {
		return mailMessages;
	}

	/**
	 * @param mailMessages
	 *            List<MailboxMessageCustomDTO>
	 */
	public void setMailMessages(List<MailboxMessageCustomDTO> mailMessages) {
		this.mailMessages = mailMessages;
	}

}
