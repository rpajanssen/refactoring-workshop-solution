package com.abnamro.nl.channels.restservice.utils;

import static com.abnamro.nl.channels.restservice.utils.MailMessageServiceConstants.SEPERATOR;
import static com.abnamro.nl.channels.restservice.utils.MailMessageServiceConstants.VALID_MESSAGE_TYPES;
import static com.abnamro.nl.channels.restservice.utils.MailMessageServiceConstants.VALID_PERIOD_UNIT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;

import com.abnamro.nl.channels.restservice.MailMessageRestService;
import com.abnamro.nl.channels.restservice.interfaces.PeriodDTO;
import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.enumeration.PeriodUnit;
import com.abnamro.nl.enumeration.util.EnumException;
import com.abnamro.nl.exceptions.ConstraintsViolatedException;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageKey;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;

/**
 * MailMessageServiceRequestValidator: 
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
public class MailMessageServiceRequestValidator {

	/**
	 * Singleton instance of LogHelper.
	 */
	private static final LogHelper LOGGER = new LogHelper(MailMessageRestService.class);
	
	/**
	 * This method provides validation for mailMessageId.
	 * 
	 * @param mailMessageIdStr              : [ String ] Input mailMessageId
	 * @return mailMessageId                : [ long ] Valid mailMessageId
	 * @throws ConstraintsViolatedException : ConstraintsViolatedException in case of validation error. 
	 */
	public long validateMailMessageId(String mailMessageIdStr) throws ConstraintsViolatedException {

		long mailMessageId;

		final String LOG_METHOD = "validateMailMessageId(String):long";
		
		//Validate MailMessageId
		validateBlank (LOG_METHOD, ValidationRoutines.MAIL_MESSAGE_ID_IS_BLANK, mailMessageIdStr);
		mailMessageId = validateLong (LOG_METHOD, ValidationRoutines.MAIL_MESSAGE_ID_IS_LONG, mailMessageIdStr);
		
		return mailMessageId;
	}

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
		validateBlank (LOG_METHOD, ValidationRoutines.CONCERNING_CUSTOMER_BC_NUMBER_IS_BLANK, concerningCustomerBCNumberStr);
		concerningCustomerBCNumber = validateLong (LOG_METHOD, ValidationRoutines.CONCERNING_CUSTOMER_BC_NUMBER_IS_LONG, concerningCustomerBCNumberStr);

		return concerningCustomerBCNumber;
	}

	/**
	 * This method provides validation for PeriodUnit & PeriodValue.
	 * 
	 * @param periodUnitStr                 : [String] Input unit of period.
	 * @param periodValueStr                : [String] value of the period in period units.
	 * @return periodDTO                    : [PeriodDTO] Valid PeriodUnit and PeriodValue.
	 * @throws ConstraintsViolatedException : ConstraintsViolatedException in case of validation error.
	 */
	public PeriodDTO validatePeriod(String periodUnitStr, String periodValueStr) throws ConstraintsViolatedException {

		final String LOG_METHOD = "validatePeriod(String, String):PeriodDTO";

		PeriodDTO returnPeriodDTO = new PeriodDTO();
		
		//Validation for Period Unit
		validateBlank (LOG_METHOD, ValidationRoutines.PERIOD_UNIT_IS_BLANK, periodUnitStr);
		PeriodUnit periodUnit = validatePeriodUnit(LOG_METHOD, ValidationRoutines.PERIOD_UNIT_IS_PERIOD_UNIT, periodUnitStr);
		
		//Validation for Period Value
		validateBlank (LOG_METHOD, ValidationRoutines.PERIOD_VALUE_IS_BLANK, periodValueStr);
		int periodValue = validateInteger(LOG_METHOD, ValidationRoutines.PERIOD_VALUE_IS_INTEGER, periodValueStr);
		
		returnPeriodDTO.setPeriodUnit(periodUnit);
		returnPeriodDTO.setPeriodValue(periodValue);

		return returnPeriodDTO;
	}

	/**
	 * This method validates the input parameter mailMessageTypes
	 * 
	 * @param mailMessageTypesStr           : [String ] Input mailMessageTypesStr
	 * @return mailMessageTypes             : [List<MailMessageType>] Valid MailMessageTypes
	 * @throws ConstraintsViolatedException : ConstraintsViolatedException in case of validation error.
	 */
	public List<MailMessageType> validateMessageTypes(String mailMessageTypesStr) throws ConstraintsViolatedException {

		final String LOG_METHOD = "validateMessageTypes(String):MailMessageType[]";

		List<MailMessageType> lstMailMessageTypes = new ArrayList<>();

		
		validateBlank (LOG_METHOD, ValidationRoutines.MESSAGE_TYPE_IS_BLANK, mailMessageTypesStr);
		
		// Check if mailMessageTypes is provided.
		if (!StringUtils.isBlank(mailMessageTypesStr)) {
			
			for (String strMailMessageType : mailMessageTypesStr.split(SEPERATOR)) {
				//Validate MailMessageType
				MailMessageType mailMessageType = validateMessageType(LOG_METHOD, ValidationRoutines.MESSAGE_TYPE_IS_MESSAGE_TYPE, strMailMessageType);
				lstMailMessageTypes.add(mailMessageType);
			}
		}

		return lstMailMessageTypes;
	}	
	
	/**
	 * This method validates if the input value is blank or null. 
	 *  
	 * @param methodName          			: [String ] methodName to be logged.
	 * @param validationRoutine   			: [ValidationRoutines ] Input attributeName & type of validation to be checked.
	 * @param attributeValue      			: [String ] Attribute Value to be validated.
	 * @throws ConstraintsViolatedException	: ConstraintsViolatedException thrown in case of validation error.
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
	 * @param validationRoutines   : [ValidationRoutines ] Input attributeName & type of validation to be checked.
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
	 * This method validates if the input value is Integer. 
	 *  
	 * @param methodName          : [String ] methodName to be logged.
	 * @param validationRoutine   : [ValidationRoutines ] Input attributeName & type of validation.
	 * @param attributeValue      : [String ] Attribute Value to be validated.
	 * @throws ConstraintsViolatedException          : ConstraintsViolatedException thrown in case of validation error.
	 */
	private int validateInteger(String methodName, ValidationRoutines validationRoutine, String attributeValue) throws ConstraintsViolatedException {
		
		int attributeValueInt = 0;
		try {

			attributeValueInt = Integer.parseInt(attributeValue);

		} catch (NumberFormatException ne) {
			
			handleValidationError(
					methodName,
					new Object[] {attributeValue},
					getMessageKey(validationRoutine),
					getLogKey(validationRoutine), ne);
		}
		return attributeValueInt;
	}
		
	/**
	 * This method validates if the input value is a valid PeriodUnit. 
	 * Valid Values: MONTH
	 * 
	 * @param methodName          : [String ] methodName to be logged.
	 * @param validationRoutine   : [ValidationRoutines ] Input attributeName & type of validation.
	 * @param attributeValue      : [String ] Attribute Value to be validated.
	 * @throws ConstraintsViolatedException          : ConstraintsViolatedException thrown in case of validation error.
	 */
	private PeriodUnit validatePeriodUnit(String methodName, ValidationRoutines validationRoutine, String attributeValue) throws ConstraintsViolatedException {
		
		PeriodUnit periodUnit = null;
		try {

			periodUnit = PeriodUnit.fromString(attributeValue);
			
			if(!ArrayUtils.contains(VALID_PERIOD_UNIT, periodUnit)) {
				throw new EnumException("PeriodUnit is Invalid " + periodUnit + 
						"; Valid values are: " + Arrays.toString(VALID_PERIOD_UNIT) + ".");
			}


		} catch (EnumException e) {

			handleValidationError(
					methodName,
					new Object[] {attributeValue },
					getMessageKey(validationRoutine),
					getLogKey(validationRoutine), e);
			
			
		}
		return periodUnit;
	}
	
	/**
	 * This method validates if the input value is a valid MessageType. 
	 * Valid Values: CONVERSATION, COMMERCIAL
	 * 
	 * @param methodName          : [String ] methodName to be logged.
	 * @param validationRoutine   : [ValidationRoutines ] Input attributeName & type of validation.
	 * @param attributeValue      : [String ] Attribute Value to be validated.
	 * @throws ConstraintsViolatedException          : ConstraintsViolatedException thrown in case of validation error.
	 */
	private MailMessageType validateMessageType(String methodName, ValidationRoutines validationRoutine, String attributeValue) throws ConstraintsViolatedException {
		MailMessageType mailMessageType = null;
		try {

			mailMessageType = MailMessageType.getByValue(attributeValue);
			
			if(!ArrayUtils.contains(VALID_MESSAGE_TYPES, mailMessageType)) {
				throw new EnumException("MessageType is Invalid " + mailMessageType + 
						"; Valid values are: " + Arrays.toString(VALID_MESSAGE_TYPES) + ".");
			}

		} catch (EnumException e) {

			handleValidationError(
					methodName,
					new Object[] {attributeValue},
					getMessageKey(validationRoutine),
					getLogKey(validationRoutine), e);
			
		}
		return mailMessageType;
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
	 * @param logConstant    : [String[]] key to be logged in the error logs.
	 * @param t              : [String] exception to be logged in the error logs.
	 * @return exception     : ConstraintsViolatedException 
	 */
	private static void handleValidationError(String methodName, Object[] attributes, MessageKey messageKey, String logConstant, Throwable t)
			throws ConstraintsViolatedException {
		Messages messages = new Messages();
		messages.addMessage(new Message(messageKey),MessageType.getError());
		LOGGER.logError(methodName, logConstant, t, attributes);
		throw new ConstraintsViolatedException(messages);
	}

}
