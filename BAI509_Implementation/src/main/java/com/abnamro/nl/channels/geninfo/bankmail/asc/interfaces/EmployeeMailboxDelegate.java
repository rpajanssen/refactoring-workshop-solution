package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.EmployeeMailboxCustomDTO;

import java.util.List;

/**
 * EmployeeMailboxDelegate Interface for a strategy class defining construction
 * of employee mailboxes.
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
public interface EmployeeMailboxDelegate {

	/**
	 * Retrieves an employee mailbox.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @param mailboxName
	 *            mailbox name
	 * @return EmployeeMailboxCustomDTO Employee-specific information about a
	 *         mailbox
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public EmployeeMailboxCustomDTO getMailbox(SecurityContext securityContext,
			String mailboxName) throws BankmailApplicationException;

	/**
	 * Returns mailboxes which employee can access.
	 * 
	 * @param securityContext
	 *            SecurityContext
	 * @return List<EmployeeMailboxCustomDTO> Employee-specific information
	 *         about a mailbox
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public List<EmployeeMailboxCustomDTO> getMailboxes(SecurityContext securityContext)
			throws BankmailApplicationException;

}
