package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.genj.securitycontext.errorhandling.SecurityContextMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCLogMessages;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.implementation.BankmailASCLogConstants;
import com.abnamro.nl.channels.geninfo.bankmail.asc.implementation.EventLogASCImpl;
import com.abnamro.nl.channels.geninfo.bankmail.client.BankmailAccessImpl;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.EmployeeMailboxCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxAccessDeniedException;
import com.abnamro.nl.employeeinfo.service.interfaces.*;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import com.siebel.customui.BankmailAccess;
import com.siebel.customui.BankmailPlus1Input;
import com.siebel.customui.BankmailPlus1Output;
import com.siebel.xml.aabr_20bankmail_20calendar_20io_ext.ListOfAabrBankmailCalendarIoExt;
import com.siebel.xml.aabr_20bankmail_20calendar_20io_ext.UserAccess;
import com.siebel.xml.aabr_20bankmail_20calendar_20io_ext.Users;
import com.siebel.xml.aabr_20bankmail_20response_20io.AccessGrant;
import com.siebel.xml.aabr_20bankmail_20response_20io.ReturnCodes;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.ArrayList;
import java.util.List;

/**
 * CCAMailboxDelegate Defines mailboxes for employees with CCA.
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS					01-10-2012	Initial version	Release 1.0
 * TCS					04-12-2013  Bankmail++			createMailbox renamed to createMailboxDTO
 * </PRE>
 * 
 * @author Sushant Karande
 * @see
 */
@LogInterceptorBinding

public class CCAMailboxDelegate implements EmployeeMailboxDelegate {
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(EventLogASCImpl.class);

	public static final int SUBSTR_THIRD_LETTER = 2;
	public static final int SUBSTR_NINTH_LETTER = 8;

	public static final int SUBSTRING_FROM = 0;
	public static final int SUBSTRING_TO = 6;
	public static final String RETURN_CODE_1 = "1";

	private EmployeeInfoService employeeInfoService = null;
	private BankmailAccess bankMailAccess = null;

	/**
	 * Set employeeinfoservice
	 * 
	 * @param employeeInfo
	 *            employeeInfo
	 */
	public void setEmployeeInfoService(EmployeeInfoService employeeInfo) {
		if (employeeInfo == null) {
			employeeInfoService = employeeInfo;
		}
	}

	/**
	 * @param bankMailAccess
	 *            bankMailAccess
	 */

	public void setBankmailAccessImpl(BankmailAccess bankMailAccess) {
		if (null == bankMailAccess) {
			this.bankMailAccess = new BankmailAccessImpl();
		} else {
			this.bankMailAccess = bankMailAccess;
		}
	}

	public static final String EMPLOYEE_NAME = "#$EmployeeName$#";
	/**
	 * Mailbox template used to create new instances of
	 * EmployeeMailboxCustomDTO. This property is provided during object
	 * construction and cannot be changed (is frozen).
	 */
	private CCAMailboxTemplate mailboxTemplate;

	/**
	 * @param accountManagerInfo
	 *            AccountManagerInfoDTO
	 * @return EmployeeMailboxCustomDTO :Employee-specific information about a
	 *         mailbox.
	 * @throws BankmailApplicationException
	 *             BankmailApplicationException
	 */
	private EmployeeMailboxCustomDTO createMailboxDTO(
			AccountManagerInfoDTO accountManagerInfo)
			throws BankmailApplicationException {
		final String LOG_METHOD = "createMailbox(AccountManagerInfoDTO):EmployeeMailboxCustomDTO";

		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = null;

		// Set values in employeeMailboxCustomDTO
		employeeMailboxCustomDTO = new EmployeeMailboxCustomDTO();

		
		employeeMailboxCustomDTO.setName(mailboxTemplate
				.getMailboxName(accountManagerInfo.getCCA()));
		LOGGER.debugHardCodedMessage(LOG_METHOD, "CCA mailbox Name:"
				+ employeeMailboxCustomDTO.getName());

		// Get SignatureTemplate and replace the EmployeeName placeholder
		// with the Employee name
		String signatureTemplate = mailboxTemplate.getSignatureTemplate();
		signatureTemplate = signatureTemplate.replace(EMPLOYEE_NAME,
				accountManagerInfo.getName());
		employeeMailboxCustomDTO.setSignature(signatureTemplate);

		employeeMailboxCustomDTO.setDisplayName(accountManagerInfo.getName());
		employeeMailboxCustomDTO.setFullDisplayName(mailboxTemplate
				.getDisplayNamePrefix().trim()
				+ " "
				+ accountManagerInfo.getName());

		employeeMailboxCustomDTO.setIsReachCheckNeeded(Boolean.FALSE);
		employeeMailboxCustomDTO.setIsCreateAllowed(Boolean.TRUE);
		employeeMailboxCustomDTO.setIsOverviewAllowed(Boolean.TRUE);
		employeeMailboxCustomDTO.setIsDeleteAllowed(Boolean.TRUE);
		return employeeMailboxCustomDTO;
	}

	/**
	 * Retrieves an employee mailbox.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            mailbox name
	 * @return EmployeeMailboxCustomDTO Employee-specific information about a
	 *         mailbox
	 * @throws BankmailApplicationException
	 *             BankmailApplicationException
	 */
	public EmployeeMailboxCustomDTO getMailbox(SecurityContext securityContext,
			String mailboxName) throws BankmailApplicationException {
		final String LOG_METHOD = "getMailbox(securityContext, String) : EmployeeMailboxCustomDTO ";

		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = null;		
		AccountManagerInfoDTO accountManagerInfoDTO = null;
		AccountManagerEmployeeDTO accountManagerEmployeeDTO = null;
		BankmailPlus1Output bankmailPlus1Output = null;
		BankmailPlus1Input bankmailPlus1Input = null;
		String cCA = null;

		try {
			LOGGER.debugHardCodedMessage(LOG_METHOD, "Mailbox Name : {0}",
					mailboxName);
			// If mailbox name starts with cc
			if ((null != mailboxTemplate && mailboxTemplate
					.isCCAMailboxName(mailboxName))
					|| BankmailConstants.DEFAULT_MAILBOX_NAME
							.equals(mailboxName)) {
				setEmployeeInfoService(employeeInfoService);
				// if mailbox name is user(default mailbox)
				if (BankmailConstants.DEFAULT_MAILBOX_NAME.equals(mailboxName)) {					
					accountManagerInfoDTO = employeeInfoService
							.retrieveAccountManagerEmployeeBySBTId(securityContext
									.getUserReference());
				} else {
					cCA = mailboxName.trim().substring(SUBSTR_THIRD_LETTER);
					// retrieve accountManagerInfo details for CCA					
					accountManagerInfoDTO = employeeInfoService
							.retrieveAccountManagerInfoByCCA(cCA, false);
				}
				// If Not found or AccountManagerGroupDTO is returned
				if (null == accountManagerInfoDTO
						|| (accountManagerInfoDTO instanceof AccountManagerGroupDTO)) {
					LOGGER.error(
							LOG_METHOD,
							BankmailABPCLogMessages.LOG_ABPC_MAILBOX_ACCESS_DENIED_EXCEPTION
									+ "mailboxName={0}", mailboxName);
					Messages myMessages = new Messages();
					myMessages
							.addMessage(
									new Message(
											BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION),
									MessageType.getError());
					throw new MailboxAccessDeniedException(myMessages);
				}
				// Cast the accountManagerInfoDTO to accountManagerEmployeeDTO
				// object
				accountManagerEmployeeDTO = (AccountManagerEmployeeDTO) accountManagerInfoDTO;
				// Check if userId is equal to accountManagerEmployee SbtId
				if (!securityContext.getUserReference().equals(
						accountManagerEmployeeDTO.getSbtID())) {
					LOGGER.info(
							LOG_METHOD,
							BankmailABPCLogMessages.LOG_INFO_ABPC_IS_AUTHORIZED_FOR,
							securityContext.getUserReference(),
							accountManagerEmployeeDTO.getSbtID());
					// call CRM WS to check authorization
					bankmailPlus1Input = new BankmailPlus1Input();					
					// call CRM WS to check authorization
					
					setBankmailAccessImpl(bankMailAccess);
					// map input details
					createBankmailAuthorisedForInput(
							securityContext.getUserReference(),
							accountManagerEmployeeDTO.getSbtID(),
							bankmailPlus1Input);
					LOGGER.debugHardCodedMessage(LOG_METHOD,
							BankmailConstants.BANKMAIL_PLUS1_INPUT_USERSBTID,
							bankmailPlus1Input
							.getListOfAabrBankmailCalendarIoExt().getUsers()
							.get(0).getUSERSBTID());	
					bankmailPlus1Output = getBankmailplusoutput1(bankmailPlus1Input);				
										
					if(bankmailPlus1Output!=null) {
					processErrorCode(mailboxName, bankmailPlus1Output);				
					}
					
				}
			} else {
				LOGGER.error(
						LOG_METHOD,
						BankmailASCLogConstants.LOG_ASC_MAILBOX_ACCESS_DENIED_EXCEPTION,
						securityContext.getUserReference(), mailboxName);
				Messages myMessages = new Messages();
				myMessages
						.addMessage(
								new Message(
										BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION),
								MessageType.getError());
				throw new MailboxAccessDeniedException(myMessages);
			}
			LOGGER.debugHardCodedMessage(LOG_METHOD,
					"accountManagerInfoDTO.getCCA() : {0}",
					accountManagerInfoDTO.getCCA());
			// construct mailbox from AccountManagerInfo
			employeeMailboxCustomDTO = createMailboxDTO(accountManagerInfoDTO);
		} catch (EmployeeInfoServiceException employeeInfoServiceException) {
			LOGGER.error(LOG_METHOD,
					SecurityContextMessageKeys.LOG_PROBLEM_RETRIEVE_SBUDETAILS,
					employeeInfoServiceException, securityContext.getUserReference());
			Messages myMessages = new Messages();
			myMessages
					.addMessage(
							new Message(
									BankmailABPCMessageKeys.ERROR_EMPLOYEE_PROBLEM_RETRIEVE_SBUDETAILS),
							MessageType.getError());
			throw new BankmailApplicationException(myMessages);
		}
		return employeeMailboxCustomDTO;
	}
	
	private BankmailPlus1Output getBankmailplusoutput1(BankmailPlus1Input bankmailPlus1Input) {
		final String LOG_METHOD = "getBankmailplusoutput1(bankmailPlus1Input) : BankmailPlus1Output ";
		BankmailPlus1Output bankmailPlus1Output = null;
	try {
		 bankmailPlus1Output = bankMailAccess
				.bankmailPlus1(bankmailPlus1Input);
		LOGGER.debugHardCodedMessage(LOG_METHOD,
				BankmailConstants.BANKMAIL_PLUS1_RECORD_EXIST,
				new Object[] { bankmailPlus1Output.getListOfBankmailResponse().getUsers().get(0).getRecExists() });

	} catch (SOAPFaultException soapFaultException) {
		bankmailPlus1Output = null;
		LOGGER.warn(
				LOG_METHOD,
				BankmailABPCLogMessages.LOG_WARN_SOAP_FAULT_EXCEPTION,
				null, soapFaultException);
	}
	return bankmailPlus1Output;
	}

	/**
	 * Process Error Code
	 * 
	 * @param mailboxName
	 *            mailboxName
	 * @param bankmailPlus1Output
	 *            response object from CRM WS
	 * @throws BankmailApplicationException
	 *             in case of exception
	 * @throws MailboxAccessDeniedException
	 *             in case of exception
	 */
	private void processErrorCode(String mailboxName,
			BankmailPlus1Output bankmailPlus1Output)
			throws BankmailApplicationException, MailboxAccessDeniedException {
		final String LOG_METHOD = "processErrorCode(String, BankmailPlus1Output) : EmployeeMailboxCustomDTO ";
		for (com.siebel.xml.aabr_20bankmail_20response_20io.Users user : bankmailPlus1Output
				.getListOfBankmailResponse().getUsers()) {
			for (ReturnCodes returnCodes : user.getReturnCodes()) {
				if (RETURN_CODE_1.equalsIgnoreCase(returnCodes.getReturnCode())) {
					String errCode = bankmailPlus1Output.getErrorSpcCode();
					LOGGER.error(LOG_METHOD,
							BankmailABPCLogMessages.LOG_ERROR_RETURN_CODE_1,
							returnCodes.getReturnCode(), errCode);
					Messages messages = mapErrorCodeWithMessageKeys(errCode);
					throw new BankmailApplicationException(messages);
				} else {
					if (null != user.getRecExists()) {
						if (BankmailConstants.RECORD_EXIST_N
								.equalsIgnoreCase(user.getRecExists())) {
							LOGGER.error(
									LOG_METHOD,
									BankmailABPCLogMessages.LOG_ASC_MAILBOX_ACCESS_DENIED_EXCEPTION,
									mailboxName);
							Messages myMessages = new Messages();
							myMessages
									.addMessage(
											new Message(
													BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION),
											MessageType.getError());
							throw new MailboxAccessDeniedException(myMessages);
						}
					} 
				}
			}
		}
	}

	/**
	 * Map input details to check CRM authorization
	 * 
	 * @param userReference
	 *            userReference
	 * @param sbtId
	 *            sbtId
	 * @param bankmailPlus1Input
	 *            bankmailPlus1Input
	 */
	private void createBankmailAuthorisedForInput(String userReference,
			String sbtId, BankmailPlus1Input bankmailPlus1Input) {
		ListOfAabrBankmailCalendarIoExt listOfAabrBankmailCalendarIoExt = null;
		Users users = null;
		listOfAabrBankmailCalendarIoExt = new ListOfAabrBankmailCalendarIoExt();
		users = new Users();
		users.setApplicationId(BankmailConstants.BANKMAIL);
		users.setOperation(BankmailConstants.IS_AUTHORISED_FOR);
		users.setUSERSBTID(userReference);
		UserAccess userAccess = new UserAccess();
		userAccess.setAccessSBTID(sbtId);
		users.getUserAccess().add(userAccess);
		listOfAabrBankmailCalendarIoExt.getUsers().add(users);
		bankmailPlus1Input
				.setListOfAabrBankmailCalendarIoExt(listOfAabrBankmailCalendarIoExt);
	}

	/**
	 * Returns mailboxes which employee can access.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @return EmployeeMailboxCustomDTO[] Employee-specific information about a
	 *         mailbox
	 * @throws BankmailApplicationException
	 *             BankmailApplicationException
	 */
	public List<EmployeeMailboxCustomDTO> getMailboxes(SecurityContext securityContext)
			throws BankmailApplicationException {
		final String LOG_METHOD = "getMailboxes(SecurityContext securityContext) : List<EmployeeMailboxCustomDTO> ";
		List<EmployeeMailboxCustomDTO> employeeMailboxCustomDTOList = null;		
		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = null;
		AccountManagerEmployeeDTO accountManagerEmployeeDTO = null;
		BankmailPlus1Output bankmailPlus1Output = null;
		BankmailPlus1Input bankmailPlus1Input = null;
		List<String> sBTIdList = null;
		try {			
			bankmailPlus1Input = new BankmailPlus1Input();
			setEmployeeInfoService(employeeInfoService);
			// create empty SbtId List and add the userId to the sbtIDList
			sBTIdList = new ArrayList<String>();
			sBTIdList.add(securityContext.getUserReference());
			LOGGER.info(
					LOG_METHOD,
					BankmailABPCLogMessages.LOG_INFO_ABPC_GET_AUTHORIZATIONS_FOR_USER,
					securityContext.getUserReference());
			// call CRMAuthorisation WS and get the list of employees who's
			// mailboxes can be accessed by logged in user			
			setBankmailAccessImpl(bankMailAccess);
			// map input details
			mapInputDetailsForGetUserList(sBTIdList, bankmailPlus1Input);
			LOGGER.debugHardCodedMessage(LOG_METHOD,
					BankmailConstants.BANKMAIL_PLUS1_INPUT_USERSBTID, bankmailPlus1Input
							.getListOfAabrBankmailCalendarIoExt().getUsers()
							.get(0).getUSERSBTID());
			bankmailPlus1Output = getBankmailplusOututAccessGrant(bankmailPlus1Input);			
			List<String> employeeSBTIds = new ArrayList<String>();
			if (bankmailPlus1Output!= null && null != bankmailPlus1Output.getListOfBankmailResponse()
					.getUsers()) {
				for (com.siebel.xml.aabr_20bankmail_20response_20io.Users user : bankmailPlus1Output
						.getListOfBankmailResponse().getUsers()) {
					for (ReturnCodes returnCodes : user.getReturnCodes()) {
						if (RETURN_CODE_1.equalsIgnoreCase(returnCodes
								.getReturnCode())) {
							String errCode = bankmailPlus1Output
									.getErrorSpcCode();
							LOGGER.error(
									LOG_METHOD,
									BankmailABPCLogMessages.LOG_ERROR_RETURN_CODE_1,
									returnCodes.getReturnCode(), errCode);
							Messages messages = mapErrorCodeWithMessageKeys(errCode);
							throw new BankmailApplicationException(messages);
						} else {
							// get all sbtid from WS
							for (AccessGrant accessGrant : user
									.getAccessGrant()) {
								employeeSBTIds
										.add(accessGrant.getSBTACCESSID());
							}
						}
					}
				}
			}

			// add SBT ids of found employees to the list
			if (!employeeSBTIds.isEmpty()) {
				for (String sbtid : employeeSBTIds) {
					sBTIdList.add(sbtid);
				}
			}

			// for each sbt id ,retrieve account manager info
			for (String sbtid : sBTIdList) {				
				accountManagerEmployeeDTO = employeeInfoService
						.retrieveAccountManagerEmployeeBySBTId(sbtid);
				if (null != accountManagerEmployeeDTO) {
					// get CCA mailbox
					employeeMailboxCustomDTO = createMailboxDTO(accountManagerEmployeeDTO);
					// if list is empty
					if (null == employeeMailboxCustomDTOList) {
						employeeMailboxCustomDTOList = new ArrayList<EmployeeMailboxCustomDTO>();
					}
					// add to result list
					employeeMailboxCustomDTOList.add(employeeMailboxCustomDTO);
				}
			}
		} catch (EmployeeInfoServiceException employeeInfoServiceException) {
			LOGGER.error(LOG_METHOD,
					SecurityContextMessageKeys.LOG_PROBLEM_RETRIEVE_SBUDETAILS,
					employeeInfoServiceException, securityContext.getUserReference());
			Messages myMessages = new Messages();
			myMessages
					.addMessage(
							new Message(
									BankmailABPCMessageKeys.ERROR_EMPLOYEE_PROBLEM_RETRIEVE_SBUDETAILS),
							MessageType.getError());
			throw new BankmailApplicationException(myMessages);
		}
		// return list
		return employeeMailboxCustomDTOList;
	}
	
	private BankmailPlus1Output getBankmailplusOututAccessGrant(BankmailPlus1Input bankmailPlus1Input) {
		final String LOG_METHOD = "getBankmailplusOututAccessGrant(bankmailPlus1Input) : BankmailPlus1Input ";
		BankmailPlus1Output bankmailPlus1Output = null;
		try {
			bankmailPlus1Output = bankMailAccess
					.bankmailPlus1(bankmailPlus1Input);
			if (null != bankmailPlus1Output) {
				for (AccessGrant accessgrant : bankmailPlus1Output
						.getListOfBankmailResponse().getUsers().get(0)
						.getAccessGrant()) {

					LOGGER.debugHardCodedMessage(LOG_METHOD,
							BankmailConstants.BANKMAIL_PLUS1_OUTPUT_SBTACCESSID,
							new Object[] { accessgrant.getSBTACCESSID() });
				}
			}
		} catch (SOAPFaultException soapFaultException) {
			bankmailPlus1Output = null;
			LOGGER.warn(LOG_METHOD,BankmailABPCLogMessages.LOG_WARN_SOAP_FAULT_EXCEPTION,soapFaultException);
		}
		
		return bankmailPlus1Output;
	}

	/**
	 * Map input details to get user list from CRM WS
	 * 
	 * @param sBTIdList
	 *            sBTIdList
	 * @param bankmailPlus1Input
	 *            bankmailPlus1Input
	 */
	private void mapInputDetailsForGetUserList(List<String> sBTIdList,
			BankmailPlus1Input bankmailPlus1Input) {
		ListOfAabrBankmailCalendarIoExt listOfAabrBankmailCalendarIoExt = null;
		Users users = null;
		listOfAabrBankmailCalendarIoExt = new ListOfAabrBankmailCalendarIoExt();
		if (null != sBTIdList) {
			for (String sbtid : sBTIdList) {
				users = new Users();
				users.setUSERSBTID(sbtid);
				users.setApplicationId(BankmailConstants.BANKMAIL);
				users.setOperation(BankmailConstants.GET_USER_LIST);
				listOfAabrBankmailCalendarIoExt.getUsers().add(users);
			}
			bankmailPlus1Input
					.setListOfAabrBankmailCalendarIoExt(listOfAabrBankmailCalendarIoExt);
		}
	}

	/**
	 * @return CCAMailboxTemplate CCA Mailbox Template
	 */
	public CCAMailboxTemplate getMailboxTemplate() {
		return mailboxTemplate;
	}

	/**
	 * @param mailboxTemplate
	 *            CCAMailboxTemplate: CCA Mailbox Template
	 */
	public void setMailboxTemplate(CCAMailboxTemplate mailboxTemplate) {
		this.mailboxTemplate = mailboxTemplate;
	}

	/**
	 * Map web service error code with Message Keys
	 * 
	 * @param errCode
	 *            errCode
	 * @return Messages
	 */
	public Messages mapErrorCodeWithMessageKeys(String errCode) {
		final String LOG_METHOD = "mapErrorCodeWithMessageKeys(String) : Messages";
		LOGGER.debugHardCodedMessage(LOG_METHOD,
				"Error code: {0} :Length: {1}", errCode, errCode.length());
		Messages messages = new Messages();
		if (BankmailConstants.ERR_USER_002.equalsIgnoreCase(errCode.trim())) {
			messages.addMessage(new Message(
					BankmailABPCMessageKeys.ERROR_USER_ID_DOES_NOT_EXIST),
					MessageType.getError());
		} else if (BankmailConstants.ERR_OPR_001.equalsIgnoreCase(errCode
				.trim())) {
			messages.addMessage(new Message(
					BankmailABPCMessageKeys.ERROR_NULL_OPERATION), MessageType
					.getError());
		}
		return messages;
	}

}
