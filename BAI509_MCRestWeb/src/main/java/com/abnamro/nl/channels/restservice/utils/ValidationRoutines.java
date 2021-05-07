package com.abnamro.nl.channels.restservice.utils;

import java.util.HashMap;
import java.util.Map;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailRestServiceLogKeys;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailRestServiceMessageKeys;
import com.abnamro.nl.enumeration.util.EnumException;
import com.abnamro.nl.messages.MessageKey;

/**
 * MailContactRestServiceValidationRoutines:
 * Holds validation routines that need to be run on the input parameters of the RestService.
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	02-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public enum ValidationRoutines {

		CONCERNING_CUSTOMBER_BC_NUMBER_IS_BLANK ("CONCERNING_CUSTOMBER_BC_NUMBER_IS_BLANK", 
				BankmailRestServiceMessageKeys.ERROR_MAILCONTACT_CONCERNING_CUSTOMER_BC_NUMBER_NOT_PROVIDED, 
				BankmailRestServiceLogKeys.LOG_MAILCONTACT_CONCERNING_CUSTOMER_BC_NUMBER_NOT_PROVIDED),
				
		CONCERNING_CUSTOMBER_BC_NUMBER_IS_LONG("CONCERNING_CUSTOMBER_BC_NUMBER_IS_LONG", 
				BankmailRestServiceMessageKeys.ERROR_MAILCONTACT_CONCERNING_CUSTOMER_BC_NUMBER_INVALID, 
				BankmailRestServiceLogKeys.LOG_MAILCONTACT_CONCERNING_CUSTOMER_BC_NUMBER_INVALID);
	    
		/**
		 * Stored enum value
		 */
	    private String code;
		
	    /**
		 * MessageKey corresponding to validation Routine
		 */
	    private MessageKey messageKey;
	    
	    /**
		 * LogKey corresponding to validation Routine
		 */
	    private String logKey;
	    
	    /*
		 * Map to hold MailContactRestServiceValidationRoutines by code.
		 */
	    private static Map<String, ValidationRoutines> vRoutines = new HashMap<>();
		static {
		    for(ValidationRoutines param : values()) {
		    	vRoutines.put(param.getCode(), param);
		    }
		}
		
		/**
		 * @param code String
		 * @param messageKey MessageKey
		 * @param logKey String[]
		 */
		private ValidationRoutines(String code, MessageKey messageKey, String logKey) {
		    this.code = code;
		    this.messageKey = messageKey;
		    this.logKey = logKey;
		}
		
		/**
		 * @return code String
		 */
		public String getCode() { 
			return code; 
		}
		
		/**
		 * @return messageKey MessageKey
		 */
		public MessageKey getMessageKey() { 
			return messageKey; 
		}
		
		/**
		 * @return logKey String[]
		 */
		public String getLogKey() { 
			return logKey;
		}
		
		
		/**
		 * Gives enum for input code
		 * @param enumCode String
		 * @return MailContactRestServiceValidationRoutines
		 * @throws EnumException enumException
		 */
		public static ValidationRoutines fromString(String enumCode) throws EnumException {
			
			if(enumCode != null){
				if(vRoutines.containsKey(enumCode)){
					return vRoutines.get(enumCode);
				}else{
					throw new EnumException("Illegal Enumeration value encountered; "
							+ ValidationRoutines.class.getName()+"::getByCode("+enumCode+") "
		    				+ "Valid values are: " + getAll()+ ".");
				}
			}
			return null;
		}
		
		/**
		* Returns a Map of all MailContactRestServiceValidationRoutines enums.
		* @return Map
		*/
		public static Map<String, ValidationRoutines> getAll() {
			return vRoutines;
		}
	
}
