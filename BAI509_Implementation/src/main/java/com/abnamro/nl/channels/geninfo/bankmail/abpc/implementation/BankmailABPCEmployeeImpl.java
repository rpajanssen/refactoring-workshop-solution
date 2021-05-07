package com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.msec.integrationservices.*;
import com.abnamro.msec.integrationservices.services.SecurityAgentFactory;
import com.abnamro.nl.channels.generic.ChannelId;
import com.abnamro.nl.channels.geninfo.bankmail.asc.implementation.*;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.resources.IncludeActions;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailUtil;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.factory.CRMGatewaySyncFactory;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.GatewayApplicationException;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.MessageDTO;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.OnlineGateway;
import com.abnamro.nl.enumeration.*;
import com.abnamro.nl.health.exceptions.HealthCheckException;
import com.abnamro.nl.health.interfaces.HealthCheckable;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageKey;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import com.abnamro.nl.rest.actions.Action;
import com.abnamro.nl.rest.actions.UserActionIndicator;
import com.abnamro.nl.security.util.SecurityAgentUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * BankmailABPCEmployeeImpl BankmailABPCEmployeeImpl Application Business
 * Process Controller Implementation handles all business logic to access
 * mailboxes, manipulate and send messages for employee user.
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  		29-05-2012	Initial version	Release 1.0
 * TCS					03-12-2013	Bankmail++			SyncGateway is renamed to OnlineGateway
 * 																sendMailboxMessage is calling OnlineGateway.asyncPush
 * </PRE>
 * 
 * @author
 * @see
 */

//this file is not implemented from interfaces, as this is related to HAA cluster only, when GHIA
//	stories are in sprint plz implment ffrom bankmail abpc
@LogInterceptorBinding
public class BankmailABPCEmployeeImpl implements HealthCheckable{
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(BankmailABPCEmployeeImpl.class);

	/**
	 * Bankmail General Util class
	 */
	private static BankmailUtil bankmailUtil = new BankmailUtil();

	/**
	 * Retrieves list of Mailboxes which can be accessed by logged in employee.
	 * If includeActions parameter is provided, actions allowed on each mailbox
	 * are returned
	 * 
	 * @param securityContext
	 *            securityContext
	 * @param includeActions
	 *            IncludeActions
	 * @return MailboxListDTO which holds all mail boxes for logged in user.
	 * @throws BankmailApplicationException
	 *             bankmailApplicationException
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public MailboxListDTO getMailboxes(SecurityContext securityContext, IncludeActions includeActions)
			throws BankmailApplicationException, IOException, SAXException {		
		MailboxListDTO mailboxListDTO = null;

		mailboxListDTO = new MailboxListDTO();

		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
		List<EmployeeMailboxCustomDTO> employeeMailboxCustomDTOList = bankmailEmployeeASC.getMailboxes(securityContext);
		List<MailboxCustomDTO> mailboxCustomDTOList = new ArrayList<MailboxCustomDTO>();

		if (null != employeeMailboxCustomDTOList) {
			for (EmployeeMailboxCustomDTO employeeMailboxCustomDTO : employeeMailboxCustomDTOList) {

				MailboxCustomDTO mailboxCustomDTO = new MailboxCustomDTO();
				mailboxCustomDTO.setName(employeeMailboxCustomDTO.getName());
				mailboxCustomDTO.setDisplayName(employeeMailboxCustomDTO.getDisplayName());
				mailboxCustomDTO.setSignature(employeeMailboxCustomDTO.getSignature());

				MailboxGroupCustomDTO group = employeeMailboxCustomDTO.getGroup();
				if (null != group) {
					mailboxCustomDTO.setGroup(group);
				}

				if (null != includeActions) {

					List<Action<MailboxActionName>> actions = new ArrayList<Action<MailboxActionName>>();

					// create action
					Action<MailboxActionName> actionCreate = new Action<MailboxActionName>();
					actionCreate.setName(MailboxActionName.CREATE_MESSAGE);
					actionCreate.setUserActionIndicator(UserActionIndicator.NOT_ALLOWED);

					if (employeeMailboxCustomDTO.getIsCreateAllowed()) {

						actionCreate.setUserActionIndicator(UserActionIndicator.ALLOWED);

					}
					actions.add(actionCreate);

					// send action
					Action<MailboxActionName> actionSend = new Action<MailboxActionName>();
					actionSend.setName(MailboxActionName.SEND_MESSAGE);
					actionSend.setUserActionIndicator(UserActionIndicator.ALLOWED);
					actions.add(actionSend);

					// delete action
					Action<MailboxActionName> actionDelete = new Action<MailboxActionName>();
					actionDelete.setName(MailboxActionName.DELETE_MESSAGES);
					actionDelete.setUserActionIndicator(UserActionIndicator.NOT_ALLOWED);

					if (employeeMailboxCustomDTO.getIsDeleteAllowed()) {

						actionDelete.setUserActionIndicator(UserActionIndicator.ALLOWED);

					}
					actions.add(actionDelete);
					mailboxCustomDTO.setActions(actions);
				}
				mailboxCustomDTOList.add(mailboxCustomDTO);

			}
		}

		mailboxListDTO.setMailboxes(mailboxCustomDTOList);
		return mailboxListDTO;
	}

	/**
	 * Retrieves a mailbox. If includeActions parameter is provided, actions
	 * allowed on the mailbox are returned
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param includeActions
	 *            IncludeActions
	 * @return MailboxCustomDTO mailbox DTO
	 * @throws BankmailApplicationException
	 *             bankmailApplicationException
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public MailboxCustomDTO getMailbox(SecurityContext securityContext, String mailboxName, IncludeActions includeActions)
			throws BankmailApplicationException, IOException, SAXException {
		MailboxCustomDTO mailboxCustomDTO = null;
		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = bankmailEmployeeASC.getMailbox(securityContext, mailboxName);

		mailboxCustomDTO = new MailboxCustomDTO();
		mailboxCustomDTO.setName(employeeMailboxCustomDTO.getName());
		mailboxCustomDTO.setDisplayName(employeeMailboxCustomDTO.getDisplayName());
		mailboxCustomDTO.setIsSubjectSelectable(employeeMailboxCustomDTO.getIsSubjectSelectable());
		mailboxCustomDTO.setSignature(employeeMailboxCustomDTO.getSignature());

		MailboxGroupCustomDTO group = employeeMailboxCustomDTO.getGroup();
		if (null != group) {
			mailboxCustomDTO.setGroup(group);
		}

		if (null != includeActions) {

			List<Action<MailboxActionName>> actions = new ArrayList<Action<MailboxActionName>>();
			Action<MailboxActionName> actionCreate = new Action<MailboxActionName>();
			actionCreate.setName(MailboxActionName.CREATE_MESSAGE);

			// create action
			if (employeeMailboxCustomDTO.getIsCreateAllowed()) {
				actionCreate.setUserActionIndicator(UserActionIndicator.ALLOWED);
			} else {
				actionCreate.setUserActionIndicator(UserActionIndicator.NOT_ALLOWED);
			}

			actions.add(actionCreate);
			// send action
			Action<MailboxActionName> actionSend = new Action<MailboxActionName>();
			actionSend.setName(MailboxActionName.SEND_MESSAGE);
			actionSend.setUserActionIndicator(UserActionIndicator.ALLOWED);
			actions.add(actionSend);

			// delete action
			Action<MailboxActionName> actionDelete = new Action<MailboxActionName>();
			actionDelete.setName(MailboxActionName.DELETE_MESSAGES);
			if (employeeMailboxCustomDTO.getIsDeleteAllowed()) {
				actionDelete.setUserActionIndicator(UserActionIndicator.ALLOWED);

			} else {
				actionDelete.setUserActionIndicator(UserActionIndicator.NOT_ALLOWED);
			}
			actions.add(actionDelete);
			mailboxCustomDTO.setActions(actions);

		}
		return mailboxCustomDTO;
	}

	/**
	 * Saves new message. Only message attributes isSeen, inReplyTo, subject and
	 * content are taken from input. Employee must also choose message
	 * recipient. Reply messages have to have inReplyTo attribute filled in,
	 * only message id has to be provided. New message is created in "Draft"
	 * folder.
	 * 
	 * @param securityContext
	 *            securityContext
	 * @param mailboxName
	 *            String
	 * @param message
	 *            MailboxMessageCustomDTO
	 * @return String String
	 * @throws BankmailApplicationException
	 *             bankmailApplicationException
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public String createMailboxMessage(SecurityContext securityContext, String mailboxName, MailboxMessageCustomDTO message)
			throws BankmailApplicationException, IOException, SAXException {
		final String LOG_METHOD = "createMailboxMessage(securityContext, String, MailboxMessageCustomDTO):String";

		String id = null;
		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
		EmployeeMailboxCustomDTO mailbox = bankmailEmployeeASC.getMailbox(
				securityContext, mailboxName);

		// check for create allowed
		if (!mailbox.getIsCreateAllowed()) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_IS_CREATE_ALLOWED_FALSE);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MAILBOX_ACCESS_DENIED_EXCEPTION), MessageType.getError());
			throw new MailboxAccessDeniedException(msgs);
		}

		// check for Client reach
		bankmailEmployeeASC.checkClientReach(securityContext, new Long(message.getConcerningCustomerBCNumber()));

		// validate Content
		String content = message.getContent();
		if (null != content) {
			bankmailEmployeeASC.validateMessageContent(content);
		}

		BankmailASC bankmailASC = new BankmailASCImpl();

		// From details
		if (null != message.getInReplyTo() && StringUtils.isNotBlank(message.getInReplyTo().getId())) {

			MailboxMessageCustomDTO inReplyToMessageCustomDTO = bankmailASC.getMailboxMessage(mailbox.getName(),
				message.getInReplyTo().getId());
			message.setFrom(inReplyToMessageCustomDTO.getTo().get(0));

		} else {
			MailContactCustomDTO from = new MailContactCustomDTO();
			from.setAddress(mailbox.getName());
			from.setDisplayName(mailbox.getFullDisplayName());
			message.setFrom(from);
		}

		// TO details
		List<MailContactCustomDTO> to = message.getTo();
		LOGGER.debugHardCodedMessage(LOG_METHOD, "to: {0}", to);
		if (null != to) {
			for (MailContactCustomDTO mailContactCustomDTO : to) {

				BankmailClientASC bankmailClientASC = new BankmailClientASCImpl();
				String displayName = bankmailClientASC.getDisplayNameOfAddress(mailContactCustomDTO.getAddress());
				mailContactCustomDTO.setDisplayName(displayName);
			}
		}

		id = bankmailASC.createMailboxMessage(mailbox.getName(), message);
		return id;
	}

	/**
	 * Sends given message. Message has to be saved prior to sending. Message is
	 * removed from the folder after it is sent. During delivery it will be
	 * placed in the "Sent" folder. This operation is restricted to messages in
	 * "Draft" folder.
	 *
	 * @param securityContext
	 *           SecurityContext
	 * @param mailboxName
	 *            String
	 * @param messageId
	 *            String
	 * @throws BankmailApplicationException
	 *             bankmailApplicationException
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public void sendMailboxMessage(SecurityContext securityContext, String mailboxName, String messageId)
			throws BankmailApplicationException, IOException, SAXException {
		final String LOG_METHOD = "sendMailboxMessage(securityContext, String, String ):void";

		try {

			// mailbox
			BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
			EmployeeMailboxCustomDTO mailbox = bankmailEmployeeASC.getMailbox(securityContext, mailboxName);

			// getMessage
			BankmailASC bankmailASC = new BankmailASCImpl();
			MailboxMessageCustomDTO message = bankmailASC.getMailboxMessage(mailbox.getName(), messageId);

			if (BankmailConstants.DRAFT_FOLDER_NAME.equals(message.getFolder().getName())) {
				// Reach Check Needed
				if (mailbox.getIsReachCheckNeeded()) {
					bankmailEmployeeASC.checkClientReach(securityContext, new Long(message.getConcerningCustomerBCNumber()));
				}

				String sender = bankmailUtil.getSenderName(securityContext);

				LOGGER.debugHardCodedMessage(LOG_METHOD, "Sender: {0}", sender);
				// CRM Gateway start
				MessageDTO messageDTO = bankmailUtil.convertMessage(message);
				messageDTO.setSender(sender);
				
				// Archive message
				MessageArchiveASC archiveASC = new MessageArchiveASCImpl();
				archiveASC.archiveMessage(securityContext, messageDTO);
				
				bankmailASC.deliverMailboxMessage(sender, mailbox.getName(), messageId);
				
				EventLogASC eventLogASC = new EventLogASCImpl();
				eventLogASC.logSendMessage(securityContext, message);
				
				// CRMGateway
				OnlineGateway cRMSyncGateway = CRMGatewaySyncFactory.newInstance();				
				cRMSyncGateway.asyncPush(messageId);
				// CRM Gateway end
				
			} else if (!BankmailConstants.SENT_FOLDER_NAME.equals(message.getFolder().getName())) {
				LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_MESSAGE_NOT_FOUND_EXCEPTION, messageId);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MESSAGE_NOT_FOUND_EXCEPTION), MessageType.getError());
				// Throw unexpected exception
				throw new MessageNotFoundException(msgs);
			}
		} catch (GatewayApplicationException gatewayApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_GATEWAY_APPLICATION_EXCEPTION, gatewayApplicationException);
			Messages msgs = gatewayApplicationException.getMessages();
			throw new BankmailApplicationException(msgs);
		}

	}

	/**
	 * Permanently delete messages from the mailbox.
	 * 
	 * @param securityContext
	 *            securityContext
	 * @param mailboxName
	 *            String
	 * @param messageIds
	 *            String[]
	 * @param fromFolder
	 *            String
	 * @throws BankmailApplicationException
	 *             bankmailApplicationException
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public void deleteMailboxMessages(SecurityContext securityContext, String mailboxName, List<String> messageIds,
			String fromFolder) throws BankmailApplicationException, IOException, SAXException {
		final String LOG_METHOD = "deleteMailboxMessages(securityContext ,String ,List<String> , String ):void";

		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
		EmployeeMailboxCustomDTO mailbox = bankmailEmployeeASC.getMailbox(securityContext, mailboxName);

		if (!mailbox.getIsDeleteAllowed()) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_IS_DELETE_ALLOWED_FALSE, mailbox.getIsDeleteAllowed());
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_OPERATION_NOT_SUPPORTED_EXCEPTION), MessageType.getError());
			throw new OperationNotSupportedException(msgs);
		}

		BankmailASC bankmailASC = new BankmailASCImpl();
		bankmailASC.deleteMailboxMessages(mailbox.getName(), messageIds, fromFolder);

		// delete event log
		EventLogASC eventLogASC = new EventLogASCImpl();
		for (String messageId : messageIds) {
			eventLogASC.logDeleteMessage(securityContext, mailbox.getName(), messageId);
		}
	}

	/**
	 * Description Retrieves messages from given folder within given mailbox.
	 * The "to" attribute contains only the first recipient and "subject" only
	 * first 100 characters. Messages can be searched by given text. Messages
	 * can be filtered by MailboxMessageTypes. Result can be sorted by one of
	 * the available sort orders.
	 * @param securityContext securitycontext
	 * @param mailboxName Mailbox name
	 * @param folderName Folder name
	 * @param searchText Search text
	 * @param messageTypes Message types
	 * @param sortBy Sort by parameter
	 * @param pageNumber Page number
	 * @param pageSize Page size
	 * @return MailboxMessageListDTO
	 * @throws BankmailApplicationException Application exception
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public MailboxMessageListDTO getMailboxMessages(SecurityContext securityContext, String mailboxName,
			MailboxFolderName folderName, List<MailMessageType> messageTypes, String searchText, MessagesSortOrder sortBy,
			Integer pageNumber, Integer pageSize) throws BankmailApplicationException, IOException, SAXException {

		final String LOG_METHOD = "getMailboxMessages(securityContext ,String ,"
				+ " MailboxFolderName ,List<MailMessageType> , "
				+ "String ,MessagesSortOrder , int , int ):MailboxMessageListDTO";

		MailboxMessageListDTO mailboxMessageListDTO = null;
		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
		EmployeeMailboxCustomDTO mailbox = bankmailEmployeeASC.getMailbox
				(securityContext, mailboxName);

		if (!mailbox.getIsOverviewAllowed()) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_IS_OVERVIEW_ALLOWED_FALSE, mailbox.getIsOverviewAllowed());
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_OPERATION_NOT_SUPPORTED_EXCEPTION), MessageType.getError());
			throw new MailboxAccessDeniedException(msgs);
		}

		BankmailASC bankmailASC = new BankmailASCImpl();
		List<MailboxMessageCustomDTO> mailboxMessageCustomDTOs = bankmailASC.getMailboxMessages(mailbox.getName(),
			folderName, messageTypes, searchText, sortBy, pageNumber, pageSize);

		mailboxMessageListDTO = new MailboxMessageListDTO();
		mailboxMessageListDTO.setMailMessages(mailboxMessageCustomDTOs);

		return mailboxMessageListDTO;
	}

	/**
	 * Retrieves a message. If employee is logged in then bankmailAddress is
	 * returned in Contact otherwise it is skipped from output. If
	 * includeActions parameter is provided, actions allowed on the message are
	 * returned.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param messageId
	 *            String
	 * @param includeActions
	 *            IncludeActions
	 * @return MailboxMessageCustomDTO mailboxMessageCustomDTO
	 * @throws BankmailApplicationException
	 *             bankmailApplicationException
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public MailboxMessageCustomDTO getMailboxMessage(SecurityContext securityContext, String mailboxName, String messageId,
			IncludeActions includeActions) throws BankmailApplicationException, IOException, SAXException {
		MailboxMessageCustomDTO mailboxMessageCustomDTO = null;

		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
		EmployeeMailboxCustomDTO mailbox = bankmailEmployeeASC.getMailbox(securityContext, mailboxName);

		// getMessage
		BankmailASC bankmailASC = new BankmailASCImpl();
		mailboxMessageCustomDTO = bankmailASC.getMailboxMessage(mailbox.getName(), messageId);

		if (mailbox.getIsReachCheckNeeded()) {

			bankmailEmployeeASC.checkClientReach(securityContext, new Long(mailboxMessageCustomDTO
				.getConcerningCustomerBCNumber()));

		}

		// includeActions
		if (null != includeActions) {

			List<Action<MailboxMessageActionName>> actions = new ArrayList<Action<MailboxMessageActionName>>();

			// Reply action
			Action<MailboxMessageActionName> actionReply = new Action<MailboxMessageActionName>();
			actionReply.setName(MailboxMessageActionName.REPLY);
			actionReply.setUserActionIndicator(UserActionIndicator.NOT_ALLOWED);
			if (mailboxMessageCustomDTO.getIsReplyAllowed() && null != mailboxMessageCustomDTO.getReceivedDate()
					&& mailbox.getIsCreateAllowed()) {

				// if mailbox is reach check needed
				if (mailbox.getIsReachCheckNeeded()) {
					actionReply.setUserActionIndicator(UserActionIndicator.ALLOWED);
				} else {

					if (bankmailEmployeeASC.authorizeClientReach(securityContext, new Long(mailboxMessageCustomDTO
						.getConcerningCustomerBCNumber()))) {
						actionReply.setUserActionIndicator(UserActionIndicator.ALLOWED);
					}
				}
			}
			actions.add(actionReply);

			// Update action
			Action<MailboxMessageActionName> actionUpdate = new Action<MailboxMessageActionName>();
			actionUpdate.setName(MailboxMessageActionName.UPDATE);
			actionUpdate.setUserActionIndicator(UserActionIndicator.ALLOWED);
			actions.add(actionUpdate);

			mailboxMessageCustomDTO.setActions(actions);

		}
		return mailboxMessageCustomDTO;
	}

	/**
	 * Retrieves all preceding messages in the reply messages chain. Only
	 * messages available in the folders "Inbox", "Archive" and "Sent" are
	 * returned. Messages are returned in reversed chronological order (latest
	 * first). If message has receivedDate (received messages only), then it is
	 * used for sorting, otherwise originationDate is used.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            String
	 * @param messageId
	 *            String
	 * @return MailboxMessageListDTO mailboxMessageListDTO
	 * @throws BankmailApplicationException Application exception
	 */
	public MailboxMessageListDTO getMailboxMessageThread(SecurityContext securityContext, String mailboxName, String messageId)
			throws BankmailApplicationException {
		final String LOG_METHOD = "getMailboxMessageThread(securityContext,String ,String):MailboxMessageListDTO";
		LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_EMP_OPERATION_NOT_SUPPORTED_EXCEPTION);
		Messages msgs = new Messages();
		msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_OPERATION_NOT_SUPPORTED_EXCEPTION), MessageType.getError());
		throw new OperationNotSupportedException(msgs);
	}
	/**
	 * Updates given message. Only "isSeen" attribute can be set to "true". All
	 * other changes will be ignored. For messages in "Draft" folder subject and
	 * content can be updated. Employee can also change message recipient.
	 * 
	 * @param securityContext
	 *            securityContext
	 * @param mailboxName
	 *            String
	 * @param message
	 *            MailboxMessageCustomDTO
	 * @return String String
	 * @throws BankmailApplicationException
	 *             bankmailApplicationException
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public String updateMailboxMessage(SecurityContext securityContext, String mailboxName, MailboxMessageCustomDTO message)
			throws BankmailApplicationException, IOException, SAXException {
		final String LOG_METHOD = "updateMailboxMessage(securityContext, String, MailboxMessageCustomDTO):String";
		// id
		String id = null;
		try {

			// mailbox
			BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
			EmployeeMailboxCustomDTO mailbox = bankmailEmployeeASC.getMailbox(securityContext, mailboxName);

			if (StringUtils.isBlank(message.getId())) {
				LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_MESSAGE_ID_NOT_PROVIDED);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_INVALID_ATTRIBUTE_VALUE_EXCEPTION), MessageType.getError());
				// Throw exception
				throw new InvalidAttributeValueException(msgs);
			}

			// getMessage
			BankmailASC bankmailASC = new BankmailASCImpl();
			MailboxMessageCustomDTO oldMessage = bankmailASC.getMailboxMessage(mailbox.getName(), message.getId());

			// Reach Check Needed
			if (Boolean.TRUE.equals(mailbox.getIsReachCheckNeeded())) {
				bankmailEmployeeASC.checkClientReach(securityContext, new Long(oldMessage.getConcerningCustomerBCNumber()));
			}

			// validate Content
			String content = message.getContent();
			if (null != content) {
				bankmailEmployeeASC.validateMessageContent(content);
			}

			MailboxMessageCustomDTO newMessage = new MailboxMessageCustomDTO();

			try {
				PropertyUtils.copyProperties(newMessage, oldMessage);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			// isSeen update check
			if (null != message.getIsSeen()) {
				newMessage.setIsSeen(message.getIsSeen());
			}
			// subject update check
			if (null != message.getSubject()) {
				newMessage.setSubject(message.getSubject());
			}
			// content update check
			if (null != message.getContent()) {
				newMessage.setContent(message.getContent());
			}

			// to update check
			if (null != message.getTo()) {

				// old recipient
				List<MailContactCustomDTO> oldTo = oldMessage.getTo();
				// HashMap<K, V>; K = address & V = displayName
				HashMap<String, String> oldRecipientsMap = new HashMap<String, String>(oldTo.size());
				for (MailContactCustomDTO mailContactCustomDTO : oldTo) {
					oldRecipientsMap.put(mailContactCustomDTO.getAddress(), mailContactCustomDTO.getDisplayName());
				}

				List<MailContactCustomDTO> messageTo = message.getTo();
				List<MailContactCustomDTO> newTo = new ArrayList<MailContactCustomDTO>();
				BankmailClientASC bankmailClientASC = new BankmailClientASCImpl();

				for (MailContactCustomDTO messageContact : messageTo) {

					String address = messageContact.getAddress();
					String displayName = null;

					if (oldRecipientsMap.containsKey(address)) {
						displayName = oldRecipientsMap.get(address);
					} else {
						displayName = bankmailClientASC.getDisplayNameOfAddress(address);
					}
					// set display name
					messageContact.setDisplayName(displayName);

					newTo.add(messageContact);

				}
				// set new contact list
				newMessage.setTo(newTo);
			}

			id = bankmailASC.updateMailboxMessage(mailbox.getName(), oldMessage, newMessage);

			// log open message event
			if (null != message.getIsSeen() && message.getIsSeen()) {
				EventLogASC eventLogASC = new EventLogASCImpl();
				eventLogASC.logOpenMessage(securityContext, mailbox.getName(), message.getId());
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
	 * 
	 * @param securityContext
	 *            securityContext
	 * @param mailboxName
	 *            String
	 * @return FolderListDTO list of folders in a mail as ListDTO
	 * @throws BankmailApplicationException
	 *             bankmailApplicationException
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public FolderListDTO getFolders(SecurityContext securityContext, String mailboxName) throws BankmailApplicationException, IOException, SAXException {
		FolderListDTO folderListDTO = null;
		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = bankmailEmployeeASC.getMailbox(securityContext, mailboxName);

		BankmailASC bankmailASC = new BankmailASCImpl();
		folderListDTO = new FolderListDTO();
		folderListDTO.setFolders(bankmailASC.getFolders(employeeMailboxCustomDTO.getName()));
		return folderListDTO;
	}

	/**
	 * Retrieves a folder from given mailbox.
	 * 
	 * @param securityContext
	 *            securityContext
	 * @param mailboxName
	 *            mailboxName
	 * @param folderName
	 *            MailboxFolderName
	 * @return FolderCustomDTO folderCustomDTO
	 * @throws BankmailApplicationException
	 *             bankmailApplicationException	 *          
	 * @throws IOException
	 *             ioException
	 * @throws SAXException
	 *             saxException
	 */
	public FolderCustomDTO getFolder(SecurityContext securityContext, String mailboxName, MailboxFolderName folderName)
			throws BankmailApplicationException, IOException, SAXException {
		FolderCustomDTO folderCustomDTO = null;
		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = bankmailEmployeeASC.getMailbox(securityContext, mailboxName);

		BankmailASC bankmailASC = new BankmailASCImpl();
		folderCustomDTO = bankmailASC.getFolder(employeeMailboxCustomDTO.getName(), folderName);
		return folderCustomDTO;
	}

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
	 * @throws BankmailApplicationException
	 *             BankmailApplicationException
	 */
	public SendBankmailPolicyListDTO getSendBankmailPolicies(SecurityContext securityContext) throws BankmailApplicationException {
		final String LOG_METHOD = "getSendBankmailPolicies(SecurityContext):SendBankmailPolicyListDTO";
		LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ABPC_EMP_OPERATION_NOT_SUPPORTED_EXCEPTION);
		Messages msgs = new Messages();
		msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_OPERATION_NOT_SUPPORTED_EXCEPTION), MessageType.getError());
		throw new OperationNotSupportedException(msgs);
	}
	/**
	 * Description Retrieves a list of mailContacts for customers which
	 * can be selected by logged in user in the attribute "concerningCustomer"
	 * in new messages.
	 *
	 * @param securityContext
	 *            SecurityContext
	 * @param concerningCustomerBCNumber
	 *            Long
	 * @return mailContactListDTO MailContactListDTO
	 * @throws BankmailApplicationException
	 *             BankmailApplicationException
	 */
	public MailContactListDTO getMailContacts(SecurityContext securityContext, Long concerningCustomerBCNumber)
			throws BankmailApplicationException {
		final String LOG_METHOD = "getMailContacts(securityContext,Long):MailContactListDTO";
		MailContactListDTO mailContactListDTO = null;

		try {
			// check client reach
			BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();
			bankmailEmployeeASC.checkClientReach(securityContext, concerningCustomerBCNumber);

			SecurityAgent securityAgent = SecurityAgentFactory.getSecurityAgent();

			// reach Subject
			ReachSubject reachSubject = new ReachSubject();
			reachSubject.setSubjectClass(ReachSubject.REACH_SUBJECT_CLASS_BC);
			reachSubject.setSubjectReference(concerningCustomerBCNumber.toString());

			// Bankmail++ changes: calling getUsersAuthorisedForSubjectUsingChannel istead getUserForSubject
			UsersAuthorisedForSubjectUsingChannelResult userAuthorisedForSubjectUsingChannelResult = SecurityAgentUtils
				.getUsersAuthorisedForSubjectUsingChannel(securityAgent, BankmailConstants.MSEC_APP_NAME, securityContext
					.getSessionSpecification(),
						//check
						String.valueOf(securityContext.getEndFunctions()),
						reachSubject, ChannelId.INTERNET);

			if (userAuthorisedForSubjectUsingChannelResult == null) {

				BankmailUtil.logAndThrowErrorMessage(LOGGER, null,
					BankmailABPCLogMessages.LOG_ERROR_USER_AUTHORISED_FOR_SUBJECT, new String[] { "null",
							//securityContext.getEndFunction()
								//check
								String.valueOf(securityContext.getEndFunctions())
						}, BankmailABPCMessageKeys.ERROR_USER_AUTHORISED_FOR_SUBJECT,
					LOG_METHOD);

			} else if (userAuthorisedForSubjectUsingChannelResult.getResultCode() != UsersAuthorisedForSubjectUsingChannelResult.SUCCESS) {

				MessageKey mSecMessageKey = SecurityAgentUtils.getMessageKey(
					UsersAuthorisedForSubjectUsingChannelResult.class, userAuthorisedForSubjectUsingChannelResult
						.getResultCode());

				BankmailUtil.logAndThrowErrorMessage(LOGGER, null,
					BankmailABPCLogMessages.LOG_ERROR_USER_AUTHORISED_FOR_SUBJECT, new Object[] {
							userAuthorisedForSubjectUsingChannelResult.getResultCode(),
								//securityContext.getEndFunction()
								//check
								securityContext.getEndFunctions()
						},
					mSecMessageKey, LOG_METHOD);
			}

			UserStatus[] users = null;
			if(userAuthorisedForSubjectUsingChannelResult!=null) {
			users = userAuthorisedForSubjectUsingChannelResult.getUserList();
			}
			if (null != users) {

				List<MailContactCustomDTO> mailContactList = new ArrayList<MailContactCustomDTO>();
				BankmailClientASC bankmailClientASC = new BankmailClientASCImpl();

				for (UserStatus userStatus : users) {

					MailContactCustomDTO mailContactCustomDTO = null;

					if (UserId.USER_CLASS_BC.equalsIgnoreCase(userStatus.getUserClass())) {

						mailContactCustomDTO = new MailContactCustomDTO();
						String address = BankmailConstants.BC_MAILBOX_NAME_PREFIX + userStatus.getUserReference();
						mailContactCustomDTO.setAddress(address);
						mailContactCustomDTO.setDisplayName(bankmailClientASC.getDisplayNameOfAddress(address));
						mailContactList.add(mailContactCustomDTO);

					} else if (UserId.USER_CLASS_AT.equalsIgnoreCase(userStatus.getUserClass())) {

						mailContactCustomDTO = new MailContactCustomDTO();
						String address = BankmailConstants.AT_MAILBOX_NAME_PREFIX + userStatus.getUserReference();
						mailContactCustomDTO.setAddress(address);
						mailContactCustomDTO.setDisplayName(bankmailClientASC.getDisplayNameOfAddress(address));
						mailContactList.add(mailContactCustomDTO);
					}
				}

				mailContactListDTO = new MailContactListDTO();
				mailContactListDTO.setMailContactCustomDTOs(mailContactList);
			}
		} catch (MSecException mSecException) {
			LOGGER.error(LOG_METHOD, BankmailABPCLogMessages.LOG_ASC_EMPLOYEE_MSEC_EXCEPTION, mSecException, securityContext.getSessionSpecification(), mSecException.getErrorCode());
			Messages myMessages = new Messages();
			myMessages.addMessage(new Message(SecurityAgentUtils.getMessageKey(
				UsersAuthorisedForSubjectUsingChannelResult.class, mSecException.getErrorCode())), MessageType.getError());
			throw new BankmailApplicationException(myMessages);
		}

		return mailContactListDTO;
	}
	/**
	 * Description Retrieves a list of mailMessage for customers based on the id.
	 *
	 * @param securityContext
	 *            SecurityContext
	 * @param id String
	 * @return mailMessageCustomDTO MailMessageCustomDTO
	 * @throws BankmailApplicationException
	 *             BankmailApplicationException
	 */
	public MailMessageCustomDTO getMailMessage(SecurityContext securityContext, String id) throws BankmailApplicationException {
		MailMessageCustomDTO mailMessageCustomDTO = null;
		// getMessage
		BankmailASC bankmailASC = new BankmailASCImpl();
		mailMessageCustomDTO = bankmailASC.getMailMessage(id);

		// check client reach
		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();

		bankmailEmployeeASC.checkClientReach(securityContext,
			new Long(mailMessageCustomDTO.getConcerningCustomerBCNumber()));
		return mailMessageCustomDTO;
	}
	/**
	 * Description Retrieves a list of mailContacts for customers which
	 * can be selected by logged in user in the attribute "concerningCustomer"
	 * in new messages.
	 *
	 * @param securityContext
	 *            SecurityContext
	 * @param concerningCustomerBCNumber String
	 * @param periodValue Integer
	 * @param periodUnit PeriodUnit
	 * @param messageTypes  List<MailMessageType>
	 * @return mailMessageListDTO MailMessageListDTO
	 * @throws BankmailApplicationException
	 *             BankmailApplicationException
	 */
	public MailMessageListDTO getMailMessageHistory(SecurityContext securityContext, String concerningCustomerBCNumber,
			Integer periodValue, PeriodUnit periodUnit, List<MailMessageType> messageTypes)
			throws BankmailApplicationException {
		MailMessageListDTO mailMessageListDTO = null;
		mailMessageListDTO = new MailMessageListDTO();

		// check client reach
		BankmailEmployeeASC bankmailEmployeeASC = new BankmailEmployeeASCImpl();

		bankmailEmployeeASC.checkClientReach(securityContext, new Long(concerningCustomerBCNumber));

		// getMessage
		BankmailASC bankmailASC = new BankmailASCImpl();
		List<MailMessageCustomDTO> mailMessageCustomDTOs = bankmailASC.getMailMessages(concerningCustomerBCNumber,
			periodValue, periodUnit, messageTypes);

		mailMessageListDTO.setMailMessages(mailMessageCustomDTOs);
		return mailMessageListDTO;
	}
	/**
	 * Description Retrieves a list of mailContacts for customers which
	 * can be selected by logged in user in the attribute "concerningCustomer"
	 * in new messages.
	 *
	 * @return isHealthy boolean
	 * @throws HealthCheckException
	 *             HealthCheckException
	 */
	public boolean isHealthy() throws HealthCheckException {
		return new BankmailASCImpl().isHealthy();
	}

}