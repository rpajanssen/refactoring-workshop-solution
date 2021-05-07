package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.EmployeeMailboxCustomDTO;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;
/**
 * BankmailEmployeeASC Interface for declaration of different methods to get client reach, end functions,
 * authorization, mailboxes and other details.
 *
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				01-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
public interface BankmailEmployeeASC {

	/**
	 * Checks if employee can reach certain client. End function 54 (task 118, "BCDB Raadplegen") 
	 * is used for the check.
	 * 
	 * If there is no reach then NoClientReachException is thrown.
	 * 
	 * @param securityContext SecurityContext
	 * @param bcNumber Long
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public void checkClientReach(SecurityContext securityContext, Long bcNumber)
			throws BankmailApplicationException;
	
	/**
	 * Checks if employee can reach certain client. End function 54 (task 118, "BCDB Raadplegen") 
	 * is used for the check.
	 * 
	 * If there is no reach then NoClientReachException is thrown.
	 * 
	 * @param securityContext SecurityContext
	 * @param bcNumber Long
	 * @return boolean
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public boolean authorizeClientReach(SecurityContext securityContext, Long bcNumber)
			throws BankmailApplicationException;

	/**
	 * Returns mailboxes which employee can access.
	 * @param securityContext SecurityContext
	 * @return employeeMailboxCustomDTOlist List<EmployeeMailboxCustomDTO>
	 * @throws BankmailApplicationException BankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public List<EmployeeMailboxCustomDTO> getMailboxes(SecurityContext securityContext)
			throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Retrieves an employee mailbox.
	 * @param securityContext SecurityContext
	 * @param mailboxName String
	 * @return employeeMailboxCustomDTO EmployeeMailboxCustomDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public EmployeeMailboxCustomDTO getMailbox(SecurityContext securityContext,
			String mailboxName) throws BankmailApplicationException, IOException, SAXException;

	/**
	 * Validates message content provided by the employee.	 * 
	 * Message must be valid HTML with <p>, <br> and <a> tags only, 
	 * otherwise InvalidAttributeValueException is thrown. 
	 * Only links to abnamro.nl domain are allowed.
	 * 
	 * @param content String 
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public void validateMessageContent(String content)
			throws BankmailApplicationException;

}
