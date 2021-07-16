package com.abnamro.nl.channels.restservice.utils;

import java.util.HashMap;
import java.util.Map;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailRestServiceLogKeys;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailRestServiceMessageKeys;
import com.abnamro.nl.enumeration.util.EnumException;
import com.abnamro.nl.messages.MessageKey;

/**
 * MailMessageRestServiceValidationRoutines:
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

		MAIL_MESSAGE_ID_IS_BLANK ("MAIL_MESSAGE_ID_IS_BLANK", 
				BankmailRestServiceMessageKeys.ERROR_MAIL_MESSAGE_ID_NOT_PROVIDED,
				BankmailRestServiceLogKeys.LOG_MAIL_MESSAGE_ID_NOT_PROVIDED),
				
		MAIL_MESSAGE_ID_IS_LONG ("MAIL_MESSAGE_ID_IS_LONG", 
				BankmailRestServiceMessageKeys.ERROR_MAIL_MESSAGE_ID_INVALID,
				BankmailRestServiceLogKeys.LOG_MAIL_MESSAGE_ID_INVALID),
		
		CONCERNING_CUSTOMER_BC_NUMBER_IS_BLANK ("CONCERNING_CUSTOMER_BC_NUMBER_IS_BLANK", 
				BankmailRestServiceMessageKeys.ERROR_MAILMESSAGE_CONCERNING_CUSTOMER_BC_NUMBER_NOT_PROVIDED,
				BankmailRestServiceLogKeys.LOG_MAILMESSAGE_CONCERNING_CUSTOMER_BC_NUMBER_NOT_PROVIDED),
				
		CONCERNING_CUSTOMER_BC_NUMBER_IS_LONG ("CONCERNING_CUSTOMER_BC_NUMBER_IS_LONG", 
				BankmailRestServiceMessageKeys.ERROR_MAILMESSAGE_CONCERNING_CUSTOMER_BC_NUMBER_INVALID,
				BankmailRestServiceLogKeys.LOG_MAILMESSAGE_CONCERNING_CUSTOMER_BC_NUMBER_INVALID),
		
		PERIOD_UNIT_IS_BLANK ("PERIOD_UNIT_IS_BLANK", 
				BankmailRestServiceMessageKeys.ERROR_PERIOD_UNIT_NOT_PROVIDED,
				BankmailRestServiceLogKeys.LOG_PERIOD_UNIT_NOT_PROVIDED),
				
		PERIOD_UNIT_IS_PERIOD_UNIT ("PERIOD_UNIT_IS_PERIOD_UNIT", 
				BankmailRestServiceMessageKeys.ERROR_PERIOD_UNIT_INVALID,
				BankmailRestServiceLogKeys.LOG_PERIOD_UNIT_INVALID),
		
		PERIOD_VALUE_IS_BLANK ("PERIOD_VALUE_IS_BLANK", 
						BankmailRestServiceMessageKeys.ERROR_PERIOD_VALUE_NOT_PROVIDED,
						BankmailRestServiceLogKeys.LOG_PERIOD_VALUE_NOT_PROVIDED),
						
		PERIOD_VALUE_IS_INTEGER ("PERIOD_VALUE_IS_INTEGER", 
				BankmailRestServiceMessageKeys.ERROR_PERIOD_VALUE_INVALID,
				BankmailRestServiceLogKeys.LOG_PERIOD_VALUE_INVALID),
		
		MESSAGE_TYPE_IS_BLANK ("MESSAGE_TYPE_IS_BLANK", 
						BankmailRestServiceMessageKeys.ERROR_MAIL_MESSAGE_TYPE_NOT_PROVIDED,
						BankmailRestServiceLogKeys.LOG_MAIL_MESSAGE_TYPE_NOT_PROVIDED),
		
		MESSAGE_TYPE_IS_MESSAGE_TYPE ("MESSAGE_TYPE_IS_MESSAGE_TYPE", 
				BankmailRestServiceMessageKeys.ERROR_MAIL_MESSAGE_TYPE_INVALID,
				BankmailRestServiceLogKeys.LOG_MAIL_MESSAGE_TYPE_INVALID);
		
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
		 * Map to hold MailMessageRestServiceValidationRoutines by code.
		 */
	    private static Map<String, ValidationRoutines> vRoutines = new HashMap<String, ValidationRoutines>();
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
		 * @return MailMessageRestServiceValidationRoutines
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
		* Returns a Map of all MailMessageRestServiceValidationRoutines enums.
		* @return Map
		*/
		public static Map<String, ValidationRoutines> getAll() {
			return vRoutines;
		}
	
}
