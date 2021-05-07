package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.exceptions.AABException;
import com.abnamro.nl.messages.Messages;

/**
 * MessageNotFoundException 
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				25-06-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public class MessageNotFoundException extends BankmailApplicationException{
	/**
	 * Default constructor.
	 * 
	 */
	public MessageNotFoundException() {
		super();
	}

	/**
	 * Constructor that will also set messages on the exception. 
	 * @param messages Messages
	 */
	public MessageNotFoundException(Messages messages) {
		super(messages);
	}

	/**
	 * Constructor that takes an existing AABException. This will move any
	 * messages into the new exception.
	 * @param aabException AABException
	 */
	public MessageNotFoundException(AABException aabException) {
		super(aabException);
	}

}
