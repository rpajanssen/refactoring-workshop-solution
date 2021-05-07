package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailboxAccessDeniedException;

/**
 * BankmailClientASC
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				01-10-2012	Initial version	  Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
public interface BankmailClientASC {

	/**
	 * Returns Bankmail address of the current user.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @return String: Bankmail address
	 * @throws MailboxAccessDeniedException
	 *             Exception
	 */
	public String getCurrentUserAddress(SecurityContext securityContext)
			throws MailboxAccessDeniedException;

	/**
	 * Returns display name for a Bankmail address
	 * 
	 * @param address
	 *            Bankmail address
	 * @return String : Display Name Of Address
	 * @throws BankmailApplicationException
	 *             Exception
	 */
	public String getDisplayNameOfAddress(String address)
			throws BankmailApplicationException;

	/**
	 * Validates message content provided by the bank client. Message must be
	 * valid HTML with
	 * <p>
	 * and <br>
	 * tags only, otherwise InvalidAttributeValueException is thrown.
	 * 
	 * @param content
	 *            String of messaeg content with html tags
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public void validateMessageContent(String content)
			throws BankmailApplicationException;

}
