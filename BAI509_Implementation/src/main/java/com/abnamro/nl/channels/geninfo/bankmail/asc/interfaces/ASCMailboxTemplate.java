/**
 *
 */
package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.GenesysMailboxTemplateJson;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailMailboxTemplateParserUtil;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailResourceDataUtil;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;

import java.util.List;

import static com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants.GENESYS_MAILBOX_TEMPLATE_ASC;

/**
 * Gets the ASCMailboxTemplate details from tridion
 * @author 534878
 */
@LogInterceptorBinding
public class ASCMailboxTemplate extends GenesysMailboxTemplate {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;	
	

	private BankmailMailboxTemplateParserUtil bankmailMailboxTemplateParserUtil = new BankmailMailboxTemplateParserUtil();
	/**
	 * ASCMailboxTemplate constructor
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public ASCMailboxTemplate() throws BankmailApplicationException {
BankmailResourceDataUtil bankmailResourceDataUtil=new BankmailResourceDataUtil() ;
List<GenesysMailboxTemplateJson> genesysAscTemplateFromTridion= (List<GenesysMailboxTemplateJson>) bankmailResourceDataUtil.readJsons(GENESYS_MAILBOX_TEMPLATE_ASC);

				// Set the mailboxTemplate with values from tridion
			GenesysMailboxTemplate ascTemplateFromTridion = bankmailMailboxTemplateParserUtil
					.parseAndRetreiveGenesysMailboxTemplateAsc(genesysAscTemplateFromTridion);

			// set the values in the GenesysMailboxTemplate
			super.setDisplayName(ascTemplateFromTridion.getDisplayName());		
			super.setSignature(ascTemplateFromTridion.getSignature());

	}	
}