package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.exceptions.AABException;
import com.abnamro.nl.messages.Messages;
/**
 * NoClientReachException Defines exception in case of client unable to reach.
 *
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 *
 * @author
 * @see
 */
public class NoClientReachException extends ResourceAccessDeniedException{
	/**
	 * Default constructor.
	 * 
	 */
	public NoClientReachException() {
		super();
	}

	/**
	 * Constructor that will also set messages on the exception. 
	 * @param messages Messages
	 */
	public NoClientReachException(Messages messages) {
		super(messages);
	}

	/**
	 * Constructor that takes an existing AABException. This will move any
	 * messages into the new exception.
	 * @param aabException AABException
	 */
	public NoClientReachException(AABException aabException) {
		super(aabException);
	}

}
