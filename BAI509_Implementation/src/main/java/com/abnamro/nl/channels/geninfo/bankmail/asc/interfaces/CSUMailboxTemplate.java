/**
 * 
 */
package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailMailboxTemplateParserUtil;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
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
@LogInterceptorBinding
@Named
@Singleton
public class CSUMailboxTemplate extends BOMailboxTemplate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private BankmailMailboxTemplateParserUtil bankmailMailboxTemplateParserUtil;

	/**
	 * CSUMailboxTemplate constructor
	 * @throws BankmailApplicationException bankmailApplicationException
	 * @throws IOException ioException
	 * @throws SAXException saxException
	 */
	public CSUMailboxTemplate() throws BankmailApplicationException, IOException, SAXException {
			// Set the mailboxTemplate with values from tridion
			BOMailboxTemplate boMailboxTemplate = bankmailMailboxTemplateParserUtil
				.parseAndRetreiveBOMailboxTemplate();
			super.setDisplayName(boMailboxTemplate.getDisplayName());
			super.setFallbackStrategy(boMailboxTemplate.getFallbackStrategy());
			super.setSignature(boMailboxTemplate.getSignature());			

	}	
}
