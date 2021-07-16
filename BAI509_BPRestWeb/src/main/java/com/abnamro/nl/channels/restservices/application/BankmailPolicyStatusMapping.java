package com.abnamro.nl.channels.restservices.application;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import com.abnamro.nl.channels.rest.errorhandler.HttpStatusCodeMapper;

/**
 * BankmailPolicyStatusMapping it is used for status mapping for message keys.
 * 
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				10-10-2012	Initial version	  Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
@Specializes
public class BankmailPolicyStatusMapping extends HttpStatusCodeMapper {

	@Inject @BankMailPolicyStatusCodes private Map<String,Status> map; 

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

	static class BankmailPolicyStatusMapProducer {
		/**
	 	 * Produces message-status map
		 * @return Map<String,Status>
		 */
		@Produces @ApplicationScoped @BankMailPolicyStatusCodes
		public Map<String,Status> map(){
			Map<String,Status> map = new HashMap<>();
			map.put("MESSAGE_BAI509_5021", Status.FORBIDDEN); //Mail Message ID not provided.
			map.put("MESSAGE_RST509_9000", Status.INTERNAL_SERVER_ERROR); //Mail Message ID is Invalid.
			return map;
		}
}
}
