package com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.interfaces.BankmailABPC;
import com.abnamro.nl.channels.geninfo.bankmail.asc.implementation.EventLogASCImpl;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.resources.IncludeActions;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailUtil;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.GatewayApplicationException;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.MessageDTO;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.OnlineGateway;
import com.abnamro.nl.employeeinfo.service.interfaces.AccountManagerGroupDTO;
import com.abnamro.nl.employeeinfo.service.interfaces.AccountManagerInfoDTO;
import com.abnamro.nl.employeeinfo.service.interfaces.EmployeeInfoService;
import com.abnamro.nl.employeeinfo.service.interfaces.EmployeeInfoServiceException;
import com.abnamro.nl.enumeration.*;
import com.abnamro.nl.health.exceptions.HealthCheckException;
import com.abnamro.nl.health.interfaces.HealthCheckable;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import com.abnamro.nl.rest.actions.Action;
import com.abnamro.nl.rest.actions.UserActionIndicator;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BankmailABPCClientImpl BankmailABPCClientImpl Application Business Process Controller Implementation handles all
 * business logic to access mailboxes, manipulate and send messages for bc user.
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  		29-05-2012	Initial version	Release 1.0
 * TCS					03-12-2013	Bankmail++			SyncGateway is renamed to OnlineGateway
 * 																sendMailboxMessage is calling OnlineGateway.asyncPush
 * </PRE>
 * @author
 * @see
 */
@LogInterceptorBinding
public class BankmailABPCClientImpl implements BankmailABPC,HealthCheckable {
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(BankmailABPCClientImpl.class);
	/**
	 * Bankmail General Util class
	 */
	private static BankmailUtil bankmailUtil = new BankmailUtil();
	@EJB
			(lookup = "java:global/BS103_EmployeeInformationService/BSI103_EJB/EmployeeInfoServiceImpl!com.abnamro.nl.employeeinfo.service.interfaces.EmployeeInfoService")
	private EmployeeInfoService employeeInfoService;


	@Inject
	private BankmailClientASC bankmailClientASC;
	@Inject
	private SendBankmailPolicyASC bankmailPolicyASC;
  @Inject
	private BankmailASC bankmailASC;
  @Inject
	private MessageArchiveASC archiveASC;

	@Inject
	@Named("GensysGateway")
	private  OnlineGateway genesysSyncGateway;

	@Inject
	@Named("CRMGateway")
	private  OnlineGateway cRMSyncGateway;

	/**
	 * Retrieves list of Mailboxes which can be accessed by logged in user. If includeActions parameter is provided,
	 * actions allowed on each mailbox are returned
	 * @param securityContext SecurityContext
	 * @param includeActions IncludeActions
	 * @return MailboxListDTO which holds all mail boxes for logged in user.
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public MailboxListDTO getMailboxes(SecurityContext securityContext , IncludeActions includeActions)
			throws BankmailApplicationException, IOException, SAXException {
		MailboxListDTO mailboxListDTO = null;

		mailboxListDTO = new MailboxListDTO();

		String currentUserAddress = bankmailClientASC.getCurrentUserAddress(securityContext);

		MailboxCustomDTO mailboxCustomDTO = getMailbox(securityContext, currentUserAddress, includeActions);

		List<MailboxCustomDTO> mailboxes = new ArrayList<MailboxCustomDTO>();

		mailboxes.add(mailboxCustomDTO);

		mailboxListDTO.setMailboxes(mailboxes);
		return mailboxListDTO;
	}

	/**
	 * Retrieves a mailbox. If includeActions parameter is provided, actions allowed on the mailbox are returned
	 * @param securityContext securityContext
	 * @param mailboxName String
	 * @param includeActions IncludeActions
	 * @return MailboxCustomDTO mailbox DTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxEception
	 *
	 */
	public MailboxCustomDTO getMailbox(SecurityContext securityContext , String mailboxName, IncludeActions includeActions)
			throws BankmailApplicationException, IOException, SAXException {
		MailboxCustomDTO mailboxCustomDTO = null;

		String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);

		mailboxCustomDTO = new MailboxCustomDTO();

		mailboxCustomDTO.setName(resolvedMailboxName);

		String bcNumber = resolvedMailboxName.substring(2);
		SendBankmailPolicyDTO sendBankmailPolicyDTO = bankmailPolicyASC.getSendBankmailPolicy(securityContext, bcNumber,
			false);

		if (resolvedMailboxName.startsWith(BankmailConstants.BC_MAILBOX_NAME_PREFIX)) {
			if (null == sendBankmailPolicyDTO) {
				mailboxCustomDTO.setIsSubjectSelectable(false);
			} else {
				mailboxCustomDTO.setIsSubjectSelectable(sendBankmailPolicyDTO.getCanSelectSubject());
			}

		} else {
			mailboxCustomDTO.setIsSubjectSelectable(false);
		}

			mailboxCustomDTO.setDisplayName(bankmailClientASC.getDisplayNameOfAddress(resolvedMailboxName));

		if (null != includeActions) {

			SendBankmailPolicyListDTO bankmailPolicyListDTO = bankmailPolicyASC.getSendBankmailPolicies(securityContext);

			List<Action<MailboxActionName>> actions = new ArrayList<Action<MailboxActionName>>();

			// delete action
			Action<MailboxActionName> actionDelete = new Action<MailboxActionName>();
			actionDelete.setName(MailboxActionName.DELETE_MESSAGES);
			actionDelete.setUserActionIndicator(UserActionIndicator.ALLOWED);

			// create action
			Action<MailboxActionName> actionCreate = new Action<MailboxActionName>();
			actionCreate.setName(MailboxActionName.CREATE_MESSAGE);
			actionCreate.setUserActionIndicator(UserActionIndicator.NOT_ALLOWED);

			// send action
			Action<MailboxActionName> actionSend = new Action<MailboxActionName>();
			actionSend.setName(MailboxActionName.SEND_MESSAGE);
			actionSend.setUserActionIndicator(UserActionIndicator.NOT_ALLOWED);

			if (null != bankmailPolicyListDTO && null != bankmailPolicyListDTO.getSendBankmailPolicies()
					&& !bankmailPolicyListDTO.getSendBankmailPolicies().isEmpty()) {

				actionCreate.setUserActionIndicator(UserActionIndicator.ALLOWED);
				actionSend.setUserActionIndicator(UserActionIndicator.ALLOWED);

			}

			// add actions
			actions.add(actionCreate);
			actions.add(actionSend);
			actions.add(actionDelete);

			mailboxCustomDTO.setActions(actions);

		}
		return mailboxCustomDTO;
	}

	/**
	 * Saves new message. Only message attributes isSeen, inReplyTo, subject and content are taken from input. Employee
	 * must also choose message recipient. Reply messages have to have inReplyTo attribute filled in, only message id has
	 * to be provided. New message is created in "Draft" folder.
	 * @param securityContext securityContext
	 * @param mailboxName String
	 * @param message MailboxMessageCustomDTO
	 * @return String String
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public String createMailboxMessage(SecurityContext securityContext , String mailboxName, MailboxMessageCustomDTO message)
			throws BankmailApplicationException, IOException, SAXException {
		String id = null;
		String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);

		// validate Content
		String content = message.getContent();
		if (null != (content)) {
			bankmailClientASC.validateMessageContent(content);
		}

		// from address
		MailContactCustomDTO from = new MailContactCustomDTO();
		from.setAddress(bankmailClientASC.getCurrentUserAddress(securityContext));
		from.setDisplayName(bankmailClientASC.getDisplayNameOfAddress(from.getAddress()));
		message.setFrom(from);

		// routeMailboxMessage
		message = routeMailboxMessage(securityContext, message, resolvedMailboxName);

		id = bankmailASC.createMailboxMessage(resolvedMailboxName, message);
		return id;
	}

	/**
	 * Sends given message. Message has to be saved prior to sending. Message is removed from the folder after it is
	 * sent. During delivery it will be placed in the "Sent" folder. This operation is restricted to messages in "Draft"
	 * folder.
	 * @param securityContext securityContext
	 * @param mailboxName String
	 * @param messageId String
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public void sendMailboxMessage(SecurityContext securityContext , String mailboxName, String messageId)
			throws BankmailApplicationException, IOException, SAXException {

		final String LOG_METHOD = "sendMailboxMessage(securityContext, String, String ):void";

		try {

			String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);

			// get message for given message id
			MailboxMessageCustomDTO oldMessage = bankmailASC.getMailboxMessage(resolvedMailboxName, messageId);
			String folderName = oldMessage.getFolder().getName();

			LOGGER.debugHardCodedMessage(LOG_METHOD, "STEP 111 :::::: Mailbox Name :{0}, Folder Name :{1}, messageId :{2}", mailboxName, folderName, messageId);
			
			if (BankmailConstants.DRAFT_FOLDER_NAME.equalsIgnoreCase(folderName)) {

				MailboxMessageCustomDTO newMessage = routeMailboxMessage(securityContext, oldMessage, resolvedMailboxName);

				bankmailASC.updateMailboxMessage(resolvedMailboxName, oldMessage, newMessage);

				String sender = bankmailUtil.getSenderName(securityContext);
				LOGGER.debugHardCodedMessage(LOG_METHOD, "Sender :{0}", sender);
				// CRM & Genesys Gateway start
				MessageDTO messageDTO = bankmailUtil.convertMessage(newMessage);
				messageDTO.setSender(sender);
				
				// GenesysGateway
				String receipentAddress = messageDTO.getTo()[0].getBankmailAddress();
				LOGGER.debugHardCodedMessage(LOG_METHOD, "SgenesysSyncGateway.syncPush(messageDTO);TEP 222 :::::: receipentAddress {0}", receipentAddress);
				
				if (receipentAddress.equals(GenesysMailboxTemplate.MAILBOX_NAME) ||
						receipentAddress.startsWith(GenesysMailboxTemplate.MAILBOX_NAME_GR_OPS)) {
					genesysSyncGateway.syncPush(messageDTO);


				}
				// CRM & Genesys Gateway endd
				// Archive message
				archiveASC.archiveMessage(securityContext, messageDTO);

				// log send message event
				EventLogASC eventLogASC = new EventLogASCImpl();
				eventLogASC.logSendMessage(securityContext, oldMessage);
				LOGGER.debugHardCodedMessage(LOG_METHOD, "STEP 333 :::::: deliverMailboxMessage messageId {0} ", messageId);
				
				bankmailASC.deliverMailboxMessage(sender, resolvedMailboxName, messageId);

				// CRMGateway
				LOGGER.debugHardCodedMessage(LOG_METHOD, "STEP 444 :::::: asyncPush {0}", messageId);
				cRMSyncGateway.asyncPush(messageId);

				
			} else if (!BankmailConstants.SENT_FOLDER_NAME.equalsIgnoreCase(folderName)) {
				LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_MESSAGE_NOT_FOUND_EXCEPTION, messageId);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MESSAGE_NOT_FOUND_EXCEPTION), MessageType.getError());
				// Throw unexpected exception
				throw new MessageNotFoundException(msgs);
			}
		} catch (GatewayApplicationException gatewayApplicationException) {
			LOGGER.error(LOG_METHOD,
					BankmailABPCLogMessages.LOG_GATEWAY_APPLICATION_EXCEPTION,
					gatewayApplicationException);
			Messages msgs = gatewayApplicationException.getMessages();
			throw new BankmailApplicationException(msgs);
		}
	}

	/**
	 * Permanently delete messages from the mailbox.
	 * @param securityContext securityContext
	 * @param mailboxName String
	 * @param messageIds String[]
	 * @param fromFolder String
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public void deleteMailboxMessages(SecurityContext securityContext , String mailboxName, List<String> messageIds,
			String fromFolder) throws BankmailApplicationException {
		String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);

		bankmailASC.deleteMailboxMessages(resolvedMailboxName, messageIds, fromFolder);

		// log delete event
		EventLogASC eventLogASC = new EventLogASCImpl();
		for (String messageId : messageIds) {
			eventLogASC.logDeleteMessage(securityContext, resolvedMailboxName, messageId);
		}
	}

	/**
	 * Description Retrieves messages from given folder within given mailbox. The "to" attribute contains only the first
	 * recipient and "subject" only first 100 characters. Messages can be searched by given text. Messages can be
	 * filtered by MailboxMessageTypes. Result can be sorted by one of the available sort orders.
	 * @param securityContext securityContext
	 * @param mailboxName Mailbox name
	 * @param folderName Folder name
	 * @param searchText Search text
	 * @param messageTypes Message types
	 * @param sortBy Sort by parameter
	 * @param pageNumber Page number
	 * @param pageSize Page size
	 * @return MailboxMessageListDTO
	 * @throws BankmailApplicationException Application exception
	 */
	public MailboxMessageListDTO getMailboxMessages(SecurityContext securityContext , String mailboxName,
			MailboxFolderName folderName, List<MailMessageType> messageTypes, String searchText, MessagesSortOrder sortBy,
			Integer pageNumber, Integer pageSize) throws BankmailApplicationException {
		MailboxMessageListDTO mailboxMessageListDTO = null;
		String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);

		List<MailboxMessageCustomDTO> mailboxMessageCustomDTOs = bankmailASC.getMailboxMessages(resolvedMailboxName,
			folderName, messageTypes, searchText, sortBy, pageNumber, pageSize);

		// logged in user address
		String currentUserAddress = bankmailClientASC.getCurrentUserAddress(securityContext);
		String displayName = bankmailClientASC.getDisplayNameOfAddress(currentUserAddress);

		if (null != mailboxMessageCustomDTOs) {

			// remove all recipients and add only current one.
			int toSize = 1;
			for (MailboxMessageCustomDTO mailboxMessageCustomDTO : mailboxMessageCustomDTOs) {

				// remove from message.to.address, message.from.address
				if (null != mailboxMessageCustomDTO.getTo()) {
					for (MailContactCustomDTO contactCustomDTO : mailboxMessageCustomDTO.getTo()) {
						contactCustomDTO.setAddress(null);
					}
				}
				if (null != mailboxMessageCustomDTO.getFrom()) {
					mailboxMessageCustomDTO.getFrom().setAddress(null);
				}

				if (null != mailboxMessageCustomDTO.getReceivedDate()) {
					List<MailContactCustomDTO> to = new ArrayList<MailContactCustomDTO>(toSize);
					MailContactCustomDTO toContact = new MailContactCustomDTO();
					toContact.setDisplayName(displayName);
					to.add(toContact);
					mailboxMessageCustomDTO.setTo(to);
				}
			}
		}

		mailboxMessageListDTO = new MailboxMessageListDTO();
		mailboxMessageListDTO.setMailMessages(mailboxMessageCustomDTOs);
		return mailboxMessageListDTO;
	}

	/**
	 * Retrieves a message. If employee is logged in then bankmailAddress is returned in Contact otherwise it is skipped
	 * from output. If includeActions parameter is provided, actions allowed on the message are returned.
	 * @param securityContext securityContext
	 * @param mailboxName String
	 * @param messageId String
	 * @param includeActions IncludeActions
	 * @return MailboxMessageCustomDTO mailboxMessageCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public MailboxMessageCustomDTO getMailboxMessage(SecurityContext securityContext , String mailboxName, String messageId,
			IncludeActions includeActions) throws BankmailApplicationException, IOException, SAXException {
		MailboxMessageCustomDTO mailboxMessageCustomDTO = null;
		String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);

		mailboxMessageCustomDTO = bankmailASC.getMailboxMessage(resolvedMailboxName, messageId);

		// remove from message.to.address, message.from.address
		if (null != mailboxMessageCustomDTO.getTo()) {
			for (MailContactCustomDTO contactCustomDTO : mailboxMessageCustomDTO.getTo()) {
				contactCustomDTO.setAddress(null);
			}
		}
		if (null != mailboxMessageCustomDTO.getFrom()) {
			mailboxMessageCustomDTO.getFrom().setAddress(null);
		}


		// remove all recipients and add only current one.
		int toSize = 1;

		if (null != mailboxMessageCustomDTO.getReceivedDate()) {

			// logged in user address
			String currentUserAddress = bankmailClientASC.getCurrentUserAddress(securityContext);
			String displayName = bankmailClientASC.getDisplayNameOfAddress(currentUserAddress);

			List<MailContactCustomDTO> to = new ArrayList<MailContactCustomDTO>(toSize);
			MailContactCustomDTO toContact = new MailContactCustomDTO();
			toContact.setDisplayName(displayName);
			to.add(toContact);
			mailboxMessageCustomDTO.setTo(to);
		}

		if (null != includeActions) {

			List<Action<MailboxMessageActionName>> actions = new ArrayList<>();
			// Reply action
			Action<MailboxMessageActionName> actionReply = new Action<MailboxMessageActionName>();
			actionReply.setName(MailboxMessageActionName.REPLY);
			actionReply.setUserActionIndicator(UserActionIndicator.NOT_ALLOWED);

			// Update action
			Action<MailboxMessageActionName> actionUpdate = new Action<MailboxMessageActionName>();
			actionUpdate.setName(MailboxMessageActionName.UPDATE);
			actionUpdate.setUserActionIndicator(UserActionIndicator.NOT_ALLOWED);

			SendBankmailPolicyDTO bankmailPolicyDTO = bankmailPolicyASC.getSendBankmailPolicy(securityContext,
				mailboxMessageCustomDTO.getConcerningCustomerBCNumber(), true);

			if (Boolean.TRUE.equals(mailboxMessageCustomDTO.getIsReplyAllowed()) && null != bankmailPolicyDTO) {
				actionReply.setUserActionIndicator(UserActionIndicator.ALLOWED);
			}

			if ((null != bankmailPolicyDTO && BankmailConstants.DRAFT_FOLDER_NAME
				.equalsIgnoreCase(mailboxMessageCustomDTO.getFolder().getName()))
					|| Boolean.FALSE.equals(mailboxMessageCustomDTO.getIsSeen())) {
				actionUpdate.setUserActionIndicator(UserActionIndicator.ALLOWED);
			}

			actions.add(actionReply);
			actions.add(actionUpdate);

			mailboxMessageCustomDTO.setActions(actions);
		}
		return mailboxMessageCustomDTO;
	}

	/**
	 * Retrieves all preceding messages in the reply messages chain. Only messages available in the folders "Inbox",
	 * "Archive" and "Sent" are returned. Messages are returned in reversed chronological order (latest first). If
	 * message has receivedDate (received messages only), then it is used for sorting, otherwise originationDate is used.
	 * @param securityContext securityContext
	 * @param mailboxName String
	 * @param messageId String
	 * @return MailboxMessageListDTO mailboxMessageListDTO
	 * @throws BankmailApplicationException Application exception
	 */
	public MailboxMessageListDTO getMailboxMessageThread(SecurityContext securityContext , String mailboxName, String messageId)
			throws BankmailApplicationException {

		MailboxMessageListDTO mailboxMessageListDTO = null;
		String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);

		List<MailboxMessageCustomDTO> mailboxMessageCustomDTOs = bankmailASC.getMailboxMessageThread(
			resolvedMailboxName, messageId);

		// logged in user address
		String currentUserAddress = bankmailClientASC.getCurrentUserAddress(securityContext);
		String displayName = bankmailClientASC.getDisplayNameOfAddress(currentUserAddress);

		if (null != mailboxMessageCustomDTOs) {

			// remove all recipients and add only current one.
			int toSize = 1;
			for (MailboxMessageCustomDTO mailboxMessageCustomDTO : mailboxMessageCustomDTOs) {

				// remove from message.to.address, message.from.address
				if (null != mailboxMessageCustomDTO.getTo()) {
					for (MailContactCustomDTO contactCustomDTO : mailboxMessageCustomDTO.getTo()) {
						contactCustomDTO.setAddress(null);
					}
				}
				if (null != mailboxMessageCustomDTO.getFrom()) {
					mailboxMessageCustomDTO.getFrom().setAddress(null);
				}

				if (null != mailboxMessageCustomDTO.getReceivedDate()) {
					List<MailContactCustomDTO> to = new ArrayList<MailContactCustomDTO>(toSize);
					MailContactCustomDTO toContact = new MailContactCustomDTO();
					toContact.setDisplayName(displayName);
					to.add(toContact);
					mailboxMessageCustomDTO.setTo(to);
				}

			}
		}

		mailboxMessageListDTO = new MailboxMessageListDTO();
		mailboxMessageListDTO.setMailMessages(mailboxMessageCustomDTOs);
		return mailboxMessageListDTO;
	}

	/**
	 * Updates given message. Only "isSeen" attribute can be set to "true". All other changes will be ignored. For
	 * messages in "Draft" folder subject and content can be updated. Employee can also change message recipient.
	 * @param securityContext securityContext
	 * @param mailboxName String
	 * @param message MailboxMessageCustomDTO
	 * @return String String
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public String updateMailboxMessage(SecurityContext securityContext , String mailboxName, MailboxMessageCustomDTO message)
			throws BankmailApplicationException {
		final String LOG_METHOD = "updateMailboxMessage(securitycontext, String, MailboxMessageCustomDTO):String";

		String id = null;

		try {

			String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);

			if (StringUtils.isBlank(message.getId())) {
				LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_INVALID_ATTRIBUTE_VALUE_EXCEPTION, message.getId());
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_INVALID_ATTRIBUTE_VALUE_EXCEPTION), MessageType
					.getError());
				throw new InvalidAttributeValueException();
			}

			if (null != message.getIsSeen() && (!message.getIsSeen())) {
				LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_INVALID_ATTRIBUTE_VALUE_EXCEPTION, message.getIsSeen());
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_INVALID_IS_SEEN_ATTRIBUTE_VALUE), MessageType.getError());
				throw new InvalidAttributeValueException(msgs);
			}

			MailboxMessageCustomDTO oldMessage = null;
			MailboxMessageCustomDTO newMessage = new MailboxMessageCustomDTO();
			String content = message.getContent();
			String subject = message.getSubject();

			if (null != message.getIsSeen() && null == subject && null == content) {
				oldMessage = new MailboxMessageCustomDTO();
				oldMessage.setId(message.getId());
				newMessage.setIsSeen(message.getIsSeen());
				id = bankmailASC.updateMailboxMessage(resolvedMailboxName, oldMessage, newMessage);

			} else {
				// validate Content
				if (null != content) {
					bankmailClientASC.validateMessageContent(content);
				}

				oldMessage = bankmailASC.getMailboxMessage(resolvedMailboxName, message.getId());


				try {
					PropertyUtils.copyProperties(newMessage, oldMessage);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}


				if (null != subject) {
					newMessage.setSubject(subject);
				}

				if (null != message.getIsSeen()) {
					newMessage.setIsSeen(message.getIsSeen());
				}

				if (null != content) {
					newMessage.setContent(content);
				}

				id = bankmailASC.updateMailboxMessage(resolvedMailboxName, oldMessage, newMessage);
			}

			// log open message event
			if (null != message.getIsSeen() && message.getIsSeen()) {
				EventLogASC eventLogASC = new EventLogASCImpl();
				eventLogASC.logOpenMessage(securityContext, resolvedMailboxName, message.getId());
			}

		} catch (IllegalAccessException illegalAccessException) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ILLEGAL_ACCESS_EXCEPTION, illegalAccessException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_ILLEGAL_ACCESS_EXCEPTION), MessageType.getError());
			// Throw unexpected exception
			throw new BankmailApplicationException(msgs);
		} catch (InvocationTargetException invocationTargetException) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_INVOCATION_TARGET_EXCEPTION, invocationTargetException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_INVOCATION_TARGET_EXCEPTION), MessageType.getError());
			// Throw unexpected exception
			throw new BankmailApplicationException(msgs);
		}
		return id;
	}

	/**
	 * Retrieves all folders of a mailbox.
	 * @param securityContext securityContext
	 * @param mailboxName String
	 * @return FolderListDTO list of folders in a mail as ListDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public FolderListDTO getFolders(SecurityContext securityContext , String mailboxName) throws BankmailApplicationException {
		FolderListDTO folderListDTO = null;

		String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);
		folderListDTO = new FolderListDTO();
		folderListDTO.setFolders(bankmailASC.getFolders(resolvedMailboxName));

		return folderListDTO;
	}

	/**
	 * Retrieves a folder from given mailbox.
	 * @param securityContext securityContext
	 * @param mailboxName mailboxName
	 * @param folderName MailboxFolderName
	 * @return FolderCustomDTO folderCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public FolderCustomDTO getFolder(SecurityContext securityContext , String mailboxName, MailboxFolderName folderName)
			throws BankmailApplicationException {
		FolderCustomDTO folderCustomDTO = null;
		String resolvedMailboxName = resolveMailboxNameAndCheckAccess(securityContext, mailboxName);
		folderCustomDTO = bankmailASC.getFolder(resolvedMailboxName, folderName);

		return folderCustomDTO;
	}

	/**
	 * Description Retrieves a list of SendBankmailPolicy for customers which can be selected by logged in user in the
	 * attribute "concerningCustomer" in new messages. List is always empty for employees. List can be empty for Internet
	 * Banking clients, in which case they are not allowed to create/send new messages. Actions CREATE_MESSAGE,
	 * SEND_MESSAGE will be disallowed in this case (see getMailbox, getMailboxes, MailboxActionName).
	 * @param securityContext securityContext
	 * @return sendBankmailPolicyListDTO SendBankmailPolicyListDTO
	 * @throws BankmailApplicationException Application exception
	 * @throws IOException Application IOException exception
	 * @throws SAXException Application SAXException exception
	 */
	public SendBankmailPolicyListDTO getSendBankmailPolicies(SecurityContext securityContext ) throws BankmailApplicationException, IOException, SAXException {
		SendBankmailPolicyListDTO sendBankmailPolicyListDTO = null;
		if (null != securityContext) {
			sendBankmailPolicyListDTO = bankmailPolicyASC.getSendBankmailPolicies(securityContext);
		}
		return sendBankmailPolicyListDTO;
	}

	/**
	 * If mailbox name is "user", constructs real mailbox name using provided securityContext, otherwise compares provided
	 * mailbox name with the constructed one. Throws MailboxAccessDeniedException if names do not match.
	 * @param securityContext securityContext
	 * @param mailboxName mailboxName
	 * @return string String
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public String resolveMailboxNameAndCheckAccess(SecurityContext securityContext , String mailboxName)
			throws BankmailApplicationException {

		final String LOG_METHOD = "resolveMailboxNameAndCheckAccess(securityContext,String):String";

		if (null != securityContext && -1 !=(int)securityContext.getSessionAttributes().get("bcnr")
		) {

			// Determine the name of the actual mailbox
			// If client logs in the actual mailbox name will be bc<userClass> or at<userClass>

			String currentUserAddress = bankmailClientASC.getCurrentUserAddress(securityContext);

			if (StringUtils.isNotBlank(currentUserAddress)) {
				if (BankmailConstants.DEFAULT_MAILBOX_NAME.equals(mailboxName)) {
					return currentUserAddress;
				} else if (!mailboxName.equals(currentUserAddress)) {
					LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ASC_MAILBOX_ACCESS_DENIED_EXCEPTION, mailboxName);
					Messages msgs = new Messages();
					msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION),
						MessageType.getError());
					throw new MailboxAccessDeniedException(msgs);
				}
			} else {
				LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ASC_MAILBOX_ACCESS_DENIED_EXCEPTION, mailboxName);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION), MessageType
					.getError());
				throw new MailboxAccessDeniedException(msgs);
			}

		} else {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ASC_USER_NOT_LOGGED_IN, securityContext);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_USER_NOT_LOGGED_IN), MessageType.getError());
			throw new MailboxAccessDeniedException(msgs);
		}
		// return mailbox name
		return mailboxName;
	}

	/**
	 * Saves new message. Only message attributes isSeen, inReplyTo, subject and content are taken from input. Attributes
	 * isSeen, subject and content are required. Employee must also choose message recipient. Reply messages have to have
	 * inReplyTo attribute filled in, only message id has to be provided. New message is created in "Draft" folder.
	 * @param securityContext securityContext
	 * @param mailboxName  String
	 * @param message MailboxMessageCustomDTO message details
	 * @return MailboxMessageCustomDTO details with proper recipients
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public MailboxMessageCustomDTO routeMailboxMessage(SecurityContext securityContext , MailboxMessageCustomDTO message,
			String mailboxName) throws BankmailApplicationException, IOException, SAXException {

		final String LOG_METHOD = "routeMailboxMessage(securityContext, MailboxMessageCustomDTO): MailboxMessageCustomDTO";

		String cCA = null;

		try {

			// destination
			List<MailContactCustomDTO> destination = Collections.emptyList();

			List<MailContactCustomDTO> toAddress = message.getTo();

			// check if message.to is empty
			if (null == toAddress || (BankmailConstants.INT_ZERO >= toAddress.size())) {

				// check if message is a reply message
				if (null != message.getInReplyTo()) {
					MailboxMessageCustomDTO replyMessage = bankmailASC.getMailboxMessage(mailboxName, message.getInReplyTo()
						.getId());

					if (replyMessage.getMessageType().equals(MailMessageType.CONVERSATION)) {
						destination = new ArrayList<MailContactCustomDTO>();
						destination.add(replyMessage.getFrom());
					}

				}

			} else {
					destination = new ArrayList<MailContactCustomDTO>();
					destination.add(message.getTo().get(0));
			}

			PreferredBankerMailboxDelegate preferredBankerMailboxDelegate = new PreferredBankerMailboxDelegate();
			if (BankmailConstants.INT_ZERO < destination.size()) {

				String destinationAddress = destination.get(0).getAddress();

				if (Boolean.TRUE.equals(preferredBankerMailboxDelegate.getMailboxTemplate().isCCAMailboxName(destinationAddress))) {

					cCA = destinationAddress.trim().substring(BankmailConstants.SUBSTR_THIRD_LETTER);

					AccountManagerInfoDTO accountManagerInfoDTO = employeeInfoService.retrieveAccountManagerInfoByCCA(cCA,
						false);

					// If Not found or AccountManagerGroupDTO is returned
					if (null == accountManagerInfoDTO || (accountManagerInfoDTO instanceof AccountManagerGroupDTO)) {
						destination = Collections.emptyList();
					}

				}
			}

			if (BankmailConstants.INT_ZERO == destination.size()) {

				// retrieve sendBankmailPolicy for specified concerned bc number

				SendBankmailPolicyDTO sendBankmailPolicyDTO = null;
				sendBankmailPolicyDTO = bankmailPolicyASC.getSendBankmailPolicy(securityContext, message
					.getConcerningCustomerBCNumber(), true);

				// sendBankmailPolicy doesn't exist throw
				// InvalidAttributeValueException
				if (null == sendBankmailPolicyDTO) {
					LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ASC_INVALID_ATTRIBUTE_VALUE_EXCEPTION, sendBankmailPolicyDTO);
					Messages msgs = new Messages();
					msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_INVALID_ATTRIBUTE_VALUE_EXCEPTION), MessageType.getError());
					throw new InvalidAttributeValueException(msgs);
				}

				// Get destination address for bc from sendBankmailPolicyDTO
				MailContactCustomDTO to = sendBankmailPolicyDTO.getDestination();
				destination = new ArrayList<MailContactCustomDTO>();
				destination.add(to);
			}

			// update destination to message
			message.setTo(destination);
		} catch (EmployeeInfoServiceException employeeInfoServiceException) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.EMPLOYEE_INFO_SERVICE_EXCEPTION, employeeInfoServiceException, cCA);
			Messages msgs = employeeInfoServiceException.getMessages();
			// Throw unexpected exception
			throw new BankmailApplicationException(msgs);
		}
		return message;
	}

	public boolean isHealthy() throws HealthCheckException {
		return true;
	}

	/**
	 * Retrieves the mail message history for an input customer. All mail messages matching input customer =
	 * concerningCustomer are returned. The mail messages are sorted in the descending order of originationDate (from the
	 * most recent to the older ones).
	 * @param securityContext securityContext
	 * @param concerningCustomerBCNumber Concerning Customer BC Number
	 * @return MailMessageListDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public MailContactListDTO getMailContacts(SecurityContext securityContext ,
											  Long concerningCustomerBCNumber)
			throws BankmailApplicationException {
		final String LOG_METHOD = "getMailContacts(securityContext, Long): MailContactListDTO";
		LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_CLIENT_OPERATION_NOT_SUPPORTED_EXCEPTION);
		Messages msgs = new Messages();
		msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_OPERATION_NOT_SUPPORTED_EXCEPTION), MessageType.getError());
		throw new OperationNotSupportedException(msgs);
	}

	/**
	 * Gets the details of a mail message. Only allowed for bank employees.
	 * @param securityContext securityContext
	 * @param id String
	 * @return MailMessageCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public MailMessageCustomDTO getMailMessage(SecurityContext securityContext , String id) throws BankmailApplicationException {
		final String LOG_METHOD = "getMailMessage(securityContext, String): MailMessageCustomDTO";
		LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_CLIENT_OPERATION_NOT_SUPPORTED_EXCEPTION);
		Messages msgs = new Messages();
		msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_OPERATION_NOT_SUPPORTED_EXCEPTION), MessageType.getError());
		throw new OperationNotSupportedException(msgs);
	}

	/**
	 * Retrieves the mail message history for an input customer. All mail messages matching input customer =
	 * concerningCustomer are returned. The mail messages are sorted in the descending order of originationDate (from the
	 * most recent to the older ones).
	 * @param securityContext securityContext
	 * @param concerningCustomerBCNumber Concerning Customer BC Number
	 * @param periodValue period Value as a Integer
	 * @param periodUnit PeriodUnit
	 * @param messageTypes List<MailMessageType>
	 * @return MailMessageListDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public MailMessageListDTO getMailMessageHistory(SecurityContext securityContext , String concerningCustomerBCNumber,
			Integer periodValue, PeriodUnit periodUnit, List<MailMessageType> messageTypes)
			throws BankmailApplicationException {
		final String LOG_METHOD = "getMailMessageHistory(securityContext, String,Integer,PeriodUnit,List<MailMessageType> ): MailMessageListDTO";
		LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_CLIENT_OPERATION_NOT_SUPPORTED_EXCEPTION);
		Messages msgs = new Messages();
		msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_OPERATION_NOT_SUPPORTED_EXCEPTION), MessageType.getError());
		throw new OperationNotSupportedException(msgs);
	}
}