/**
 * 
 */
package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * CSUMailboxTemplate
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * Sushant Karande  	29-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author Sushant Karande
 * @see
 */
public class CSUMailboxDelegate extends BOMailboxDelegate {

	/**
	 * CSUMailboxDelegate constructor
	 * @throws BankmailApplicationException BankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public CSUMailboxDelegate() throws BankmailApplicationException, IOException, SAXException {
		super.setMailboxTemplate(new CSUMailboxTemplate());
	}
}
