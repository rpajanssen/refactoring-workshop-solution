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
public class MailContactStatusMapper extends HttpStatusCodeMapper {

	@Inject @MailContactStatusCodes private Map<String,Status> map; 
	
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
	
	 static class MailContactStatusMapProducer {
		 	/**
		 	 * Produces message-status map
			 * @return Map<String,Status>
			 */
			@Produces @ApplicationScoped @MailContactStatusCodes
			public Map<String,Status> map(){
				Map<String,Status> map = new HashMap<>();
				map.put("MESSAGE_RST509_2001", Status.BAD_REQUEST); //Validation Error	
				map.put("MESSAGE_RST509_2002", Status.BAD_REQUEST); //Validation Error
				
				map.put("MESSAGE_BAI509_5031", Status.FORBIDDEN); //Client ReachException
				
				map.put("MESSAGE_RST509_9002", Status.INTERNAL_SERVER_ERROR); //There was a technical error
				return map;
			}
		}
}
