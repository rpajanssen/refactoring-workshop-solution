/**
 * 
 */
package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * YBBMailboxDelegate
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				01-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
public class YBBMailboxDelegate extends GenesysMailboxDelegate {

	/**
	 * YBBMailboxDelegate constructor
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public YBBMailboxDelegate() throws BankmailApplicationException, IOException, SAXException {
		super.setMailboxTemplate(new YBBMailboxTemplate());
	}
}
