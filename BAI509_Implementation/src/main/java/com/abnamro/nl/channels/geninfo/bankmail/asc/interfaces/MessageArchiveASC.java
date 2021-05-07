package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.MessageDTO;

/**
 * MessageArchiveASC Message archiver.
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	03-12-2013	Initial version	  Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
public interface MessageArchiveASC {
	/**
	 *  Archives given input message.
	 *  
	 * @param securityContext logged in user information
	 * @param messageDTO MailMessageDTO which holds message information
	 * @throws BankmailApplicationException  bankmailApplicationException
	 */
	public void archiveMessage(SecurityContext securityContext, MessageDTO messageDTO)
			throws BankmailApplicationException;

}
