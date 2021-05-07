/**
 * com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces
 */

package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.EmployeeMailboxCustomDTO;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * GenesysROMailboxDelegate : Defines read-only mailbox for employees working with Genesys
 *
 * <PRE>
 * <B>History:</B>
 * Developer         Date            Change Reason     Change
 * ----------------- --------------- ----------------- ----------------------------------------------
 * TCS	          	Dec 3, 2013   	 Initial version
 *
 * </PRE>
 * @author  TCS
 * @version $version 1.0 $
 */
@LogInterceptorBinding
public class GenesysROMailboxDelegate extends GenesysMailboxDelegate {

	/**
	 * Default constructor: initialize the GenesysMailboxTemplate
	 * @throws BankmailApplicationException if configuration is not present in tridion
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public GenesysROMailboxDelegate() throws BankmailApplicationException, IOException, SAXException {

		GenesysMailboxTemplate template = readAndParseXML();

		super.setMailboxTemplate(template);
	}

	/**
	 * Retrieves an employee mailbox.
	 * @param securityContext SecurityContext
	 * @param mailboxName mailbox name
	 * @return EmployeeMailboxCustomDTO Employee-specific information about a mailbox
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public EmployeeMailboxCustomDTO getMailbox(SecurityContext securityContext, String mailboxName)
		throws BankmailApplicationException {
		EmployeeMailboxCustomDTO employeeMailboxCustomDTO = null;
		employeeMailboxCustomDTO = super.getMailbox(securityContext, mailboxName);
		employeeMailboxCustomDTO.setIsCreateAllowed(Boolean.FALSE);
		return employeeMailboxCustomDTO;
	}

	/**
	 * @return This method is added to get the OPS signature and display name
	 * @throws BankmailApplicationException in case XML template is not configured in tridion.
	 */
	private GenesysMailboxTemplate readAndParseXML() throws BankmailApplicationException, IOException, SAXException {


//remove the below code when you are doing GHIA changes
			return new GenesysMailboxTemplate();


	}
}
