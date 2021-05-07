package com.abnamro.nl.channels.restservice.utils;

import org.apache.commons.validator.GenericValidator;

import com.abnamro.nl.channels.restservice.MailContactRestService;
import com.abnamro.nl.exceptions.ConstraintsViolatedException;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageKey;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;

/**
 * MailContactServiceRequestValidator: 
 * Validator service which validates input parameters to 
 * different methods of the Rest Service.
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	05-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author: TCS 
 * @see
 */
@LogInterceptorBinding
public class MailContactServiceRequestValidator {

	/**
	 * Singleton instance of LogHelper.
	 */
	private static final LogHelper LOGGER = new LogHelper(MailContactRestService.class);
	
	/**
	 * This method provides validation for concerningCustomerBCNumber.
	 * 
	 * @param concerningCustomerBCNumberStr    : [string ] Input concerningCustomerBCNumber
	 * @return concerningCustomerBCNumber      : [long] Long representation of concerningCustomerBCNumber
	 * @throws ConstraintsViolatedException    : ConstraintsViolatedException in case of validation error.
	 */
	public long validateConcerningCustomerBCNumber(String concerningCustomerBCNumberStr) throws ConstraintsViolatedException {

		long concerningCustomerBCNumber;

		final String LOG_METHOD = "validateConcerningCustomerBCNumber(String):long";

		//Validate ConcerningBCNumber
		validateBlank (LOG_METHOD, ValidationRoutines.CONCERNING_CUSTOMBER_BC_NUMBER_IS_BLANK, concerningCustomerBCNumberStr);
		concerningCustomerBCNumber = validateLong (LOG_METHOD, ValidationRoutines.CONCERNING_CUSTOMBER_BC_NUMBER_IS_LONG, concerningCustomerBCNumberStr);

		return concerningCustomerBCNumber;
	}
		
	/**
	 * This method validates if the input value is blank or null. 
	 * @param methodName          			: [String ] methodName to be logged.
	 * @param validationRoutine   			: [ValidationRoutines ] Input attributeName & type of validation to be checked.
	 * @param attributeValue      			: [String ] Attribute Value to be validated.
	 * @throws ConstraintsViolatedException : ConstraintsViolatedException thrown in case of validation error.
	 */
	private void validateBlank(String methodName, ValidationRoutines validationRoutine, String attributeValue) throws ConstraintsViolatedException {
		
		if(GenericValidator.isBlankOrNull(attributeValue)) {
			handleValidationError(
					methodName,
					new Object[] {attributeValue},
					getMessageKey(validationRoutine),
					getLogKey(validationRoutine), null);
		}
	}
	
	/**
	 * This method validates if the input value is Long. 
	 *  
	 * @param methodName      : [String ] methodName to be logged.
	 * @param validationRoutines    : [ValidationRoutines ] Input attributeName & type of validation to be checked.
	 * @param attributeValue  : [String ] Attribute Value to be validated.
	 * @throws ConstraintsViolatedException      : ConstraintsViolatedException thrown in case of validation error.
	 */
	private long validateLong(String methodName, ValidationRoutines validationRoutines, String attributeValue) throws ConstraintsViolatedException {
		
		long attributeValueLong = 0L;
		try {

			attributeValueLong = Long.parseLong(attributeValue);

		} catch (NumberFormatException ne) {
			
			handleValidationError(
					methodName,
					new Object[] {attributeValue},
					getMessageKey(validationRoutines),
					getLogKey(validationRoutines), ne);
		}
		return attributeValueLong;
	}
	
	/**
	 * This method returns MessageKey corresponding to attributeName
	 * and validationType to be used by validation routines.
	 * 
	 * @param validationRoutine    : [ValidationRoutines] Input attributeName & validation to be checked.
	 * @return messageKey          : [MessageKey] MessageKey corresponding to Validation Error 
	 */
	private MessageKey getMessageKey(ValidationRoutines validationRoutine){
		return validationRoutine.getMessageKey();		
	}
	
	/**
	 * This method returns LogKey corresponding to attributeName
	 * and validationType to be used by validation routines.
	 * 
	 * @param validationRoutine    : [ValidationRoutines] Input attributeName & validation to be checked.
	 * @return messageKey          : [MessageKey] MessageKey corresponding to Validation Error
	 * @return logKey              : [String[]] LogKey corresponding to Validation Error 
	 */
	private String getLogKey(ValidationRoutines validationRoutine){
		return validationRoutine.getLogKey();
	}

	/**
	 * This method adds messageKey, logs errors and throws ConstraintsViolatedException
	 * in case of validation error.
	 * 
	 * @param methodName     : [String] methodName to be logged for error logs.
	 * @param attributes     : [Object[]] Other inputs to be logged with more information about error.
	 * @param messageKey     : [MessageKey] messageKey to be returned.
	 * @param logConstant    : [String] key to be logged in the error logs.
	 * @param t              : [String] exception to be logged in the error logs.
	 * @return exception     : ConstraintsViolatedException 
	 */
	private static void handleValidationError(String methodName, Object[] attributes, MessageKey messageKey, String logConstant, Throwable t)
			throws ConstraintsViolatedException {

		Messages messages = new Messages();
		messages.addMessage(new Message(messageKey),MessageType.getError());

		LOGGER.logError(methodName,logConstant, attributes, t);

		throw new ConstraintsViolatedException(messages);

	}

}
