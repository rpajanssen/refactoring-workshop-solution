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
 * MailboxContactStatusMapper 
 * it is used for status mapping for message keys.
 *
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				10-12-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
@Specializes
public class MailMessageStatusMapper extends HttpStatusCodeMapper {
	
	@Inject @MailMessageStatusCodes private Map<String,Status> map; 
	
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
	
	static class MailMessageStatusMapProducer {
		/**
		 * Produces message-status map
		 * @return Map<String,Status>
		 */
		@Produces @ApplicationScoped @MailMessageStatusCodes
		public Map<String, Status> map() {
			Map<String, Status> map = new HashMap<>();
			map.put("MESSAGE_RST509_3001", Status.BAD_REQUEST); // Mail Message ID not provided.
			map.put("MESSAGE_RST509_3002", Status.BAD_REQUEST); // Mail Message ID is Invalid.
			map.put("MESSAGE_RST509_3003", Status.BAD_REQUEST); // Concerning Customer BC Number not provided.
			map.put("MESSAGE_RST509_3004", Status.BAD_REQUEST); // Concerning Customer BC Number is Invalid.
			map.put("MESSAGE_RST509_3005", Status.BAD_REQUEST); // Period incomplete.
			map.put("MESSAGE_RST509_3006", Status.BAD_REQUEST); // Period Unit Invalid.
			map.put("MESSAGE_RST509_3007", Status.BAD_REQUEST); // Period Value Invalid.
			map.put("MESSAGE_RST509_3008", Status.BAD_REQUEST); // Mail Message Type Invalid.
			map.put("MESSAGE_BAI509_5031", Status.FORBIDDEN); // Client ReachException
			map.put("MESSAGE_BSI109_0017", Status.NOT_FOUND); // MailMessage Not Found.
			map.put("MESSAGE_RST509_9003", Status.INTERNAL_SERVER_ERROR); // Technical Error.
			return map;
		}
	}	
}
