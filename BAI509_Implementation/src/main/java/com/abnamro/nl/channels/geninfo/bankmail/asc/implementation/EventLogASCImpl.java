package com.abnamro.nl.channels.geninfo.bankmail.asc.implementation;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.EventLogASC;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailContactCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailMessageCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailUtil;
import com.abnamro.nl.health.exceptions.HealthCheckException;
import com.abnamro.nl.health.interfaces.HealthCheckable;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.abnamro.nl.channels.geninfo.bankmail.asc.implementation.ArchivingConstants.APPID;

/**
 * EventLogASCImpl: It is the implementation for EventLogASC.
 * 
 * <PRE>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	11-12-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
@LogInterceptorBinding
public class EventLogASCImpl implements EventLogASC, HealthCheckable {
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(EventLogASCImpl.class);

	public static final String MESSAGE_LOG = "LOG_BAI509_0031";

	public static final String EVENT_LOG = "LOG_BAI509_0032";

	/**
	 * Bankmail General Util class
	 */
	private static BankmailUtil bankmailUtil = new BankmailUtil();

	/**
	 * Logs send message event.
	 * @param securityContext SecurityContext
	 * @param message MailMessageCustomDTO
	 * @throws BankmailApplicationException application exception
	 */
	public void logSendMessage(SecurityContext securityContext, MailMessageCustomDTO message) throws BankmailApplicationException {
		ArrayList<String> sendMessageAttributesList = new ArrayList<String>();
		// Message ID
		sendMessageAttributesList.add(message.getId());

		// senderName
		String senderName = bankmailUtil.getSenderName(securityContext);
		sendMessageAttributesList.add(senderName);

		// InitiativeOf
		if (senderName.startsWith(BankmailConstants.BC_MAILBOX_NAME_PREFIX)
				|| senderName.startsWith(BankmailConstants.AT_MAILBOX_NAME_PREFIX)) {
			sendMessageAttributesList.add(BankmailConstants.INITIATIVE_OF_BC);
		} else {
			sendMessageAttributesList.add(BankmailConstants.INITIATIVE_OF_BANK);
		}

		// From Address
		sendMessageAttributesList.add(message.getFrom().getAddress());

		// Recipients
		List<MailContactCustomDTO> toList = message.getTo();
		StringBuilder toSBuilder = new StringBuilder();
		for (MailContactCustomDTO contact : toList) {
			toSBuilder.append(contact.getAddress()).append("/");
		}
		if (toSBuilder.length() > 0) {
			int lastIndexOf = toSBuilder.lastIndexOf("/");
			toSBuilder.replace(lastIndexOf, lastIndexOf + 1, "");
		}

		sendMessageAttributesList.add(toSBuilder.toString());

		// Origination date
		String formattedDate = bankmailUtil.getFormattedDate(message.getOriginationDate(),
			BankmailConstants.MI_TIMESTAMP_FORMAT);
		sendMessageAttributesList.add(formattedDate);

		// Activation date
		sendMessageAttributesList.add(BankmailConstants.EMPTY_STRING);

		// Document type
		sendMessageAttributesList.add(BankmailConstants.EMPTY_STRING);

		// Message type
		sendMessageAttributesList.add(((Integer) message.getMessageType().getCode()).toString());

		// Concerning customer BC
		if (null == message.getConcerningCustomerBCNumber()) {
			sendMessageAttributesList.add(BankmailConstants.EMPTY_STRING);
		} else {
			sendMessageAttributesList.add(message.getConcerningCustomerBCNumber());
		}

		// Campaign id
		sendMessageAttributesList.add(BankmailConstants.EMPTY_STRING);

		// In reply to
		if (null != message.getInReplyTo() && null != message.getInReplyTo().getId()) {
			sendMessageAttributesList.add(message.getInReplyTo().getId());
		} else {
			sendMessageAttributesList.add(BankmailConstants.EMPTY_STRING);
		}

		// Conversation id
		sendMessageAttributesList.add(message.getConversationId());

		// Expiry date
		sendMessageAttributesList.add(BankmailConstants.EMPTY_STRING);

		// subject & replace all comma & double quote char with space
		String subject = message.getSubject();
		subject = subject.replaceAll(",", " ");
		subject = subject.replaceAll("\"", " ");
		sendMessageAttributesList.add(subject);

		// added for FrontEndType wish
		if (null != securityContext &&  null != securityContext.getSessionAttributes().get(APPID)) {
			sendMessageAttributesList.add(String.valueOf(null != securityContext.getSessionAttributes().get(APPID)));
		}

		logEvent(MESSAGE_LOG, sendMessageAttributesList);
	}

	/**
	 * Logs open mailbox message event.
	 * @param securityContext SecurityContext
	 * @param mailboxName mailbox name String
	 * @param messageId message id String
	 * @throws BankmailApplicationException application exception
	 */
	public void logOpenMessage(SecurityContext securityContext, String mailboxName, String messageId)
			throws BankmailApplicationException {
		ArrayList<String> openMessageAttributesList = new ArrayList<String>();
		Date currentTime = new Date();
		openMessageAttributesList.add(bankmailUtil
			.getFormattedDate(currentTime, BankmailConstants.MI_TIMESTAMP_FORMAT));
		openMessageAttributesList.add(BankmailConstants.MI_OPEN_EVENT);
		openMessageAttributesList.add(mailboxName);
		openMessageAttributesList.add(messageId);

		String senderName = bankmailUtil.getSenderName(securityContext);
		openMessageAttributesList.add(senderName);
		
		// added for FrontEndType wish
		if (null != securityContext && null != securityContext.getSessionAttributes().get(APPID)) {
			openMessageAttributesList.add(String.valueOf(null != securityContext.getSessionAttributes().get(APPID)));
		}

		logEvent(EVENT_LOG, openMessageAttributesList);
	}

	/**
	 * Logs delete mailbox message event.
	 * @param securityContext SecurityContext
	 * @param mailboxName String
	 * @param messageId String
	 * @throws BankmailApplicationException application exception
	 */
	public void logDeleteMessage(SecurityContext securityContext, String mailboxName, String messageId)
			throws BankmailApplicationException {
		ArrayList<String> deleteMessageAttributesList = new ArrayList<String>();
		Date currentTime = new Date();
		deleteMessageAttributesList.add(bankmailUtil.getFormattedDate(currentTime,
			BankmailConstants.MI_TIMESTAMP_FORMAT));
		deleteMessageAttributesList.add(BankmailConstants.MI_DELETE_EVENT);
		deleteMessageAttributesList.add(mailboxName);
		deleteMessageAttributesList.add(messageId);
		String senderName = bankmailUtil.getSenderName(securityContext);
		deleteMessageAttributesList.add(senderName);
		// added for FrontEndType wish
		if (null != securityContext && null != securityContext.getSessionAttributes().get(APPID)) {
			deleteMessageAttributesList.add(String.valueOf(null != securityContext.getSessionAttributes().get(APPID)));
		}
		logEvent(EVENT_LOG, deleteMessageAttributesList);
	}

	/**
	 * Logs an event to external system. Currently logs to WebSphere log using logName as prefix. Event is logged in CSV
	 * format).
	 * @param log String
	 * @param eventAttributes atrributes to be logged
	 */
	private void logEvent(String log, List<String> eventAttributes) {
		final String LOG_METHOD = "logEvent(String, List<String>):void";
		StringBuilder message = new StringBuilder();
		message.append(getCSVMessage(eventAttributes));
		LOGGER.info(LOG_METHOD, log, message.toString());
	}

	/**
	 * isHealthy
	 * This method returns the healthiness of a component
	 * @return boolean true|false
	 * @throws HealthCheckException healthCheckException
	 */
	public boolean isHealthy() throws HealthCheckException {
		return true;
	}

	/**
	 * Operation returns CSV format output.
	 * @param eventAttributes List of event attributes
	 * @return CSV format output
	 */
	private StringBuilder getCSVMessage(List<String> eventAttributes) {
		StringBuilder attributeBuilder = new StringBuilder();
		for (String attribute : eventAttributes) {
			attributeBuilder.append(attribute).append(BankmailConstants.COMMA_SEPARATOR);
		}

		if (attributeBuilder.length() > 0) {
			int lastIndexOf = attributeBuilder.lastIndexOf(",");
			attributeBuilder.replace(lastIndexOf, lastIndexOf + 1, "");
		}
		return attributeBuilder;
	}

}
