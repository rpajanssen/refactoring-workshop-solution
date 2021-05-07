package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.SendBankmailPolicyDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.SendBankmailPolicyListDTO;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * SendBankmailPolicyASC 
 * Bankmail 'Application Specific Controller' handles all business logic 
 * to related to SendBankmailPolicy.
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-08-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public interface SendBankmailPolicyASC {

	/**
	 * Retrieves SendBankmailPolicy for a customer. If applyRolloutFilter is false, no filtering is applied.
	 * If no messages can be sent concerning given customer, null is returned. 
	 * This method always returns null if employee is logged in.
	 *  
	 * @param securityContext
	 *            SecurityContext
	 * @param bcNumber
	 *            String
	 * @param applyRolloutFilter boolean                 
	 * @return sendBankmailPolicyDTO SendBankmailPolicyDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public SendBankmailPolicyDTO getSendBankmailPolicy(SecurityContext securityContext,
			String bcNumber, boolean applyRolloutFilter ) throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Description Retrieves a list of SendBankmailPolicy for customers which
	 * can be selected by logged in user in the attribute "concerningCustomer"
	 * in new messages.
	 * 
	 * List is always empty for employees. List can be empty for Internet
	 * Banking clients, in which case they are not allowed to create/send new
	 * messages.
	 * 
	 * Actions CREATE_MESSAGE, SEND_MESSAGE will be disallowed in this case (see
	 * getMailbox, getMailboxes, MailboxActionName).
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @return sendBankmailPolicyListDTO SendBankmailPolicyListDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public SendBankmailPolicyListDTO getSendBankmailPolicies(
			SecurityContext securityContext) throws BankmailApplicationException, IOException, SAXException;
	
	/**
	 * Retrieves SendBankmailPolicy for a customer. If applyRolloutFilter is false, no filtering is applied.
	 * Null is returned if no messages can be sent concerning given customer.  
	 * This method doesn't contain any security checks.
	 * 
	 * @param bcNumber String 
	 * @param applyRolloutFilter boolean 
	 * @return SendBankmailPolicyDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public SendBankmailPolicyDTO retrieveSendBankmailPolicy(String bcNumber, boolean applyRolloutFilter)
			throws BankmailApplicationException, IOException, SAXException;

}
