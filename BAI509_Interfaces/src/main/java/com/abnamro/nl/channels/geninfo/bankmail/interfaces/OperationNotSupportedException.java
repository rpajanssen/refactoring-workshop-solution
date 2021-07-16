package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.exceptions.AABException;
import com.abnamro.nl.messages.Messages;

/**
 * OperationNotSupportedException 
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				29-05-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public class OperationNotSupportedException extends BankmailApplicationException{
	/**
	 * Default constructor.
	 * 
	 */
	public OperationNotSupportedException() {
		super();
	}

	/**
	 * Constructor that will also set messages on the exception. 
	 * @param messages Messages
	 */
	public OperationNotSupportedException(Messages messages) {
		super(messages);
	}

	/**
	 * Constructor that takes an existing AABException. This will move any
	 * messages into the new exception.
	 * @param aabException AABException
	 */
	public OperationNotSupportedException(AABException aabException) {
		super(aabException);
	}

}
