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
 * BOMailboxDelegate This type of mailboxes are defined on BO level. All employees of the BO get access to it.
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
public class BOMailboxDelegate implements EmployeeMailboxDelegate {

	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(BOMailboxDelegate.class);

	/**
	 * This type of mailboxes are defined on BO level.
	 */
	private BOMailboxTemplate mailboxTemplate;

	/**
	 * Retrieves an employee mailbox.
	 * @param securityContext securityContext
	 * @param mailboxName mailbox name
	 * @return EmployeeMailboxCustomDTO Employee-specific information about a mailbox
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public EmployeeMailboxCustomDTO getMailbox(SecurityContext securityContext, String mailboxName)
			throws BankmailApplicationException {
		final String LOG_METHOD = "getMailbox(SecurityContext,String):EmployeeMailboxCustomDTO";

		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = null;
		Long sbtBO = 0L;
		try {
			// if mailboxName != user and isBOMailboxMName is false throw
			// MailbosAccessDeniedException
			if (!BankmailConstants.DEFAULT_MAILBOX_NAME.equalsIgnoreCase(mailboxName)
					&& !mailboxTemplate.isBOMailboxName(mailboxName)) {
				throw new MailboxAccessDeniedException();
			}

			// Set values in employeeMailboxCustomDTO
			employeeMailboxCustomDTO = new EmployeeMailboxCustomDTO();

			// Get SBTBO id from securtiyContext
			//check in UT
			sbtBO = Long.parseLong(String.valueOf(securityContext.getAccountNumberCardOwner()));

			employeeMailboxCustomDTO.setName(mailboxTemplate.getMailboxName(sbtBO));

			if (!BankmailConstants.DEFAULT_MAILBOX_NAME.equalsIgnoreCase(mailboxName)
					&& !employeeMailboxCustomDTO.getName().equalsIgnoreCase(mailboxName)) {
				throw new MailboxAccessDeniedException();
			}

			employeeMailboxCustomDTO.setSignature(mailboxTemplate.getSignature());
			employeeMailboxCustomDTO.setDisplayName(mailboxTemplate.getDisplayName());
			employeeMailboxCustomDTO.setFullDisplayName(mailboxTemplate.getDisplayName());

			employeeMailboxCustomDTO.setIsReachCheckNeeded(Boolean.FALSE);
			employeeMailboxCustomDTO.setIsCreateAllowed(Boolean.TRUE);
			employeeMailboxCustomDTO.setIsOverviewAllowed(Boolean.TRUE);
			employeeMailboxCustomDTO.setIsDeleteAllowed(Boolean.TRUE);

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
	 * @param securityContext securityContext
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
	 * Gets the mailbox template
	 * @return BOMailboxTemplate This type of mailboxes are defined on BO level.
	 */
	public BOMailboxTemplate getMailboxTemplate() {
		return mailboxTemplate;
	}

	/**
	 * @param mailboxTemplate BOMailboxTemplate This type of mailboxes are defined on BO level.
	 */
	public void setMailboxTemplate(BOMailboxTemplate mailboxTemplate) {
		this.mailboxTemplate = mailboxTemplate;
	}

}
