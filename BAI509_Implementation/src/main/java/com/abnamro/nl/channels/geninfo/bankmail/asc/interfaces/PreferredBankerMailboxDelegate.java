/**
 * 
 */
package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * PreferredBankerMailboxDelegate
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
public class PreferredBankerMailboxDelegate extends CCAMailboxDelegate {
	/**
	 * PreferredBankerMailboxDelegate constructor
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public PreferredBankerMailboxDelegate() throws BankmailApplicationException, IOException, SAXException {
		super.setMailboxTemplate(new PreferredBankerMailboxTemplate());
	}
}
