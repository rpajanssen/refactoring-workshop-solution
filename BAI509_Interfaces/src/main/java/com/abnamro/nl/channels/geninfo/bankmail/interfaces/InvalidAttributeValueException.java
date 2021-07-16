package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.exceptions.AABException;
import com.abnamro.nl.messages.Messages;

/**
 * InvalidAttributeValueException 
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				9-07-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public class InvalidAttributeValueException extends BankmailApplicationException{
	
	/**
	 * Default constructor.
	 * 
	 */
	public InvalidAttributeValueException() {
		super();
	}

	/**
	 * Constructor that will also set messages on the exception. 
	 * @param messages Messages
	 */
	public InvalidAttributeValueException(Messages messages) {
		super(messages);
	}

	/**
	 * Constructor that takes an existing AABException. This will move any
	 * messages into the new exception.
	 * @param aabException AABException
	 */
	public InvalidAttributeValueException(AABException aabException) {
		super(aabException);
	}

}
