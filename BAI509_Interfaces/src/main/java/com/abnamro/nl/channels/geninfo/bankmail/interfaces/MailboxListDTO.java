package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import java.util.List;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * MailboxListDTO 
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
public class MailboxListDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * List of mailboxes.
	 */
	private List<MailboxCustomDTO> mailboxes;

	/**
	 * @return mailboxes List<MailboxCustomDTO>
	 */
	public List<MailboxCustomDTO> getMailboxes() {
		return mailboxes;
	}

	/**
	 * @param mailboxes List<MailboxCustomDTO>
	 */
	public void setMailboxes(List<MailboxCustomDTO> mailboxes) {
		this.mailboxes = mailboxes;
	}

}
