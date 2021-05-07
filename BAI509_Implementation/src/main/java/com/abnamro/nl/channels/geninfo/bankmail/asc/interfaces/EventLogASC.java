package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailMessageCustomDTO;

/**
 * EventLogASC logs send message, open message & delete message events
 * 
 * <PRE>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	07-11-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
public interface EventLogASC {

	/**
	 * Logs send message event.
	 * @param securityContext SecurityContext
	 * @param message MailMessageCustomDTO
	 * @throws BankmailApplicationException application exception
	 */
	public void logSendMessage(SecurityContext securityContext, MailMessageCustomDTO message) throws BankmailApplicationException;

	/**
	 * Logs open mailbox message event.
	 * @param securityContext SecurityContext
	 * @param mailboxName mailbox name String
	 * @param messageId message id String
	 * @throws BankmailApplicationException application exception
	 */
	public void logOpenMessage(SecurityContext securityContext, String mailboxName, String messageId)
			throws BankmailApplicationException;

	/**
	 * Logs delete mailbox message event.
	 * @param securityContext SecurityContext
	 * @param mailboxName String
	 * @param messageId String
	 * @throws BankmailApplicationException application exception
	 */
	public void logDeleteMessage(SecurityContext securityContext, String mailboxName, String messageId)
			throws BankmailApplicationException;

}
