package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.exceptions.AABException;
import com.abnamro.nl.messages.Messages;
/**
 * ResourceAccessDeniedException Defines exception in case if resource access in denied.
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
public class ResourceAccessDeniedException extends BankmailApplicationException {
	
	/**
	 * Default constructor.
	 * 
	 */
	public ResourceAccessDeniedException() {
		super();
	}

	/**
	 * Constructor that will also set messages on the exception. 
	 * @param messages Messages
	 */
	public ResourceAccessDeniedException(Messages messages) {
		super(messages);
	}

	/**
	 * Constructor that takes an existing AABException. This will move any
	 * messages into the new exception.
	 * @param aabException AABException
	 */
	public ResourceAccessDeniedException(AABException aabException) {
		super(aabException);
	}


}
