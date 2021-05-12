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

import java.util.List;

/**
 * Gets the YBBMailboxTemplate details from tridion
 * @author 534878
 */
@LogInterceptorBinding
public class YBBMailboxTemplate extends GenesysMailboxTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * YBBMailboxTemplate constructor
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public YBBMailboxTemplate() throws BankmailApplicationException {
		BankmailMailboxTemplateParserUtil bankmailMailboxTemplateParserUtil = new BankmailMailboxTemplateParserUtil();
		BankMailResourceProvider bankMailResourceProvider = new BankMailResourceProvider();

		List<GenesysMailboxTemplateJson> genesysMailboxTemplateList = bankMailResourceProvider.getData(MailResources.GENESYS_YBB_MAIL_TEMPLATE);

		// Set the mailboxTemplate with values from tridion
		GenesysMailboxTemplate ybbTemplateFromTridion = bankmailMailboxTemplateParserUtil.parseAndRetreiveGenesysMailboxTemplateYBB(genesysMailboxTemplateList);
		super.setDisplayName(ybbTemplateFromTridion.getDisplayName());
		super.setSignature(ybbTemplateFromTridion.getSignature());
	}
	
}
