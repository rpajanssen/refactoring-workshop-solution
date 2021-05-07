/**
 * 
 */
package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Gets the ASCMailboxDelegate details from tridion
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				29-18-2012	Initial version	  Release 1.0
 * </PRE>
 * @author Sushant Karande
 * @see
 */
//check

public class ASCMailboxDelegate extends GenesysMailboxDelegate {
	/**
	 * ASCMailboxDelegate constructor sets the value of mailboxTemplate object
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public ASCMailboxDelegate() throws BankmailApplicationException, IOException, SAXException {
		super.setMailboxTemplate(new ASCMailboxTemplate());
	}
}
