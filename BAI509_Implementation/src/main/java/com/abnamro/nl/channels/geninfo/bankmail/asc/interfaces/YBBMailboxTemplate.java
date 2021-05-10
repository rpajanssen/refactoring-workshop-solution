/**
 * 
 */
package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.GenesysMailboxTemplateJson;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailMailboxTemplateParserUtil;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailResourceDataUtil;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

import static com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants.GENESYS_MAILBOX_TEMPLATE_YBB;

/**
 * Gets the YBBMailboxTemplate details from tridion
 * @author 534878
 */
@LogInterceptorBinding
@Named
@Singleton
public class YBBMailboxTemplate extends GenesysMailboxTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private BankmailMailboxTemplateParserUtil bankmailMailboxTemplateParserUtil;

	@Inject
	private BankmailResourceDataUtil bankmailResourceDataUtil;

	/**
	 * YBBMailboxTemplate constructor
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public YBBMailboxTemplate() throws BankmailApplicationException {
		List<GenesysMailboxTemplateJson> genesysMailboxTemplateList = (List<GenesysMailboxTemplateJson>) bankmailResourceDataUtil.getData(GENESYS_MAILBOX_TEMPLATE_YBB);

			// Set the mailboxTemplate with values from tridion
			GenesysMailboxTemplate ybbTemplateFromTridion = bankmailMailboxTemplateParserUtil
				.parseAndRetreiveGenesysMailboxTemplateYBB(genesysMailboxTemplateList);
			super.setDisplayName(ybbTemplateFromTridion.getDisplayName());
			super.setSignature(ybbTemplateFromTridion.getSignature());

	}
	
}
