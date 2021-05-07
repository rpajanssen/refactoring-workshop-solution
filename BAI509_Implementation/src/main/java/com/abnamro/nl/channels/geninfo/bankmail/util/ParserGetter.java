package com.abnamro.nl.channels.geninfo.bankmail.util;

import javax.swing.text.html.HTMLEditorKit;


/**
 * ParserGetter: provides the parser to use for reading HTML content. 
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	01-11-2012	Initial version	  Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
class ParserGetter extends HTMLEditorKit {
	/**
	 * getParser
	 * 
	 * @return HTMLEditorKit.Parser
	 */
	public HTMLEditorKit.Parser getParser() {
		return super.getParser();
	}
}
