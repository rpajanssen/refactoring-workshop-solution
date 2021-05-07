package com.abnamro.nl.channels.geninfo.bankmail.asc.implementation;

import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailASC;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailUtil;
import com.abnamro.nl.channels.service.mailbox.implementation.MailboxServiceImpl;
import com.abnamro.nl.channels.service.mailbox.interfaces.*;
import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.enumeration.MailboxFolderName;
import com.abnamro.nl.enumeration.MessagesSortOrder;
import com.abnamro.nl.enumeration.PeriodUnit;
import com.abnamro.nl.enumeration.util.EnumException;
import com.abnamro.nl.exceptions.ConstraintsViolatedException;
import com.abnamro.nl.health.exceptions.HealthCheckException;
import com.abnamro.nl.health.interfaces.HealthCheckable;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * BankmailASC Bankmail 'Application Specific Controller' handles all business logic to access mailboxes, manipulate and
 * send messages.
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
@LogInterceptorBinding
public class BankmailASCImpl implements BankmailASC, HealthCheckable {
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(BankmailASCImpl.class);

	/**
	 * Bankmail General Util class
	 */
	private static BankmailUtil bankmailUtil = new BankmailUtil();

	// getMessageThread allowed folder list.
	private static final ArrayList<String> MESSAGE_THREAD_FOLDER_LIST = new ArrayList<String>();

	/**
	 * getFolders allowed folder list.
	 */
	private static final ArrayList<String> FOLDER_LIST = new ArrayList<String>();

	static {
		MESSAGE_THREAD_FOLDER_LIST.add("INBOX");
		MESSAGE_THREAD_FOLDER_LIST.add("SENT");
		MESSAGE_THREAD_FOLDER_LIST.add("ARCHIVE");
		FOLDER_LIST.add("INBOX");
		FOLDER_LIST.add("SENT");
		FOLDER_LIST.add("DRAFT");
	}

	/**
	 * Saves new message. Only message attributes isSeen, inReplyTo, subject and content are taken from input. Attributes
	 * isSeen, subject and content are required. Employee must also choose message recipient. Reply messages have to have
	 * inReplyTo attribute filled in, only message id has to be provided. New message is created in "Draft" folder.
	 * @param mailboxName name of the mailbox
	 * @param message MailboxMessageCustomDTO which holds message information
	 * @return String message id
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws ConstraintsViolatedException constraintsViolatedException
	 */
	public String createMailboxMessage(String mailboxName, MailboxMessageCustomDTO message)
			throws BankmailApplicationException {
		final String logMethod = "createMailboxMessage(String,MailboxMessageCustomDTO): String";
		String id = null;
		try {
			// copy Data
			MailboxMessageDTO mailboxMessageDTO = new MailboxMessageDTO();

			// from
			ContactDTO from = new ContactDTO();
			from.setBankmailAddress(message.getFrom().getAddress());
			from.setName(message.getFrom().getDisplayName());
			mailboxMessageDTO.setFrom(from);

			// recipients
			List<MailContactCustomDTO> orgTo = message.getTo();

			if (null != orgTo && orgTo.size() > 0) {

				List<ContactDTO> to = new ArrayList<ContactDTO>();
				ContactDTO contactDTO = null;
				for (MailContactCustomDTO customContact : orgTo) {
					contactDTO = new ContactDTO();
					contactDTO.setBankmailAddress(customContact.getAddress());
					contactDTO.setName(customContact.getDisplayName());
					to.add(contactDTO);
				}
				mailboxMessageDTO.setTo(to);
			} else {
				LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_RECIPIENT_NOT_PROVIDED);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_RECIPIENT_NOT_PROVIDED), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}

			// setSeen
			if (null != message.getIsSeen()) {
				mailboxMessageDTO.setSeen(message.getIsSeen());
			}

			// Only message id has to be provided when inReplyTo available
			if (null != message.getInReplyTo() && StringUtils.isNotBlank(message.getInReplyTo().getId())) {
				MailboxMessageDTO inReplyTo = new MailboxMessageDTO();
				inReplyTo.setId(message.getInReplyTo().getId());
				mailboxMessageDTO.setInReplyTo(inReplyTo);
			}

			// Concerning BC
			mailboxMessageDTO.setConcerningCustomerBCNumber(message.getConcerningCustomerBCNumber());

			mailboxMessageDTO.setSubject(message.getSubject());
			mailboxMessageDTO.setContent(message.getContent());

			// Message Type
			mailboxMessageDTO.setMessageType(MailMessageType.CONVERSATION.getCode());
			mailboxMessageDTO.setReplyAllowed(true);

			MailboxServiceImpl mailboxService=new MailboxServiceImpl();

			id = mailboxService.createMailboxMessage(mailboxName, mailboxMessageDTO);

		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		}
		return id;
	}

	/**
	 * Delivers message to Bankmail mailboxes. Precondition: message must exist and be in the "DRAFT" folder. Sender of
	 * the message is set to the current user, originationDate to the current system time. Message is moved to "SENT"
	 * folder and if it is a reply message, the replied message gets isAnswered = TRUE.
	 * @param senderName String
	 * @param mailboxName String of mailbox name
	 * @param messageId String of message id
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public void deliverMailboxMessage(String senderName, String mailboxName, String messageId)
			throws BankmailApplicationException {
		final String logMethod = "deliverMailboxMessage(String,String,String):void";

		MailboxServiceImpl mailboxService=new MailboxServiceImpl();


		try {

			mailboxService.deliverMailboxMessage(senderName, mailboxName, messageId);
		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		}
	}

	/**
	 * Deletes messages from a mailbox.
	 * @param mailboxName String of mailbox name
	 * @param messageIds String of message ids
	 * @param fromFolder from Folder name
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public void deleteMailboxMessages(String mailboxName, List<String> messageIds, String fromFolder)
			throws BankmailApplicationException {
		final String logMethod = "deleteMailboxMessages(String, List<String> , String): void";

		try {
			MailboxServiceImpl mailboxService=new MailboxServiceImpl();
			mailboxService.deleteMailboxMessages(mailboxName, fromFolder, messageIds);

		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		}

	}

	/**
	 * Retrieves all folders of a mailbox.
	 * @param mailboxName String
	 * @return FolderCustomDTO[] list of folderCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public List<FolderCustomDTO> getFolders(String mailboxName) throws BankmailApplicationException {
		final String logMethod = "getFolders(String):List<FolderCustomDTO>";

		List<FolderCustomDTO> folderCustomDTOs = null;
		try {

			// Retrieve mailbox folders using BSI109 service.
			MailboxServiceImpl mailboxService=new MailboxServiceImpl();

			/**
			 * Fix for get all folders : Current BAI design says that ASC layer has to return INBOX, SENT and DRAFT
			 * folders. Hence hard-coding it and once the list is updated (with REMOVED, ARCHIVE folders for example) then
			 * this method in the BAI has also to be changed.
			 */

			List<FolderDTO> folderDTOs = mailboxService.getFolders(mailboxName);

			folderCustomDTOs = new ArrayList<FolderCustomDTO>();
			FolderCustomDTO folderCustomDTO = null;

			if (null != folderDTOs) {

				for (int count = 0; count < FOLDER_LIST.size(); count++) {

					folderCustomDTO = new FolderCustomDTO();
					folderCustomDTO.setName(FOLDER_LIST.get(count));
					folderCustomDTO.setUnseenMessagesCount(0);
					for (FolderDTO folderDTOUnit : folderDTOs) {

						if (folderDTOUnit.getName().equals(folderCustomDTO.getName())) {
							folderCustomDTO.setUnseenMessagesCount(folderDTOUnit.getUnseenMessagesCount());
							break;
						}
					}
					folderCustomDTOs.add(folderCustomDTO);
				}
			} else {
				for (int count = 0; count < FOLDER_LIST.size(); count++) {
					folderCustomDTO = new FolderCustomDTO();
					folderCustomDTO.setName(FOLDER_LIST.get(count));
					folderCustomDTO.setUnseenMessagesCount(0);
					folderCustomDTOs.add(folderCustomDTO);
				}
			}
		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		}
		return folderCustomDTOs;
	}

	/**
	 * Retrieves a folder from given mailbox.
	 * @param mailboxName String
	 * @param folderName MailboxFolderName
	 * @return FolderCustomDTO folderCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public FolderCustomDTO getFolder(String mailboxName, MailboxFolderName folderName)
			throws BankmailApplicationException {

		final String logMethod = "getFolder(String, MailboxFolderName): FolderCustomDTO";
		FolderCustomDTO folderCustomDTO = null;
		try {

			// Retrieve mailbox folder using BSI109 service.
			MailboxServiceImpl mailboxService=new MailboxServiceImpl();

			FolderDTO folderDTO = mailboxService.getFolder(mailboxName, folderName.getCode());

			if (null == folderDTO) {
				LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_FOLDER_NAME_NOT_FOUND);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_FOLDER_NAME_NOT_FOUND), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			} else {
				folderCustomDTO = new FolderCustomDTO();
				folderCustomDTO.setName(folderDTO.getName());
				folderCustomDTO.setUnseenMessagesCount(folderDTO.getUnseenMessagesCount());
				folderCustomDTO.setUnseenAlertMessagesCount(folderDTO.getUnseenAlertMessagesCount());
			}
		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		}
		return folderCustomDTO;
	}

	/**
	 * Retrieves messages from given folder within given mailbox. If employee is logged in then bankmailAddress is
	 * returned in Contact otherwise it is skipped from output. Messages can be searched by given text. Messages can be
	 * filtered by MailboxMessageTypes. Result can be sorted by one of the available sort orders. Result can be paged.
	 * @param mailboxName String
	 * @param folderName MailboxFolderName
	 * @param messageTypes MailboxFolderName[]
	 * @param searchText String
	 * @param sortBy MessagesSortOrder
	 * @param pageNumber int
	 * @param pageSize int
	 * @return MailboxMessageCustomDTO[] mailboxMessageCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public List<MailboxMessageCustomDTO> getMailboxMessages(String mailboxName, MailboxFolderName folderName,
			List<MailMessageType> messageTypes, String searchText, MessagesSortOrder sortBy, int pageNumber, int pageSize)
			throws BankmailApplicationException {

		final String logMethod = "getMailboxMessages(securityContext,String,MailboxFolderName,"
				+ "List<MailMessageType>, String ,MessagesSortOrder , int , " + "int ):List<MailboxMessageCustomDTO>";

		List<MailboxMessageCustomDTO> mailboxMessageCustomDTOs = null;

		try {

			// Convert message type enum[] to int[]
			int[] serviceMessageTypes = null;
			if (messageTypes != null) {
				serviceMessageTypes = new int[messageTypes.size()];
				int i = 0;
				for (MailMessageType mailMessageType : messageTypes) {
					serviceMessageTypes[i] = mailMessageType.getCode();
					i++;
				}
			}

			// set value of enum MessagesSortOrder
			com.abnamro.nl.channels.service.mailbox.interfaces.MessagesSortOrder serviceSortBy = null;
			if (null != sortBy && StringUtils.isNotEmpty(sortBy.getCode())) {
				serviceSortBy = com.abnamro.nl.channels.service.mailbox.interfaces.MessagesSortOrder.fromString(sortBy
					.getCode());
			}

			MailboxServiceImpl mailboxService=new MailboxServiceImpl();
			List<MailboxMessageDTO> mailboxMessageDTOs = mailboxService.getMailboxMessages(mailboxName,
				folderName.getCode(), searchText, serviceMessageTypes, serviceSortBy, pageNumber, pageSize);

			// copy service DTO data to custom DTO.
			mailboxMessageCustomDTOs = bankmailUtil.convertMessageList(mailboxMessageDTOs, false);

		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		} catch (EnumException enumException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ENUM_EXCEPTION, enumException);
			Messages messages = new Messages();
			messages.addMessage(new Message(BankmailABPCMessageKeys.ERROR_ENUM_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(messages);
		}
		return mailboxMessageCustomDTOs;
	}

	/**
	 * Retrieves a message. If employee is logged in then bankmailAddress is returned in Contact otherwise it is skipped
	 * from output. If includeActions parameter is provided, actions allowed on the message are returned.
	 * @param mailboxName String
	 * @param messageId String
	 * @return MailboxMessageCustomDTO mailboxMessageCustomDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public MailboxMessageCustomDTO getMailboxMessage(String mailboxName, String messageId)
			throws BankmailApplicationException, MessageNotFoundException {

		final String logMethod = "getMailboxMessage(String,String):MailboxMessageCustomDTO";

		try {

			MailboxServiceImpl mailboxService=new MailboxServiceImpl();

			MailboxMessageDTO mailboxMessageDTO = mailboxService.getMailboxMessage(mailboxName, messageId);

			MailboxMessageCustomDTO mailboxMessageCustomDTO = bankmailUtil.convertMessage(mailboxMessageDTO);
			return mailboxMessageCustomDTO;

		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		}

	}

	/**
	 * Retrieves all preceding messages in the reply messages chain. Only messages available in the folders "Inbox",
	 * "Archive" and "Sent" are returned. Messages are returned in reversed chronological order (latest first). If
	 * message has receivedDate (received messages only), then it is used for sorting, otherwise originationDate is used.
	 * @param mailboxName String of mailbox name
	 * @param messageId String of message Id
	 * @return MailboxMessageListDTO holds list of messages in same conversation
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws MessageNotFoundException messageNotFoundException thrown when message is not available
	 */
	public List<MailboxMessageCustomDTO> getMailboxMessageThread(String mailboxName, String messageId)
			throws MessageNotFoundException, BankmailApplicationException {

		final String logMethod = "getMailboxMessageThread(String,String):List<MailboxMessageCustomDTO> ";

		try {

			MailboxServiceImpl mailboxService=new MailboxServiceImpl();

			MailboxMessageDTO mailboxMessageDTO = mailboxService.getMailboxMessage(mailboxName, messageId);

			if (null != mailboxMessageDTO && StringUtils.isNotBlank(mailboxMessageDTO.getId())) {

				String folderName = mailboxMessageDTO.getFolder().getName();

				if (MESSAGE_THREAD_FOLDER_LIST.contains(folderName)) {

					List<MailboxMessageDTO> mailboxMessageDTOs = mailboxService.getMailboxMessageConversation(mailboxName,
						mailboxMessageDTO.getConversationId(), mailboxMessageDTO.getOriginationTimestamp());

					// copy service DTO data to custom DTO.
					List<MailboxMessageCustomDTO> mailboxMessageCustomDTOs = bankmailUtil.convertMessageList(
						mailboxMessageDTOs, true);

					return mailboxMessageCustomDTOs;

				} else {
					// invalid folder name provide for getMessageThread
					// operation
					LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_INVALID_FOLDER_NAME_GET_MESSAGE_THREAD,
						folderName);
					Messages msgs = new Messages();
					msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_INVALID_FOLDER_NAME_GET_MESSAGE_THREAD),
						MessageType.getError());
					// Throw unexpected exception
					throw new BankmailApplicationException(msgs);
				}

			} else {
				// Message not Found exception thrown
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MESSAGE_NOT_FOUND_EXCEPTION),
					MessageType.getError());
				// Throw unexpected exception
				throw new MessageNotFoundException(msgs);
			}

		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailASC#updateMailboxMessage(java.lang.String,
	 * com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxMessageCustomDTO,
	 * com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxMessageCustomDTO)
	 */
	/**
	 * Updates given message. Only "isSeen" attribute can be set to "true". All other changes will be ignored. For
	 * messages in "Draft" folder subject and content can be updated. Employee can also change message recipient.
	 * @param mailboxName mailbox name
	 * @param oldMessage MailboxMessageCustomDTO to be updated
	 * @param newMessage MailboxMessageCustomDTO with message details to be updated
	 * @return String message id of updated message
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public String updateMailboxMessage(String mailboxName, MailboxMessageCustomDTO oldMessage,
			MailboxMessageCustomDTO newMessage) throws BankmailApplicationException {
		final String logMethod = "updateMailboxMessage(MailboxMessageCustomDTO," + "MailboxMessageCustomDTO):String";

		MailboxServiceImpl mailboxService=new MailboxServiceImpl();
		String id = null;
		try {

			id = oldMessage.getId();

			MailboxMessageDTO oldServiceMessage = mailboxService.getMailboxMessage(mailboxName, oldMessage.getId());
			MailboxMessageDTO newServiceMessage = new MailboxMessageDTO();

			try {
				PropertyUtils.copyProperties(newServiceMessage, oldServiceMessage);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			if (null != newMessage.getContent()) {
				newServiceMessage.setContent(newMessage.getContent());
			}
			if (null != newMessage.getSubject()) {
				newServiceMessage.setSubject(newMessage.getSubject());
			}
			if (null != newMessage.getIsSeen()) {
				newServiceMessage.setSeen(newMessage.getIsSeen());
			}


			if (null != newMessage.getTo() && newMessage.getTo().size() > 0) {

				List<MailContactCustomDTO> custumTo = newMessage.getTo();
				List<ContactDTO> to = new ArrayList<ContactDTO>();

				for (MailContactCustomDTO mailContactCustomDTO : custumTo) {
					ContactDTO contactDTO = new ContactDTO();
					contactDTO.setBankmailAddress(mailContactCustomDTO.getAddress());
					contactDTO.setName(mailContactCustomDTO.getDisplayName());
					to.add(contactDTO);
				}

				if (to.size() > 0) {
					newServiceMessage.setTo(to);
				}
			}

			mailboxService.updateMailboxMessage(mailboxName, oldServiceMessage, newServiceMessage);
			return id;
		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		} catch (IllegalAccessException illegalAccessException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ILLEGAL_ACCESS_EXCEPTION, illegalAccessException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_ILLEGAL_ACCESS_EXCEPTION), MessageType.getError());
			// Throw unexpected exception
			throw new BankmailApplicationException(msgs);
		} catch (InvocationTargetException invocationTargetException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_INVOCATION_TARGET_EXCEPTION, invocationTargetException);
			Messages msgs = new Messages();
			msgs
				.addMessage(new Message(BankmailABPCMessageKeys.ERROR_INVOCATION_TARGET_EXCEPTION), MessageType.getError());
			// Throw unexpected exception
			throw new BankmailApplicationException(msgs);
		}
	}

	public boolean isHealthy() throws HealthCheckException {
		return true;
	}
	
	/**
	 * Gets the details of a mail message.
	 * @param messageId String message id
	 * @return MailMessageCustomDTO
	 */
	public MailMessageCustomDTO getMailMessage(String messageId) throws BankmailApplicationException {
		final String logMethod = "getMailMessage(String):MailMessageCustomDTO";
		MailMessageCustomDTO mailMessageCustomDTO = null;
		try {

			MailboxServiceImpl mailboxService=new MailboxServiceImpl();
			MailMessageDTO mailMessageDTO = mailboxService.getMailMessage(messageId);

			mailMessageCustomDTO = bankmailUtil.convertMailMessage(mailMessageDTO);
			return mailMessageCustomDTO;

		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		}
	}
	
	/**
	 * Retrieves the mail message history for an input customer. All mail messages matching input customer =
	 * concerningCustomer are returned. The mail messages are sorted in the descending order of originationDate (from the
	 * most recent to the older ones).
	 * @param concerningCustomerBCNumber String BC Number
	 * @param periodValue Integer
	 * @param periodUnit PeriodUnit
	 * @param messageTypes MailMessageType[]
	 * @return MailMessageCustomDTO[]
	 */
	public List<MailMessageCustomDTO> getMailMessages(String concerningCustomerBCNumber, Integer periodValue,
			PeriodUnit periodUnit, List<MailMessageType> messageTypes) throws BankmailApplicationException {
		final String logMethod = "getMailMessages(String,Integer,PeriodUnit,"
				+ "List<MailMessageType>): List<MailMessageCustomDTO>";

		List<MailMessageCustomDTO> mailMessageCustomDTOs = null;
		try {

			// Convert message type enum[] to int[]
			int[] serviceMessageTypes = null;
			if (messageTypes != null) {
				serviceMessageTypes = new int[messageTypes.size()];
				int i = 0;
				for (MailMessageType mailMessageType : messageTypes) {
					serviceMessageTypes[i] = mailMessageType.getCode();
					i++;
				}
			}

			MailboxServiceImpl mailboxService=new MailboxServiceImpl();
			List<MailMessageDTO> mailMessageDTOList = mailboxService.getMailMessages(concerningCustomerBCNumber,
				periodValue, periodUnit, serviceMessageTypes);

			mailMessageCustomDTOs = bankmailUtil.convertMailMessageList(mailMessageDTOList);

			return mailMessageCustomDTOs;

		} catch (MailboxServiceException mailboxServiceException) {
			LOGGER.error(logMethod, BankmailASCLogConstants.LOG_ASC_MAILBOX_SERVICE_EXCEPTION, mailboxServiceException);
			Messages msgs = mailboxServiceException.getMessages();
			throw new BankmailApplicationException(msgs);
		}
	}
}