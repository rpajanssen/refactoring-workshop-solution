package com.abnamro.nl.channels.geninfo.bankmail.asc.implementation;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.msec.integrationservices.UserId;
import com.abnamro.nl.businesscontactgroup.service.businesscontact.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCLogMessages;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailClientASC;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.InvalidAttributeValueException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxAccessDeniedException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.UserClass;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailUtil;
import com.abnamro.nl.channels.geninfo.bankmail.util.ClientTagValidator;
import com.abnamro.nl.health.exceptions.HealthCheckException;
import com.abnamro.nl.health.interfaces.HealthCheckable;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;

import javax.ejb.EJB;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BankmailClientASCImpl
 * Interface for a strategy class defining construction of employee mailboxes.
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				01-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
@LogInterceptorBinding
public class BankmailClientASCImpl implements BankmailClientASC, HealthCheckable{
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(BankmailClientASCImpl.class);
	
	/**
	 * Bankmail General Util class
	 */
	private static BankmailUtil bankmailUtil = new BankmailUtil();


	@EJB(lookup = "java:global/BS001_BusinessContactService/BS001_EJB/BusinessContactServiceImpl!com.abnamro.nl.businesscontactgroup.service.businesscontact.interfaces.BusinessContactService")
private BusinessContactService businessContactService;

	/**
     * Returns Bankmail address of the current user.
     * @param securityContext SecurityContext
     * @return String Bankmail address
	 * @throws MailboxAccessDeniedException 
     */
	public String getCurrentUserAddress(SecurityContext securityContext) throws MailboxAccessDeniedException {

		final String LOG_METHOD = "getCurrentUserAddress(securityContext):String";
		
		String currentUserAddress = null;

		String userReference = securityContext.getUserReference();

		// Determine the name of the mailbox
		// If bc logs in the actual mailbox name will be  bc<userReference>
		// If at logs in the actual mailbox name will be  at<userReference>
		String userClass=null;
		if (null!= securityContext.getUserClass()) {
			userClass = securityContext.getUserClass();
		}
		if (null != userClass && userClass.equals(UserClass.BC_WITH_PERSONAL_CARD))
		{
			// bc user mailbox name
			currentUserAddress = BankmailConstants.BC_MAILBOX_NAME_PREFIX
					+ userReference;

		} else if (null != userClass && userClass.equals(UserClass.BC_WITH_NONPERSONAL_CARD))
		{
			// at user mailbox name
			currentUserAddress = BankmailConstants.AT_MAILBOX_NAME_PREFIX
					+ userReference;
		} else {

			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_MAILBOX_ACCESS_DENIED_EXCEPTION, userClass, userReference);
			Messages msgs = new Messages();
			msgs
					.addMessage(
							new Message(
									BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION),
							MessageType.getError());

			throw new MailboxAccessDeniedException(msgs);
		}
		return currentUserAddress;
	}


	/**
     * Returns display name for a Bankmail address
     * @param address Bankmail address
     * @return String get display name of address
     * @throws BankmailApplicationException bankmailApplicationException
     */
	public String getDisplayNameOfAddress(String address)
			throws BankmailApplicationException {

		final String LOG_METHOD = "getDisplayNameOfAddress(String):String";

		String displayName = null;
		try {
			String userReference = bankmailUtil.getUserReferenceFromAddress(address);

			if (address.startsWith(BankmailConstants.BC_MAILBOX_NAME_PREFIX)) {

				BusinessContactInputDTO retrieveSpecification = new BusinessContactInputDTO();

				retrieveSpecification.setIndicationRetrieveInterpayName(true);


				BusinessContactExtendedInfoDTO businessContactExtendedInfoDTO =
						businessContactService
								.readBusinessContactExtended(KeyType.getBcNumber(),
										userReference, retrieveSpecification, null, true);
				//Interpay Name
				displayName = businessContactExtendedInfoDTO.getInterpayName();

				

			} else if (address.startsWith(BankmailConstants.AT_MAILBOX_NAME_PREFIX)) {


				List<UserId> userIds = new ArrayList<UserId>();

				UserId userId = new UserId();
				userId.setUserClass("AT");
				userId.setUserReference(userReference);
				userIds.add(userId);

				displayName=null;

			}
			else {
				LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_INVALID_ATTRIBUTE_VALUE_EXCEPTION, address);
				Messages msgs = new Messages();
				msgs
						.addMessage(
								new Message(
										BankmailABPCMessageKeys.ERROR_INVALID_ATTRIBUTE_VALUE_EXCEPTION),
								MessageType.getError());

				throw new InvalidAttributeValueException(msgs);				
			}
			// if no displayName is available
			if (null == displayName) {
				displayName= "UNKNOWN";
			}
			return displayName;

		} catch (BusinessContactServiceException businessContactServiceException) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_BUSINESS_CONTACT_SERVICE_EXCEPTION, businessContactServiceException);
			Messages msgs = new Messages();
			msgs
					.addMessage(
							new Message(
									BankmailABPCMessageKeys.ERROR_BUSINESS_CONTACT_SERVICE_EXCEPTION),
							MessageType.getError());
			throw new BankmailApplicationException(msgs);
		}
		
	}

	public void validateMessageContent(String content) throws BankmailApplicationException {
		
		final String LOG_METHOD = "validateMessageContent(String):void";

		try {
			boolean isValidContentFlag = false;
			
			ClientTagValidator validator = new ClientTagValidator(content);
			isValidContentFlag = validator.isValid();			
			
			if (!isValidContentFlag) {
				LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_CONTENT_VALIDATION_FAILED, content);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(
						BankmailABPCMessageKeys.ERROR_CLIENT_CONTENT_VALIDATION_FAILED),
						MessageType.getError());
				// Throw unexpected exception			
				throw new InvalidAttributeValueException (msgs);
			}
	
		} catch (IOException exception) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_IO_EXCEPTION, exception);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(
					BankmailABPCMessageKeys.ERROR_IO_EXCEPTION),
					MessageType.getError());
			// Throw unexpected exception			
			throw new BankmailApplicationException(msgs);
		}
		
	}

	public boolean isHealthy() throws HealthCheckException {
		return true;
	}
}
