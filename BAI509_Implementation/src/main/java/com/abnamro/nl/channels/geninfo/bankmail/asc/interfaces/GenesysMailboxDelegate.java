package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCLogMessages;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.EmployeeMailboxCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxAccessDeniedException;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GenesysMailboxDelegate Defines mailbox for employees working with Genesys.
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
@LogInterceptorBinding
public class GenesysMailboxDelegate implements EmployeeMailboxDelegate {

	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(GenesysMailboxDelegate.class);

	/**
	 * Mailbox template used to create new instances of EmployeeMailboxCustomDTO. This property is provided during object
	 * construction and cannot be changed (is frozen).
	 */
	private GenesysMailboxTemplate mailboxTemplate;

	/**
	 * Retrieves an employee mailbox.
	 * @param securityContext SecurityContext
	 * @param mailboxName mailbox name
	 * @return EmployeeMailboxCustomDTO Employee-specific information about a mailbox
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public EmployeeMailboxCustomDTO getMailbox(SecurityContext securityContext, String mailboxName)
			throws BankmailApplicationException {
		final String LOG_METHOD = " getMailbox(securityContext):EmployeeMailboxCustomDTO";

		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = null;

		try {
			// if mailboxName != user and != grGenesys throw
			// MailbosAccessDeniedException
			if (!BankmailConstants.DEFAULT_MAILBOX_NAME.equalsIgnoreCase(mailboxName)
					&& !GenesysMailboxTemplate.MAILBOX_NAME.equalsIgnoreCase(mailboxName)) {
				throw new MailboxAccessDeniedException();
			}

			// Set values in employeeMailboxCustomDTO
			employeeMailboxCustomDTO = new EmployeeMailboxCustomDTO();
			employeeMailboxCustomDTO.setName(GenesysMailboxTemplate.MAILBOX_NAME);

			employeeMailboxCustomDTO.setSignature(mailboxTemplate.getSignature());
			employeeMailboxCustomDTO.setDisplayName(mailboxTemplate.getDisplayName());
			employeeMailboxCustomDTO.setFullDisplayName(mailboxTemplate.getDisplayName());

			employeeMailboxCustomDTO.setIsReachCheckNeeded(Boolean.TRUE);
			employeeMailboxCustomDTO.setIsCreateAllowed(Boolean.TRUE);
			employeeMailboxCustomDTO.setIsOverviewAllowed(Boolean.FALSE);
			employeeMailboxCustomDTO.setIsDeleteAllowed(Boolean.FALSE);

		} catch (MailboxAccessDeniedException accessDeniedException) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_MAILBOX_ACCESS_DENIED_EXCEPTION, accessDeniedException, securityContext.getSessionSpecification(), accessDeniedException.getMessage());
			Messages myMessages = new Messages();
			myMessages.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(myMessages);
		}
		return employeeMailboxCustomDTO;
	}

	/**
	 * Returns mailboxes which employee can access.
	 * @param securityContext SecurityContext
	 * @return EmployeeMailboxCustomDTO[] Employee-specific information about a mailbox
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public List<EmployeeMailboxCustomDTO> getMailboxes(SecurityContext securityContext) throws BankmailApplicationException {
		List<EmployeeMailboxCustomDTO> employeeMailboxCustomDTOList = null;
		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = null;

		// create an empty list
		employeeMailboxCustomDTOList = Collections.emptyList();

		// get default mailbox
		employeeMailboxCustomDTO = getMailbox(securityContext, BankmailConstants.DEFAULT_MAILBOX_NAME);

		// add dto to list
		if (null != employeeMailboxCustomDTO) {
			employeeMailboxCustomDTOList = new ArrayList<EmployeeMailboxCustomDTO>();
			employeeMailboxCustomDTOList.add(employeeMailboxCustomDTO);
		}
		return employeeMailboxCustomDTOList;
	}

	/**
	 * @return GenesysMailboxTemplate Defines mailbox for employees working with Genesys.
	 */
	public GenesysMailboxTemplate getMailboxTemplate() {
		return mailboxTemplate;
	}

	/**
	 * @param mailboxTemplate GenesysMailboxTemplate Defines mailbox for employees working with Genesys.
	 */
	public void setMailboxTemplate(GenesysMailboxTemplate mailboxTemplate) {
		this.mailboxTemplate = mailboxTemplate;
	}

}
