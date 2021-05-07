package com.abnamro.nl.channels.geninfo.bankmail.asc.implementation;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.msec.integrationservices.*;
import com.abnamro.msec.integrationservices.services.SecurityAgentFactory;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCLogMessages;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.util.EmployeeTagValidator;
import com.abnamro.nl.channels.jndilookup.JndiLookup;
import com.abnamro.nl.health.exceptions.HealthCheckException;
import com.abnamro.nl.health.interfaces.HealthCheckable;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import com.abnamro.nl.security.util.SecurityAgentUtils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

/**
 * BankmailEmployeeASCImpl
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
public class BankmailEmployeeASCImpl implements BankmailEmployeeASC, HealthCheckable {

	/** Msec end functions */
	public static final String USER_REACH_END_FUNCTION = new JndiLookup(BankmailConstants.END_FUNCTION_PREFIX)
		.getString("EmployeeClientReach", BankmailConstants.MSEC_CLIENT_REACH_END_FUNCTION_DEFAULT);

	public static final String MSEC_ENF_BANKMAIL_PREF_BANKING = new JndiLookup(BankmailConstants.END_FUNCTION_PREFIX)
		.getString("PreferredBankerEndFunction", BankmailConstants.MSEC_ENF_BANKMAIL_PREF_BANKING_DEFAULT);

	public static final String MSEC_ENF_BANKMAIL_PRIVATE_BANKING = new JndiLookup(BankmailConstants.END_FUNCTION_PREFIX)
		.getString("PrivateBankerEndFunction", BankmailConstants.MSEC_ENF_BANKMAIL_PRIVATE_BANKING_DEFAULT);

	public static final String MSEC_ENF_BANKMAIL_ASC_YBB = new JndiLookup(BankmailConstants.END_FUNCTION_PREFIX)
		.getString("AscYbbEndFunction", BankmailConstants.MSEC_ENF_BANKMAIL_ASC_YBB_DEFAULT);

	public static final String MSEC_ENF_BANKMAIL_ASC_RETAIL = new JndiLookup(BankmailConstants.END_FUNCTION_PREFIX)
		.getString("AscRetailEndFunction", BankmailConstants.MSEC_ENF_BANKMAIL_ASC_RETAIL_DEFAULT);

	public static final String MSEC_ENF_BANKMAIL_CLIENT_SUPPORT = new JndiLookup(BankmailConstants.END_FUNCTION_PREFIX)
		.getString("ClientSupportEndFunction", BankmailConstants.MSEC_ENF_BANKMAIL_CLIENT_SUPPORT_DEFAULT);
	
	public static final String MSEC_ENF_OPERATIONS = new JndiLookup(BankmailConstants.END_FUNCTION_PREFIX)
		.getString("OperationsSupportEndFunction", BankmailConstants.MSEC_ENF_OPERATIONS_SUPPORT_DEFAULT);

	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(BankmailEmployeeASCImpl.class);

	/**
	 * Checks if employee can reach certain client. End function 54 (task 118, "BCDB Raadplegen") is used for the check.
	 * If there is no reach then NoClientReachException is thrown.
	 * @param securityContext
	 * @param bcNumber Long
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public void checkClientReach(SecurityContext securityContext , Long bcNumber) throws BankmailApplicationException {
		final String logMethod = "checkClientReach(securityContext ) :void";
		
		try {
			// if returned objct is not userAuthReachResult.SUCCESS, throw NoClientReachException.
			if (! authorizeClientReach(securityContext ,bcNumber)) {
				throw new NoClientReachException();
			}
			
		} catch (NoClientReachException noClientReachException) {
			LOGGER.error(logMethod, BankmailABPCLogMessages.LOG_ASC_EMPLOYEE_BO_CLIENT_REACH_EXCEPTION, noClientReachException, securityContext .getSessionSpecification(), noClientReachException.getMessage());
			Messages myMessages = new Messages();
			myMessages.addMessage(new Message(BankmailABPCMessageKeys.ERROR_EMPLOYEE_NO_CLIENT_REACH_EXCEPTION),
				MessageType.getError());
			throw new BankmailApplicationException(myMessages);
		}
	}
	
	/**
	 * Checks if employee can reach certain client. End function 54 (task 118, "BCDB Raadplegen") is used for the check.
	 * If there is no reach then NoClientReachException is thrown.
	 * @param securityContext securityContext
	 * @param bcNumber Long
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public boolean authorizeClientReach(SecurityContext securityContext , Long bcNumber) throws BankmailApplicationException {
		final String logMethod = "authorizeClientReach(securityContext ,Long) :boolean";
		boolean isAuthorized = false;
		SecurityAgent securityAgent = null;
		String smSSS = null;
		try {
			securityAgent = SecurityAgentFactory.getSecurityAgent();
			smSSS = securityContext .getSessionSpecification();

			// get endFunction from jndi
			String employeeUserReachEndFunction = USER_REACH_END_FUNCTION;

			String[] endFunctions = new String[] { employeeUserReachEndFunction };
			ReachSubject reachSubject = new ReachSubject(ReachSubject.REACH_SUBJECT_CLASS_BC, bcNumber.toString());
			LOGGER.debugHardCodedMessage(logMethod, "Client reach details :: bcNumber {0} :: SubjectClass :: {1} :: subjectReference {2}", bcNumber, reachSubject.getSubjectClass(), reachSubject.getSubjectReference());
			UserAuthReachResult userAuthReachResult = SecurityAgentUtils.authoriseUserReach(securityAgent,
				BankmailConstants.MSEC_APP_NAME, smSSS, endFunctions, reachSubject);
			
			// if returned objct is not userAuthReachResult.SUCCESS, throw NoClientReachException.
			if (null != userAuthReachResult && (SecurityAgentResult.SUCCESS == userAuthReachResult.getResultCode())) {
				isAuthorized = true;
			} else if (userAuthReachResult != null) {
				LOGGER.debugHardCodedMessage(logMethod, "UserAuthReachResult result code : {0}", userAuthReachResult.getResultCode());
			}
		}catch (MSecException msecException) {
			LOGGER.error(logMethod, BankmailABPCLogMessages.LOG_ASC_EMPLOYEE_MSEC_EXCEPTION, msecException, securityContext .getSessionSpecification(), msecException.getErrorCode());
			Messages myMessages = new Messages();
			myMessages.addMessage(new Message(SecurityAgentUtils.getMessageKey(MSecException.class, msecException
				.getErrorCode())), MessageType.getError());
			throw new BankmailApplicationException(myMessages);
		}
		
		return isAuthorized ;
	}

	/**
	 * Retrieves an employee mailbox.
	 * @param securityContext securityContext
	 * @param mailboxName String
	 * @return employeeMailboxCustomDTO EmployeeMailboxCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public EmployeeMailboxCustomDTO getMailbox(SecurityContext securityContext , String mailboxName)
			throws BankmailApplicationException, IOException, SAXException {

		final String logMethod = "getMailbox(securityContext ):EmployeeMailboxCustomDTO";
		try {
			if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_ASC_RETAIL)) {
				return new ASCMailboxDelegate().getMailbox(securityContext , mailboxName);
			} else if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_ASC_YBB)) {
				return new YBBMailboxDelegate().getMailbox(securityContext , mailboxName);
			} else if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_CLIENT_SUPPORT)) {
				return new CSUMailboxDelegate().getMailbox(securityContext , mailboxName);
			} else if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_PREF_BANKING)) {
				return new PreferredBankerMailboxDelegate().getMailbox(securityContext , mailboxName);
			} else if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_PRIVATE_BANKING)) {
				return new PrivateBankerMailboxDelegate().getMailbox(securityContext , mailboxName);
			} else if (isAuthorisedTaskId(securityContext , MSEC_ENF_OPERATIONS)) {
				return new GenesysROMailboxDelegate().getMailbox(securityContext , mailboxName);
			} else {
				LOGGER.debugHardCodedMessage(logMethod, "getMailbox throw MailboxAccessDeniedException, user is not authorized for any task.");
				throw new MailboxAccessDeniedException();
			}
		} catch (MailboxAccessDeniedException mailboxAccessDeniedException) {
			LOGGER.error(logMethod, BankmailABPCLogMessages.LOG_ABPC_MAILBOX_ACCESS_DENIED_EXCEPTION + " Session {0}, ErrorCode {1}", mailboxAccessDeniedException, securityContext .getSessionSpecification(), mailboxAccessDeniedException.getMessage());
			Messages myMessages = new Messages();
			myMessages.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(myMessages);
		}
	}

	/**
	 * Returns mailboxes which employee can access.
	 * @param securityContext securityContext
	 * @return employeeMailboxCustomDTOlist List<EmployeeMailboxCustomDTO>
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public List<EmployeeMailboxCustomDTO> getMailboxes(SecurityContext securityContext ) throws BankmailApplicationException, IOException, SAXException {

		final String logMethod = "getMailboxes(securityContext ):EmployeeMailboxCustomDTO";
		// check if user is authorized for the taskID
		try {
			if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_ASC_RETAIL)) {
				return new ASCMailboxDelegate().getMailboxes(securityContext );
			} else if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_ASC_YBB)) {
				return new YBBMailboxDelegate().getMailboxes(securityContext );
			} else if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_CLIENT_SUPPORT)) {
				return new CSUMailboxDelegate().getMailboxes(securityContext );
			} else if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_PREF_BANKING)) {
				return new PreferredBankerMailboxDelegate().getMailboxes(securityContext );
			} else if (isAuthorisedTaskId(securityContext , MSEC_ENF_BANKMAIL_PRIVATE_BANKING)) {
				return new PrivateBankerMailboxDelegate().getMailboxes(securityContext );
			}  else if (isAuthorisedTaskId(securityContext , MSEC_ENF_OPERATIONS)) {
				return new GenesysROMailboxDelegate().getMailboxes(securityContext );
			} else {
				LOGGER.debugHardCodedMessage(logMethod, "getMailboxes throw MailboxAccessDeniedException, user is not authorized for any task.");
				throw new MailboxAccessDeniedException();
			}
		} catch (MailboxAccessDeniedException mailboxAccessDeniedException) {
			LOGGER.error(logMethod, BankmailABPCLogMessages.LOG_ABPC_MAILBOX_ACCESS_DENIED_EXCEPTION, mailboxAccessDeniedException, securityContext.getSessionSpecification(), mailboxAccessDeniedException.getMessage());
			Messages myMessages = new Messages();
			myMessages.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(myMessages);
		}
	}

	/**
	 * Validates message content provided by the employee. * Message must be valid HTML with
	 * <p>
	 * , <br>
	 * and <a> tags only, otherwise InvalidAttributeValueException is thrown. Only links to abnamro.nl domain are
	 * allowed.
	 * @param content String
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public void validateMessageContent(String content) throws BankmailApplicationException {
		final String logMethod = "validateMessageContent(String):void";
		try {

			EmployeeTagValidator validator = new EmployeeTagValidator(content);
			// Check if tagNames are valid and if a tag exists then href
			// must have 'abnamro.nl' domain
			if (!validator.isValid()) {
				LOGGER.error(logMethod, BankmailABPCLogMessages.LOG_ABPC_EMPLOYEE_VALIDATE_CONTENT_ERROR, content);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_EMPLOYEE_CONTENT_VALIDATION_FAILED),
					MessageType.getError());
				// Throw unexpected exception
				throw new InvalidAttributeValueException(msgs);
			}

		} catch (IOException ioException) {
			LOGGER.error(logMethod, BankmailABPCLogMessages.LOG_ABPC_EMPLOYEE_VALIDATE_CONTENT_IO_EXCEPTION, ioException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_EMPLOYEE_VALIDATE_CONTENT_IO_EXCEPTION), MessageType.getError());
			// Throw unexpected exception
			throw new BankmailApplicationException(msgs);
		}
	}

	/**
	 * Check if the user is authorized to access task
	 * @param securityContext securityContext
	 * @param taskId String
	 * @return isAuthorised boolean
	 * @throws BankmailApplicationException
	 */
	private boolean isAuthorisedTaskId(SecurityContext securityContext , String taskId) throws BankmailApplicationException {
		final String logMethod = "isAuthorisedTaskId(securityContext ,String):boolean";
		SecurityAgent securityAgent = null;
		String smSSS = null;
		try {
			securityAgent = SecurityAgentFactory.getSecurityAgent();

			smSSS = securityContext .getSessionSpecification();

			UserAuthResult userAuthResult = SecurityAgentUtils.authoriseUserForTask(securityAgent,
				BankmailConstants.MSEC_APP_NAME, smSSS, new String[] { taskId });

			LOGGER.debugHardCodedMessage(logMethod, "isAuthorisedTaskId for task :{0} :: userAuthResult returnCode :{2}", taskId, userAuthResult.getResultCode());
			return (SecurityAgentResult.SUCCESS == userAuthResult.getResultCode());
		} catch (MSecException msecException) {
			LOGGER.error(logMethod, BankmailABPCLogMessages.LOG_ASC_EMPLOYEE_MSEC_EXCEPTION, msecException, securityContext.getSessionSpecification(), msecException.getErrorCode());
			Messages myMessages = new Messages();
			myMessages.addMessage(new Message(SecurityAgentUtils.getMessageKey(MSecException.class, msecException
				.getErrorCode())), MessageType.getError());
			throw new BankmailApplicationException(myMessages);
		}
	}
	
	public boolean isHealthy() throws HealthCheckException {
		return true;
	}

}
