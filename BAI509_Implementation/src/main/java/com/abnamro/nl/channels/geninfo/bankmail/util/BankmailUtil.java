package com.abnamro.nl.channels.geninfo.bankmail.util;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.implementation.BankmailASCLogConstants;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.MessageDTO;
import com.abnamro.nl.channels.service.mailbox.interfaces.ContactDTO;
import com.abnamro.nl.channels.service.mailbox.interfaces.MailActionDTO;
import com.abnamro.nl.channels.service.mailbox.interfaces.MailMessageDTO;
import com.abnamro.nl.channels.service.mailbox.interfaces.MailboxMessageDTO;
import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageKey;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import org.apache.commons.lang.StringUtils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BankmailUtil 
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	01-11-2012	Initial version	  Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
@LogInterceptorBinding
public class BankmailUtil {
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(BankmailUtil.class);

	/**
	 * Description This method copies message details from MailboxMessageDTO to MailboxMessageCustomDTO
	 * @param mailboxMessageDTO MailboxMessageDTO
	 * @return MailboxMessageCustomDTO mailboxMessageCustomDTO
	 */
	public MailboxMessageCustomDTO convertMessage(MailboxMessageDTO mailboxMessageDTO) {
		MailboxMessageCustomDTO mailboxMessageCustomDTO = null;
		if (null != mailboxMessageDTO && StringUtils.isNotBlank(mailboxMessageDTO.getId())) {

			mailboxMessageCustomDTO = new MailboxMessageCustomDTO();

			// Message ID
			mailboxMessageCustomDTO.setId(mailboxMessageDTO.getId());
			// from
			MailContactCustomDTO from = new MailContactCustomDTO();
			from.setDisplayName(mailboxMessageDTO.getFrom().getName());
			from.setAddress(mailboxMessageDTO.getFrom().getBankmailAddress());
			mailboxMessageCustomDTO.setFrom(from);

			// to
			List<ContactDTO> toList = mailboxMessageDTO.getTo();
			List<MailContactCustomDTO> toDestination = new ArrayList<>();

			for (ContactDTO contactDTO : toList) {
				MailContactCustomDTO contactCustomDTO = new MailContactCustomDTO();
				contactCustomDTO.setDisplayName(contactDTO.getName());
				contactCustomDTO.setAddress(contactDTO.getBankmailAddress());
				toDestination.add(contactCustomDTO);
			}
			mailboxMessageCustomDTO.setTo(toDestination);

			// inReplyTo
			if (null != mailboxMessageDTO.getInReplyTo()) {

				MailMessageCustomDTO inReplyTo = new MailMessageCustomDTO();
				// set id
				inReplyTo.setId(mailboxMessageDTO.getInReplyTo().getId());

				mailboxMessageCustomDTO.setInReplyTo(inReplyTo);
			}

			// IS_SEEN
			mailboxMessageCustomDTO.setIsSeen(mailboxMessageDTO.isSeen());
			// IS_ANSWERED
			mailboxMessageCustomDTO.setIsAnswered(mailboxMessageDTO.isAnswered());

			// Message Type
			MailMessageType mailMessageType = null;

			int messageCode = mailboxMessageDTO.getMessageType();
			mailMessageType = getMailMessageTypeFromMessageCode(messageCode);
			mailboxMessageCustomDTO.setMessageType(mailMessageType);

			// Origination date
			mailboxMessageCustomDTO.setOriginationDate(mailboxMessageDTO.getOriginationTimestamp());
			// received date
			mailboxMessageCustomDTO.setReceivedDate(mailboxMessageDTO.getReceivedTimestamp());
			// subject
			mailboxMessageCustomDTO.setSubject(mailboxMessageDTO.getSubject());
			// content
			mailboxMessageCustomDTO.setContent(mailboxMessageDTO.getContent());
			// deepLink
			MailDeepLinkCustomDTO deepLink = new MailDeepLinkCustomDTO();
			if(mailboxMessageDTO.getDeepLink()!= null) {
				Map<String, String> collect = Arrays.stream(mailboxMessageDTO.getDeepLink().split(";"))
						.map(s -> s.split("'", 2))
						.collect(Collectors.toMap(a -> a[0], a -> a.length > 1 ? a[1] : ""));
				if(collect.get("bcNumber")!=null) {
					deepLink.setBcNumber(collect.get("bcNumber").substring(0, collect.get("bcNumber").length() - 1));
				}
				if(collect.get("contractNumber")!=null) {
					deepLink.setContractNumber(collect.get("contractNumber").substring(0, collect.get("contractNumber").length() - 1));
				}
				if(collect.get("documentId")!=null) {
					deepLink.setDocumentId(collect.get("documentId").substring(0, collect.get("documentId").length() - 1));
				}
				if(collect.get("creationDate")!=null) {
					deepLink.setCreationDate(collect.get("creationDate").substring(0, collect.get("creationDate").length() - 1));
				}
				if(collect.get("PostboxDocumentType")!=null) {
					deepLink.setPostboxDocumentType(collect.get("PostboxDocumentType").substring(0, collect.get("PostboxDocumentType").length() - 1));
				}
				mailboxMessageCustomDTO.setDeepLink(deepLink);
			}
			// isReplyAllowed
			mailboxMessageCustomDTO.setIsReplyAllowed(mailboxMessageDTO.isReplyAllowed());

			// Folder Name
			FolderCustomDTO folder = new FolderCustomDTO();
			folder.setName(mailboxMessageDTO.getFolder().getName());
			mailboxMessageCustomDTO.setFolder(folder);

			// Concerning BC
			mailboxMessageCustomDTO.setConcerningCustomerBCNumber(mailboxMessageDTO.getConcerningCustomerBCNumber());

			// Conversation id
			mailboxMessageCustomDTO.setConversationId(mailboxMessageDTO.getConversationId());

			// doc type for differentiating between in app alerts
			if (MailMessageType.ALERT.equals(mailboxMessageCustomDTO.getMessageType())) {
				mailboxMessageCustomDTO.setDocumentType(mailboxMessageDTO.getDocumentType());
				convertMailActionDTO(mailboxMessageDTO, mailboxMessageCustomDTO);
			}

		}
		return mailboxMessageCustomDTO;
	}

	/**
	 * Description: This method converts mailActionDTO
	 * @param mailboxMessageDTO mailboxMessageDTO
	 * @param mailboxMessageCustomDTO mailboxMessageCustomDTO
	 */
	private void convertMailActionDTO(MailboxMessageDTO mailboxMessageDTO,
			MailboxMessageCustomDTO mailboxMessageCustomDTO) {
		MailActionCustomDTO[] mailActionCustomList = null;
		if (null != mailboxMessageDTO.getMailActionList() && mailboxMessageDTO.getMailActionList().length > 0) {
			mailActionCustomList = new MailActionCustomDTO[mailboxMessageDTO.getMailActionList().length];
			MailActionDTO[] mailActionList = mailboxMessageDTO.getMailActionList();
			//This below statement filters only the non-null MailActionDTO objects from the
			MailActionDTO[] mailActionListNonNullElements =  Arrays.stream(mailActionList).filter(Objects::nonNull).toArray(MailActionDTO[]::new);
			for (int i = 0; i < mailActionListNonNullElements.length; i++) {
				MailActionDTO mailActionDTO = mailActionListNonNullElements[i];
				MailActionCustomDTO mailActionCustomDTO = new MailActionCustomDTO();
				if (StringUtils.isNotEmpty(mailActionDTO.getName())) {
					mailActionCustomDTO.setName(mailActionDTO.getName());
				}
				if (null != mailActionDTO.getParams() && 0 != mailActionDTO.getParams().length) {
					mailActionCustomDTO.setParams(mailActionDTO.getParams());
				}
				mailActionCustomList[i] = mailActionCustomDTO;
			}
		}
		mailboxMessageCustomDTO.setMailActionList(mailActionCustomList);
	}

	/**
	 * This method copies message details from List<MailboxMessageDTO> mailboxMessageDTOs to
	 * List<MailboxMessageCustomDTO>
	 * @param mailboxMessageDTOs List<MailboxMessageDTO>
	 * @param contentFlag true|false flag for copying content.
	 * @return List<MailboxMessageCustomDTO>
	 */
	public List<MailboxMessageCustomDTO> convertMessageList(List<MailboxMessageDTO> mailboxMessageDTOs,
			boolean contentFlag) {
		List<MailboxMessageCustomDTO> mailboxMessageCustomDTOs = null;

		if (mailboxMessageDTOs != null && mailboxMessageDTOs.size() > 0) {

			mailboxMessageCustomDTOs = new ArrayList<>();

			for (MailboxMessageDTO mailboxMessageDTO : mailboxMessageDTOs) {

				MailboxMessageCustomDTO mailboxMessageCustomDTO = new MailboxMessageCustomDTO();
				mailboxMessageCustomDTO.setId(mailboxMessageDTO.getId());

				// from
				MailContactCustomDTO from = new MailContactCustomDTO();
				from.setDisplayName(mailboxMessageDTO.getFrom().getName());
				from.setAddress(mailboxMessageDTO.getFrom().getBankmailAddress());
				mailboxMessageCustomDTO.setFrom(from);

				// to
				List<MailContactCustomDTO> toDestination = new ArrayList<>();
				MailContactCustomDTO to = new MailContactCustomDTO();
				to.setDisplayName(mailboxMessageDTO.getTo().get(0).getName());
				to.setAddress(mailboxMessageDTO.getTo().get(0).getBankmailAddress());

				toDestination.add(to);
				mailboxMessageCustomDTO.setTo(toDestination);

				mailboxMessageCustomDTO.setOriginationDate(mailboxMessageDTO.getOriginationTimestamp());
				mailboxMessageCustomDTO.setReceivedDate(mailboxMessageDTO.getReceivedTimestamp());

				// Message Type
				MailMessageType mailMessageType = null;

				int messageCode = mailboxMessageDTO.getMessageType();
				mailMessageType = getMailMessageTypeFromMessageCode(messageCode);
				mailboxMessageCustomDTO.setMessageType(mailMessageType);

				mailboxMessageCustomDTO.setIsSeen(mailboxMessageDTO.isSeen());
				mailboxMessageCustomDTO.setIsAnswered(mailboxMessageDTO.isAnswered());
				mailboxMessageCustomDTO.setSubject(mailboxMessageDTO.getSubject());

				if (contentFlag) {
					mailboxMessageCustomDTO.setContent(mailboxMessageDTO.getContent());
				}
				// Concerning BC
				mailboxMessageCustomDTO.setConcerningCustomerBCNumber(mailboxMessageDTO.getConcerningCustomerBCNumber());

				// doc type for differentiating between in app alerts
				if (MailMessageType.ALERT.equals(mailboxMessageCustomDTO.getMessageType())) {
					mailboxMessageCustomDTO.setDocumentType(mailboxMessageDTO.getDocumentType());
				}

				mailboxMessageCustomDTOs.add(mailboxMessageCustomDTO);
			}
		}
		return mailboxMessageCustomDTOs;
	}

	/**
	 * convertMailMessage This method copies message details from MailMessageDTO to MailMessageCustomDTO
	 * @param mailMessageDTO MailMessageDTO
	 * @return MailMessageCustomDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public MailMessageCustomDTO convertMailMessage(MailMessageDTO mailMessageDTO) throws BankmailApplicationException {
		final String LOG_METHOD = " convertMailMessage(MailMessageDTO):MailMessageCustomDTO";
		MailMessageCustomDTO mailMessageCustomDTO = null;

		try {
			if (null != mailMessageDTO && StringUtils.isNotBlank(mailMessageDTO.getId())) {

				mailMessageCustomDTO = new MailMessageCustomDTO();

				// Message ID
				mailMessageCustomDTO.setId(mailMessageDTO.getId());
				// from
				MailContactCustomDTO from = new MailContactCustomDTO();
				from.setDisplayName(mailMessageDTO.getFrom().getName());
				from.setAddress(mailMessageDTO.getFrom().getBankmailAddress());
				mailMessageCustomDTO.setFrom(from);

				// to
				List<ContactDTO> toList = mailMessageDTO.getTo();
				List<MailContactCustomDTO> toDestination = new ArrayList<MailContactCustomDTO>();
				for (ContactDTO contactDTO : toList) {
					MailContactCustomDTO contactCustomDTO = new MailContactCustomDTO();
					contactCustomDTO.setDisplayName(contactDTO.getName());
					contactCustomDTO.setAddress(contactDTO.getBankmailAddress());
					toDestination.add(contactCustomDTO);
				}
				mailMessageCustomDTO.setTo(toDestination);

				// inReplyTo field
				if (null != mailMessageDTO.getInReplyTo()) {

					MailMessageCustomDTO inReplyTo = new MailMessageCustomDTO();
					inReplyTo.setId(mailMessageDTO.getInReplyTo().getId());
					mailMessageCustomDTO.setInReplyTo(inReplyTo);
				}

				// Message Type
				MailMessageType mailMessageType = null;

				int messageCode = mailMessageDTO.getMessageType();
				mailMessageType = getMailMessageTypeFromMessageCode(messageCode);
				mailMessageCustomDTO.setMessageType(mailMessageType);

				// Origination date
				mailMessageCustomDTO.setOriginationDate(mailMessageDTO.getOriginationTimestamp());

				// Expiry Date
				mailMessageCustomDTO.setExpiryDate(mailMessageDTO.getExpiryDate());

				mailMessageCustomDTO.setSubject(mailMessageDTO.getSubject());

				mailMessageCustomDTO.setContent(mailMessageDTO.getContent());

				// Concerning BC
				mailMessageCustomDTO.setConcerningCustomerBCNumber(mailMessageDTO.getConcerningCustomerBCNumber());

			}
			return mailMessageCustomDTO;
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_UNEXPECTED_EXCEPTION, exception);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_TECHNICAL_EXCEPTION), MessageType.getError());
			// Throw unexpected exception
			throw new BankmailApplicationException(msgs);
		}

	}

	/**
	 * convertMailMessageList This method copies message details from List<MailMessageDTO> to List<MailMessageCustomDTO>
	 * @param mailMessageDTOList List<MailMessageDTO>
	 * @return List<MailMessageCustomDTO>
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public List<MailMessageCustomDTO> convertMailMessageList(List<MailMessageDTO> mailMessageDTOList)
			throws BankmailApplicationException {

		final String LOG_METHOD = " convertMailMessageList(List<MailMessageDTO>):List<MailMessageCustomDTO>";
		try {

			List<MailMessageCustomDTO> mailMessageCustomDTOs = null;

			if (mailMessageDTOList != null && mailMessageDTOList.size() > 0) {

				mailMessageCustomDTOs = new ArrayList<MailMessageCustomDTO>();

				int count = 0;
				for (MailMessageDTO mailMessageDTO : mailMessageDTOList) {

					MailMessageCustomDTO mailMessageCustomDTO = new MailMessageCustomDTO();
					mailMessageCustomDTO.setId(mailMessageDTO.getId());

					// from
					MailContactCustomDTO from = new MailContactCustomDTO();
					from.setDisplayName(mailMessageDTO.getFrom().getName());
					from.setAddress(mailMessageDTO.getFrom().getBankmailAddress());
					mailMessageCustomDTO.setFrom(from);

					// to
					List<MailContactCustomDTO> toDestination = new ArrayList<MailContactCustomDTO>();
					MailContactCustomDTO to = new MailContactCustomDTO();
					to.setDisplayName(mailMessageDTO.getTo().get(0).getName());
					to.setAddress(mailMessageDTO.getTo().get(0).getBankmailAddress());

					toDestination.add(to);
					mailMessageCustomDTO.setTo(toDestination);

					// setOriginationDate
					if (null == mailMessageDTO.getActivationTimestamp()) {
						mailMessageCustomDTO.setOriginationDate(mailMessageDTO.getOriginationTimestamp());
					} else {
						mailMessageCustomDTO.setOriginationDate(mailMessageDTO.getActivationTimestamp());
					}

					mailMessageCustomDTO.setSubject(mailMessageDTO.getSubject());

					// Message Type
					MailMessageType mailMessageType = null;
					int messageCode = mailMessageDTO.getMessageType();
					mailMessageType = getMailMessageTypeFromMessageCode(messageCode);
					mailMessageCustomDTO.setMessageType(mailMessageType);
					mailMessageCustomDTOs.add(mailMessageCustomDTO);
					count++;
				}
			}
			return mailMessageCustomDTOs;
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_UNEXPECTED_EXCEPTION, exception);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_TECHNICAL_EXCEPTION), MessageType.getError());
			// Throw unexpected exception
			throw new BankmailApplicationException(msgs);
		}

	}

	/**
	 * Provides user class details
	 * @param userClassEnum UserClassEnum
	 * @return String UserClass
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public String getUserClass(String userClassEnum) throws BankmailApplicationException {
		final String LOG_METHOD = "getUserClass(UserClass):String";
		String userClass = "UNKNOWN";
		try {
//check

			String code = userClassEnum;

			if (UserClass.BC_WITH_PERSONAL_CARD .equals(code)) {
				userClass = BankmailConstants.BC_MAILBOX_NAME_PREFIX;
			} else if (UserClass.BC_WITH_NONPERSONAL_CARD .equals(code)) {
				userClass = BankmailConstants.AT_MAILBOX_NAME_PREFIX;
			} else if (UserClass.EMPLOYEE .equals( code)) {
				userClass = BankmailConstants.EMPLOYEE_MAILBOX_NAME_PREFIX;
			}

		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_UNEXPECTED_EXCEPTION, exception);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_TECHNICAL_EXCEPTION), MessageType.getError());
			// Throw unexpected exception
			throw new BankmailApplicationException(msgs);
		}
		return userClass;
	}

	/**
	 * Provides UserReference from Address
	 * @param address String
	 * @return String UserReference
	 * @throws InvalidAttributeValueException InvalidAttributeValueException
	 */
	public String getUserReferenceFromAddress(String address) throws InvalidAttributeValueException {
		final String LOG_METHOD = "getUserReferenceFromAddress(String):String";
		String userReference = null;

		if (StringUtils.isNotEmpty(address)) {
			userReference = address.substring(2);
		} else {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_INVALID_ATTRIBUTE_VALUE_EXCEPTION, address);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_INVALID_ATTRIBUTE_VALUE_EXCEPTION),
				MessageType.getError());
			throw new InvalidAttributeValueException(msgs);
		}
		return userReference;
	}

	/**
	 * Provides MailMessageType for given message code.
	 * @param messageCode int message code 1...5
	 * @return MailMessageType
	 */
	public MailMessageType getMailMessageTypeFromMessageCode(int messageCode) {
		MailMessageType mailMessageType = null;

		switch (messageCode) {
		case 1:
			mailMessageType = MailMessageType.CONVERSATION;
			break;
		case 2:
			mailMessageType = MailMessageType.COMMERCIAL;
			break;
		case 3:
			mailMessageType = MailMessageType.SERVICE;
			break;
		case 4:
			mailMessageType = MailMessageType.TRANSACTION;
			break;
		case 5:
			mailMessageType = MailMessageType.FINBOX;
			break;
		case 6:
			mailMessageType = MailMessageType.ALERT;
			break;
		default:
			mailMessageType = MailMessageType.FINBOX;
			break;
		}

		return mailMessageType;
	}

	/**
	 * @param mailboxMessageCustomDTO MailboxMessageCustomDTO
	 * @return MessageDTO messageDTO
	 */
	public MessageDTO convertMessage(MailboxMessageCustomDTO mailboxMessageCustomDTO) {
		MessageDTO messageDTO = null;

		if (null != mailboxMessageCustomDTO && StringUtils.isNotBlank(mailboxMessageCustomDTO.getId())) {

			messageDTO = new MessageDTO();

			// Message ID
			messageDTO.setId(mailboxMessageCustomDTO.getId());

			// from
			com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.ContactDTO from = new com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.ContactDTO();
			from.setName(mailboxMessageCustomDTO.getFrom().getDisplayName());
			from.setBankmailAddress(mailboxMessageCustomDTO.getFrom().getAddress());
			messageDTO.setFrom(from);

			// to
			List<MailContactCustomDTO> toCustomList = mailboxMessageCustomDTO.getTo();
			List<com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.ContactDTO> toAciDestination = new ArrayList<com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.ContactDTO>();

			for (MailContactCustomDTO mailContactCustomDTO : toCustomList) {
				com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.ContactDTO aciContactDTO = new com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.ContactDTO();
				aciContactDTO.setName(mailContactCustomDTO.getDisplayName());
				aciContactDTO.setBankmailAddress(mailContactCustomDTO.getAddress());
				toAciDestination.add(aciContactDTO);
			}

			com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.ContactDTO[] to = new com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.ContactDTO[0];
			to = toAciDestination.toArray(to);
			messageDTO.setTo(to);

			// Message Type
			int messageCode = mailboxMessageCustomDTO.getMessageType().getCode();
			messageDTO.setMessageType(messageCode);

			// Reply_Allowed
			messageDTO.setReplyAllowed(mailboxMessageCustomDTO.getIsReplyAllowed());

			// InReplyTO
			MailMessageCustomDTO inReplyToCustom = mailboxMessageCustomDTO.getInReplyTo();
			if (null != inReplyToCustom && StringUtils.isNotBlank(inReplyToCustom.getId())) {
				MessageDTO inReplyAci = new MessageDTO();
				inReplyAci.setId(inReplyToCustom.getId());
				messageDTO.setInReplyTo(inReplyAci);
			}

			// Conversation ID
			messageDTO.setConversationId(mailboxMessageCustomDTO.getConversationId());

			// Origination date
			messageDTO.setOriginationTimestamp(mailboxMessageCustomDTO.getOriginationDate());

			// Expiry date
			messageDTO.setExpiryDate(mailboxMessageCustomDTO.getExpiryDate());

			messageDTO.setSubject(mailboxMessageCustomDTO.getSubject());

			messageDTO.setContent(mailboxMessageCustomDTO.getContent());

			// Concerning BC
			messageDTO.setConcerningCustomerBCNumber(mailboxMessageCustomDTO.getConcerningCustomerBCNumber());

		}
		return messageDTO;
	}

	/**
	 * Description This method return date in simple date format as expected.
	 * @param date Date
	 * @param dateFormat String
	 * @return formatted date for given format.
	 */
	public String getFormattedDate(Date date, String dateFormat) {
		String formattedDate = null;
		Format formatter = new SimpleDateFormat(dateFormat);
		formattedDate = formatter.format(date);
		return formattedDate;
	}

	/**
	 * Description This method returns sender name based on the userClass and securityContext user reference
	 * @param securityContext SecurityContext
	 * @return sender name
	 * @throws BankmailApplicationException application exception
	 */
	public String getSenderName(SecurityContext securityContext) throws BankmailApplicationException {
		String userClass = getUserClass(securityContext.getUserClass());
		String senderName = userClass + securityContext.getUserReference();
		return senderName;
	}

	/**
	 * Log and throw error message
	 * @param LOG Logger object of class
	 * @param ex Exception to be logged
	 * @param logMessageId log key
	 * @param params logging parameters
	 * @param messageKey MessageKey
	 * @param methodName method name to log
	 * @throws BankmailApplicationException throws application exception
	 */
	public static void logAndThrowErrorMessage(final LogHelper LOG, Exception ex, String logMessageId,
			Object[] params, MessageKey messageKey, String methodName) throws BankmailApplicationException {
		LOG.error(methodName, logMessageId, ex, params);

		if (messageKey != null) {
			Messages msgs = new Messages();
			msgs.addMessage(new Message(messageKey), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		} else {
			throw new BankmailApplicationException();
		}
	}
}
