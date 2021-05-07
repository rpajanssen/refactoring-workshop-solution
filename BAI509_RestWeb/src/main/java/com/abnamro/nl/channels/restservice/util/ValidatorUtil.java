package com.abnamro.nl.channels.restservice.util;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailRestServiceLogKeys;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailRestServiceMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.resources.*;
import com.abnamro.nl.channels.restservice.MailboxConstants;
import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.enumeration.MailboxFolderName;
import com.abnamro.nl.enumeration.MessagesSortOrder;
import com.abnamro.nl.enumeration.util.EnumException;
import com.abnamro.nl.exceptions.ConstraintsViolatedException;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * ValidatorUtil: It validates Mailbox Rest Service input.
 * 
 * <PRE>
 * 
 * Developer          Date       Change Reason	  		Change
 * ------------------ ---------- ----------------- ---------------------------------------------- * 
 * TCS			  	10-10-2012	Initial version 	Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
@LogInterceptorBinding
public class ValidatorUtil {
	private static final LogHelper LOGGER = new LogHelper(ValidatorUtil.class);

	/**
	 * This method provides validation for mailbox name.
	 * @param mailboxName mailboxName mailbox's name
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public void validateMailboxName(String mailboxName) throws ConstraintsViolatedException {

		// Method signature to use in the log statements
		final String LOG_METHOD = "validateMailboxName(String)";

		Messages messages = new Messages();
		if (StringUtils.isBlank(mailboxName)) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_MAILBOX_NAME_NOT_PROVIDED, mailboxName);
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_MAILBOX_NAME_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}
	}

	/**
	 * This method provides validation for folder name
	 * @param folderName folder name
	 * @return {@link MailboxFolderName}
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public MailboxFolderName validateFolderName(String folderName) throws ConstraintsViolatedException {

		// Method signature to use in the log statements
		final String LOG_METHOD = "validateFolderName(String):MailboxFolderName";

		if (folderName == null || StringUtils.isBlank(folderName)) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_FOLDER_NAME_NOT_PROVIDED, folderName);
			Messages messages = new Messages();
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_FOLDER_NAME_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}

		try {

			MailboxFolderName folder = MailboxFolderName.fromString(folderName);

			if (null == folder) {
				LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ILLEGAL_FOLDER_NAME_PROVIDED, folderName);
				Messages messages = new Messages();
				messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_FOLDER_NAME_PROVIDED),
					MessageType.getError());
				throw new ConstraintsViolatedException(messages);
			}

			return folder;

		} catch (EnumException e) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ENUM_EXCEPTION, e);
			Messages messages = new Messages();
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_FOLDER_NAME_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}
	}

	/**
	 * This method provides validation for IncludeActions
	 * @param includeActionsString Possible values: EXTENDED
	 * @return {@link IncludeActions}
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 * @throws EnumException Enum exception
	 */
	public IncludeActions validateIncludeAction(String includeActionsString) throws ConstraintsViolatedException {

		// Method signature to use in the log statements
		final String LOG_METHOD = "validateIncludeAction(IncludeActions):IncludeActions";

		IncludeActions includeActions = null;
		if (StringUtils.isNotBlank(includeActionsString)) {

			includeActions = IncludeActions.get(includeActionsString);

			if (null == includeActions || !includeActions.equals(IncludeActions.EXTENDED)) {
				LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_INVALID_INCLUDE_ACTIONS_PROVIDED,
					includeActionsString);
				Messages messages = new Messages();
				messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_INCLUDE_ACTIONS_PROVIDED),
					MessageType.getError());
				throw new ConstraintsViolatedException(messages);
			}
		}

		return includeActions;
	}

	/**
	 * This method provides validation to for messageId
	 * @param messageId message id
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public void validateMessageId(String messageId) throws ConstraintsViolatedException {
		// Method signature to use in the log statements
		final String LOG_METHOD = "validateMessageId(String):void";

		Messages messages = new Messages();
		if (StringUtils.isBlank(messageId)) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_MESSAGE_ID_NOT_PROVIDED, messageId);
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_MESSAGE_ID_NOT_PROVIDED),
				MessageType.getError());

			throw new ConstraintsViolatedException(messages);
		} else {
			try {
				long parsedValue = Long.parseLong(messageId);
				if (parsedValue > MailboxConstants.MAX_MESSAGE_ID || parsedValue < MailboxConstants.MIN_MESSAGE_ID) {
					LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ILLEGAL_MESSAGE_ID_PROVIDED, messageId);
					messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_MESSAGE_ID_PROVIDED),
						MessageType.getError());

					throw new ConstraintsViolatedException(messages);
				}

			} catch (NumberFormatException numberFormatException) {
				LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ILLEGAL_MESSAGE_ID_PROVIDED, messageId);
				messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_MESSAGE_ID_PROVIDED),
					MessageType.getError());

				throw new ConstraintsViolatedException(messages);
			}
		}
	}

	/**
	 * This method provides validation to for messageTypeFilter
	 * @param messageTypeFilter Message type filter
	 * @return MailboxMessageType[] enum array of MailboxMessageType
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public MailMessageType[] validateAndMapMessageTypes(String messageTypeFilter) throws ConstraintsViolatedException {
		// Method signature to use in the log statements
		final String LOG_METHOD = "validateAndMapMessageTypes(String):MailMessageType[]";
		String[] messageTypes = null;
		MailMessageType[] mailMessageTypes = null;
		if (null != messageTypeFilter && messageTypeFilter.length() > 0) {

			messageTypes = messageTypeFilter.split("\\|");

			Set<String> messageTypesSet = new HashSet<>(Arrays.asList(messageTypes));
			if (messageTypesSet.size() > 1 && messageTypesSet.contains("ALERT")) {
				LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ILLEGAL_MESSAGE_TYPE_PROVIDED, messageTypeFilter);
				Messages messages = new Messages();
				messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_MESSAGE_TYPE_PROVIDED),
					MessageType.getError());
				throw new ConstraintsViolatedException(messages);
			}
			List<String> tempList = new ArrayList<>();
			for (Iterator<String> iterator = messageTypesSet.iterator(); iterator.hasNext();) {
				String string = iterator.next();
				tempList.add(string);
			}

			messageTypes = tempList.toArray(messageTypes);

			mailMessageTypes = new MailMessageType[messageTypes.length];
			int i = 0;

			MailMessageType messageType = null;
			for (String string : messageTypes) {

				messageType = MailMessageType.getByValue(string);

				if (null == messageType) {
					LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ILLEGAL_MESSAGE_TYPE_PROVIDED,
						messageTypeFilter);
					Messages messages = new Messages();
					messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_MESSAGE_TYPE_PROVIDED),
						MessageType.getError());
					throw new ConstraintsViolatedException(messages);
				}
				mailMessageTypes[i] = messageType;
				i++;
			}
		}
		return mailMessageTypes;
	}

	/**
	 * This method provides validation to for sortBy
	 * @param sortByString Sort parameter
	 * @return MessagesSortOrder enum of MessagesSortOrder
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public MessagesSortOrder validateSortBy(String sortByString) throws ConstraintsViolatedException {

		// Method signature to use in the log statements
		final String LOG_METHOD = "validateSortBy(String):MessagesSortOrder";

		MessagesSortOrder sortBy = null;
		try {
			if (StringUtils.isNotBlank(sortByString)) {

				sortBy = MessagesSortOrder.fromString(sortByString);

				if (null == sortBy) {
					LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_INVALID_SORT_ORDER_PROVIDED, sortByString);
					Messages messages = new Messages();
					messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_SORT_ORDER_PROVIDED),
						MessageType.getError());
					throw new ConstraintsViolatedException(messages);
				}
			}
			return sortBy;

		} catch (EnumException enumException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ENUM_EXCEPTION, enumException);
			Messages messages = new Messages();
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_SORT_ORDER_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}
	}

	/**
	 * This method provides validation to for pageSize. Maximum page is allowed 100.
	 * @param pageSizeString Page size
	 * @return int valid page size
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public int validatePageSize(String pageSizeString) throws ConstraintsViolatedException {
		// Method signature to use in the log statements
		final String LOG_METHOD = "validatePageSize(String):int";

		int pageSize = 0;

		if (StringUtils.isNotBlank(pageSizeString)) {
			try {
				pageSize = Integer.parseInt(pageSizeString);
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_INVALID_PAGE_SIZE_PROVIDED, numberFormatException,
					pageSizeString);
				Messages messages = new Messages();
				messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_PAGE_SIZE),
					MessageType.getError());
				throw new ConstraintsViolatedException(messages);
			}
		}

		if (pageSize < MailboxConstants.MIN_PAGE_SIZE || pageSize > MailboxConstants.MAX_PAGE_SIZE) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_INVALID_PAGE_SIZE_PROVIDED, pageSizeString);
			Messages messages = new Messages();
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_PAGE_SIZE),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}
		return pageSize;
	}

	/**
	 * This method provides validation to for pageNumber
	 * @param pageNumberString Page number
	 * @param pageSize Page size
	 * @return int page number
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public int validatePageNumber(String pageNumberString, int pageSize) throws ConstraintsViolatedException {
		// Method signature to use in the log statements
		final String LOG_METHOD = "validatePageNumber(String,int):int";

		int pageNumber = 0;

		if (StringUtils.isNotBlank(pageNumberString)) {
			try {
				pageNumber = Integer.parseInt(pageNumberString);
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_INVALID_PAGE_SIZE_PROVIDED, numberFormatException,
					pageNumberString);
				Messages messages = new Messages();
				messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_PAGE_NO),
					MessageType.getError());
				throw new ConstraintsViolatedException(messages);
			}
		}

		if (pageNumber < MailboxConstants.MIN_PAGE_NUMBER) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_INVALID_PAGE_NO_PROVIDED, pageNumberString);
			Messages messages = new Messages();
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_PAGE_NO), MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		} else if (pageNumber > MailboxConstants.MIN_PAGE_NUMBER && (pageSize <= MailboxConstants.MIN_PAGE_SIZE || pageSize > MailboxConstants.MAX_PAGE_SIZE)) {
			// if page number is provided then page size is mandatory
			// Should be in range of 1-Int max .
				LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_INVALID_PAGE_SIZE_FOR_PAGE_NO, pageSize);
				Messages messages = new Messages();
				messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_PAGE_SIZE_FOR_PAGE_NO),
					MessageType.getError());
				throw new ConstraintsViolatedException(messages);
		}
		return pageNumber;
	}

	/**
	 * This method provides validation to for CreateMessage operation
	 * @param securityContext  securityContext
	 * @param mailboxName name of mailbox
	 * @param mailboxMessageWrapper MailboxMessageWrapper
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 * @throws BankmailApplicationException Application exception
	 */
	public void validateCreateMessage(SecurityContext securityContext, String mailboxName,
			MailboxMessageWrapper mailboxMessageWrapper)
			throws ConstraintsViolatedException, BankmailApplicationException {

		final String LOG_METHOD = "validateCreateMessage(secuirtyContext,String,MailboxMessageWrapper):void";
		// validate mailboxName
		validateMailboxName(mailboxName);

		// validate input is provided or not
		if (null == mailboxMessageWrapper || null == mailboxMessageWrapper.getMailboxMessage()) {
			Messages messages = new Messages();
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CREATE_MESSAGE_INPUT_NOT_PROVIDED,
				mailboxMessageWrapper);
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_CREATE_MESSAGE_INPUT_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}

		// validate Concerning Customer
		MailboxMessage mailboxMessage = mailboxMessageWrapper.getMailboxMessage();

		if (null == mailboxMessage.getConcerningCustomer()
				|| null == String.valueOf(mailboxMessage.getConcerningCustomer().getBcNumber())) {
			Messages messages = new Messages();
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONCERNING_CUSTOMER_NOT_PROVIDED,
				mailboxMessage.getConcerningCustomer());
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_CONCERNING_CUSTOMER_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}

		// validate subject
		validateSubject(mailboxMessage.getSubject());

		// validate content
		validateContent(mailboxMessage.getContent());

		// validate isSeen
		validateIsSeen(mailboxMessage.getIsSeen());
	}

	/**
	 * This method provides validation to for UpdateMessage operation
	 * @param mailboxName name of mailbox
	 * @param messageId message if=d
	 * @param mailboxMessageWrapper mailboxMessageWrapper
	 * @throws ConstraintsViolatedException ConstraintsViolatedException
	 */
	public void validateUpdateMessage(String mailboxName, String messageId, MailboxMessageWrapper mailboxMessageWrapper)
			throws ConstraintsViolatedException {

		final String LOG_METHOD = "validateUpdateMessage(String,String,MailboxMessageWrapper):void";
		// validate mailboxName
		validateMailboxName(mailboxName);

		// validate input is provided or not
		if (null == mailboxMessageWrapper || null == mailboxMessageWrapper.getMailboxMessage()) {
			Messages messages = new Messages();
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_UPDATE_MESSAGE_INPUT_NOT_PROVIDED,
				mailboxMessageWrapper);
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_UPDATE_MESSAGE_INPUT_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}

		// validate message Id
		validateMessageId(messageId);
	}

	/**
	 * This method provides validation to for DeleteMessages operation
	 * @param mailboxName name of mailbox
	 * @param deleteMessagesInstructionWrapper DeleteMessagesInstructionWrapper
	 * @throws ConstraintsViolatedException ConstraintsViolatedException
	 */
	public void validateDeleteMessages(String mailboxName,
			DeleteMessagesInstructionWrapper deleteMessagesInstructionWrapper) throws ConstraintsViolatedException {

		final String LOG_METHOD = "validateDeleteMessages(String ,DeleteMessagesInputWrapper):void";
		// validate mailboxName
		validateMailboxName(mailboxName);

		// validate input is provided or not
		if (null == deleteMessagesInstructionWrapper
				|| null == deleteMessagesInstructionWrapper.getDeleteMessagesInstruction()) {
			Messages messages = new Messages();
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_DELETE_MESSAGES_INPUT_NOT_PROVIDED,
				deleteMessagesInstructionWrapper);
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_DELETE_MESSAGES_INPUT_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}

		DeleteMessagesInstruction deleteMessagesInstruction = deleteMessagesInstructionWrapper
			.getDeleteMessagesInstruction();

		// validate folder name
		validateFolderName(deleteMessagesInstruction.getFromFolderName().getCode());

		// validate message ID
		List<String> messageIds = deleteMessagesInstruction.getMessageIds();

		if (null != messageIds && !messageIds.isEmpty()) {
			for (String id : messageIds) {
				try {

					long parsedValue = Long.parseLong(id);

					if (parsedValue > MailboxConstants.MAX_MESSAGE_ID || parsedValue < MailboxConstants.MIN_MESSAGE_ID) {
						LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ILLEGAL_MESSAGE_ID_PROVIDED, id);
						Messages messages = new Messages();
						messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_MESSAGE_ID_PROVIDED),
							MessageType.getError());
						throw new ConstraintsViolatedException(messages);
					}
				} catch (NumberFormatException numberFormatException) {
					LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ILLEGAL_MESSAGE_ID_PROVIDED,
						numberFormatException, id);
					Messages messages = new Messages();
					messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_INVALID_MESSAGE_ID_PROVIDED),
						MessageType.getError());
					throw new ConstraintsViolatedException(messages);
				}

			}
		} else {
			// will throw exception saying that message id not provided
			validateMessageId("");
		}
	}

	/**
	 * This method provides validation to for SendMessage operation
	 * @param mailboxName name of mailbox
	 * @param sendMessageInstructionWrapper SendMessageInstructionWrapper
	 * @throws ConstraintsViolatedException ConstraintsViolatedException
	 */
	public void validateSendMessage(String mailboxName, SendMessageInstructionWrapper sendMessageInstructionWrapper)
			throws ConstraintsViolatedException {

		final String LOG_METHOD = "validateSendMessage(String,SendMessageInputWrapper):void";
		// validate mailboxName
		validateMailboxName(mailboxName);

		// validate input is provided or not
		if (null == sendMessageInstructionWrapper || null == sendMessageInstructionWrapper.getSendMessageInstruction()) {
			Messages messages = new Messages();
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_SEND_MESSAGE_INPUT_NOT_PROVIDED,
				sendMessageInstructionWrapper);
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_SEND_MESSAGE_INPUT_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}

		SendMessageInstruction sendMessageInstruction = sendMessageInstructionWrapper.getSendMessageInstruction();

		// validate send message messageId
		validateMessageId(sendMessageInstruction.getMessageId());
	}

	/**
	 * This method provides validation for subject as a Mandatory field.
	 * @param subject message subject
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public void validateSubject(String subject) throws ConstraintsViolatedException {

		// Method signature to use in the log statements
		final String LOG_METHOD = "validateSubject(String):void";

		if (StringUtils.isBlank(subject)) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_SUBJECT_NOT_PROVIDED, subject);
			Messages messages = new Messages();
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_SUBJECT_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}
	}

	/**
	 * This method provides validation for content as a Mandatory field.
	 * @param validateContent message content
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public void validateContent(String validateContent) throws ConstraintsViolatedException {

		// Method signature to use in the log statements
		final String LOG_METHOD = "validateContent(String):void";

		if (StringUtils.isBlank(validateContent)) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_COTENT_NOT_PROVIDED, validateContent);
			Messages messages = new Messages();
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_CONTENT_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}
	}

	/**
	 * This method provides validation for isSeen as a Mandatory field.
	 * @param isSeen message isSeen flag
	 * @throws ConstraintsViolatedException ConstraintsViolatedException will be thrown if validation fails
	 */
	public void validateIsSeen(Boolean isSeen) throws ConstraintsViolatedException {

		// Method signature to use in the log statements
		final String LOG_METHOD = "validateIsSeen(Boolean):void";

		if (null == isSeen) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_IS_SEEN_NOT_PROVIDED, isSeen);
			Messages messages = new Messages();
			messages.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_IS_SEEN_NOT_PROVIDED),
				MessageType.getError());
			throw new ConstraintsViolatedException(messages);
		}
	}

}
