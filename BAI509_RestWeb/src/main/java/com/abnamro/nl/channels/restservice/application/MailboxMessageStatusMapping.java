package com.abnamro.nl.channels.restservice.application;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import com.abnamro.nl.channels.rest.errorhandler.HttpStatusCodeMapper;

/**
 * MailboxMessageStatusMapping it is used for status mapping for message keys.
 * 
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				29-08-2012	Initial version	  Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
@Specializes
public class MailboxMessageStatusMapping extends HttpStatusCodeMapper {

	@Inject @MailboxMessageStatusCodes private Map<String,Status> map; 
	
	/**
	 * Gets the status code for the given message key
	 * @param messageKey String
	 * @return {@link Status} 
	 */
	@Override
	public Status getStatusCode(String messageKey) {
		if(map.containsKey(messageKey)){
			return map.get(messageKey);
		}
		return super.getStatusCode(messageKey);
	}

	static class MailboxMessageStatusMapProducer {
		/**
	 	 * Produces message-status map
		 * @return Map<String,Status>
		 */
		@Produces @ApplicationScoped @MailboxMessageStatusCodes
		public Map<String, Status> map() {
			Map<String, Status> map = new HashMap<>();
			map.put("MESSAGE_BAI509_5001", Status.FORBIDDEN); // Access to mailbox denied
			map.put("MESSAGE_BAI509_5004", Status.NOT_FOUND); // Message not found
			map.put("MESSAGE_BAI509_5007", Status.FORBIDDEN); // Mailbox access denied
			map.put("MESSAGE_BAI509_5011", Status.UNAUTHORIZED); // User not logged in or authorized
			map.put("MESSAGE_BAI509_5013", Status.BAD_REQUEST); // InvalidAttributeValueException is thrown.
			map.put("MESSAGE_BAI509_5021", Status.FORBIDDEN); // Operation Not Supported
			map.put("MESSAGE_BAI509_5022", Status.BAD_REQUEST); // ERROR_RECIPIENT_NOT_PROVIDED
			map.put("MESSAGE_BAI509_5025", Status.BAD_REQUEST); // Bad request
			map.put("MESSAGE_BAI509_5026", Status.BAD_REQUEST); // Bad request
			map.put("MESSAGE_BAI509_5029", Status.BAD_REQUEST); // Bad request
			map.put("MESSAGE_BAI509_9000", Status.INTERNAL_SERVER_ERROR); // Internal server error
			map.put("MESSAGE_RST509_9000", Status.INTERNAL_SERVER_ERROR); // Internal server error

			// BSI109 message to Status code mapping
			map.put("MESSAGE_BSI109_0001", Status.BAD_REQUEST); // ERROR_FOLDER_NAME_NOT_PROVIDED
			map.put("MESSAGE_BSI109_0002", Status.BAD_REQUEST); // ERROR_MESSAGE_ID_NOT_PROVIDED
			map.put("MESSAGE_BSI109_0003", Status.BAD_REQUEST); // ERROR_CONVERSATION_ID_NOT_PROVIDED
			map.put("MESSAGE_BSI109_0004", Status.BAD_REQUEST); // ERROR_INVALID_PAGE_SIZE
			map.put("MESSAGE_BSI109_0005", Status.BAD_REQUEST); // ERROR_INVALID_ATTRIBUTE_IN_DRAFT_MESSAGE
			map.put("MESSAGE_BSI109_0007", Status.BAD_REQUEST); // ERROR_INVALID_ATTRIBUTE_IN_DRAFT_MESSAGE
			map.put("MESSAGE_BSI109_0008", Status.BAD_REQUEST); // ERROR_MAILBOX_MESSAGE_NOT_PROVIDED
			map.put("MESSAGE_BSI109_0009", Status.BAD_REQUEST); // ERROR_INVALID_PAGE_SIZE_FOR_PAGE_NO
			map.put("MESSAGE_BSI109_0010", Status.BAD_REQUEST); // ERROR_REPLY_NOT_ALLOWED_FOR_CREATE_MESSAGE
			map.put("MESSAGE_BSI109_0011", Status.BAD_REQUEST); // ERROR_INVALID_CONCERNING_BC_IN_REPLY_CREATE_MESSAGE
			map.put("MESSAGE_BSI109_0012", Status.BAD_REQUEST); // ERROR_INVALID_ATTRIBUTE_LENGTH_EXCEPTION
			map.put("MESSAGE_BSI109_0013", Status.NOT_FOUND); // ERROR_MESSAGE_NOT_FOUND_INFROMFOLDER_WHILEDELETE
			map.put("MESSAGE_BSI109_0014", Status.BAD_REQUEST); // ERROR_READ_ONLY_ATTRIBUTE_EXCEPTION
			map.put("MESSAGE_BSI109_0015", Status.BAD_REQUEST); // ERROR_MISSING_ATTRIBUTE_EXCEPTION
			map.put("MESSAGE_BSI109_0016", Status.BAD_REQUEST); // ERROR_INVALID_ATTRIBUTE_EXCEPTION
			map.put("MESSAGE_BSI109_0017", Status.NOT_FOUND); // ERROR_MESSAGE_NOT_FOUND_EXCEPTION
			map.put("MESSAGE_BSI109_0018", Status.BAD_REQUEST); // ERROR_MAILBOX_NAME_NOT_PROVIDED
			return map;
		}
	}

}
