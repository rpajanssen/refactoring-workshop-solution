package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import java.util.List;

/**
 * MailContactListDTO 
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
public class MailContactListDTO {

	/**
	 * List of MailContactCustomDTO 
	 */
	private List<MailContactCustomDTO> mailContactCustomDTOs;
	

	/**
	 * @return List<MailContactCustomDTO> list of MailContactCustomDTO
	 */
	public List<MailContactCustomDTO> getMailContactCustomDTOs() {
		return mailContactCustomDTOs;
	}

	/**
	 * @param mailContactCustomDTOs List<MailContactCustomDTO>
	 */
	public void setMailContactCustomDTOs(
			List<MailContactCustomDTO> mailContactCustomDTOs) {
		this.mailContactCustomDTOs = mailContactCustomDTOs;
	}	
}
