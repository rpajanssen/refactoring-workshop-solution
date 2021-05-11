/**
 *
 */
package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.GenesysMailboxTemplateJson;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankMailResourceProvider;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailMailboxTemplateParserUtil;
import com.abnamro.nl.channels.geninfo.bankmail.util.MailResources;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Gets the ASCMailboxTemplate details from tridion
 * @author 534878
 */
@LogInterceptorBinding
@Named
@Singleton
public class ASCMailboxTemplate extends GenesysMailboxTemplate {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;	
	

	@Inject
	private BankmailMailboxTemplateParserUtil bankmailMailboxTemplateParserUtil;

	@Inject
	private BankMailResourceProvider bankMailResourceProvider;

	/**
	 * ASCMailboxTemplate constructor
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public ASCMailboxTemplate() throws BankmailApplicationException {

		List<GenesysMailboxTemplateJson> genesysAscTemplateFromTridion= bankMailResourceProvider.getData(MailResources.GENESYS_ASC_MAIL_TEMPLATE);

				// Set the mailboxTemplate with values from tridion
			GenesysMailboxTemplate ascTemplateFromTridion = bankmailMailboxTemplateParserUtil
					.parseAndRetreiveGenesysMailboxTemplateAsc(genesysAscTemplateFromTridion);

			// set the values in the GenesysMailboxTemplate
			super.setDisplayName(ascTemplateFromTridion.getDisplayName());		
			super.setSignature(ascTemplateFromTridion.getSignature());

	}	
}
