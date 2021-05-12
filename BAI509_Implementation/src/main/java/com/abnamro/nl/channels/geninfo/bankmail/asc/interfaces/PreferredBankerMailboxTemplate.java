/**
 * 
 */
package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.CCAMailboxTemplateJson;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.GenesysMailboxTemplateJson;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankMailResourceProvider;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailMailboxTemplateParserUtil;
import com.abnamro.nl.channels.geninfo.bankmail.util.MailResources;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;

import java.util.List;

/**
 * Gets the PreferredBankerMailboxTemplate details from tridion
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
@LogInterceptorBinding
public class PreferredBankerMailboxTemplate extends CCAMailboxTemplate {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(PreferredBankerMailboxTemplate.class);

	/**
	 * PreferredBankerMailboxTemplate constructor
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public PreferredBankerMailboxTemplate() throws BankmailApplicationException {
		final String LOG_METHOD = "PreferredBankerMailboxTemplate()";

		BankmailMailboxTemplateParserUtil bankmailMailboxTemplateParserUtil = new BankmailMailboxTemplateParserUtil();
		BankMailResourceProvider bankMailResourceProvider = new BankMailResourceProvider();

		// Set the privateBankerMailboxTemplate with values from tridion
		List<CCAMailboxTemplateJson> ccaMailboxTemplateList= bankMailResourceProvider.getData(MailResources.CCA_PRIVATE_MAIL_TEMPLATE);
		LOGGER.debugHardCodedMessage(LOG_METHOD, " ccaMailboxTemplateList : LENGTH : {0}", ccaMailboxTemplateList.size());
		if (ccaMailboxTemplateList.size() > 0) {
			for (int i = 0; i < ccaMailboxTemplateList.size(); i++) {
				// get the ccaTemplateElement element
				CCAMailboxTemplateJson ccaTemplateElement=ccaMailboxTemplateList.get(i);

				// retrieve and set displayNamePrefix
				String displayNamePrefix=ccaTemplateElement.getDisplayNamePrefix();

				super.setDisplayNamePrefix(displayNamePrefix);

				// retrieve and set fallbackStrategy
				String fallbackStrategy=ccaTemplateElement.getFallbackStrategy();

				// Since fallBackStrategy is genesysmailboxTemplate get details
				GenesysMailboxTemplate fallbackTemplate = new GenesysMailboxTemplate();
				fallbackTemplate.setDisplayName(GenesysMailboxTemplate.MAILBOX_NAME);

				if (BankmailConstants.CNMB_YBB.equalsIgnoreCase(fallbackStrategy)) {
					List<GenesysMailboxTemplateJson> genesysMailboxTemplateList = bankMailResourceProvider.getData(MailResources.GENESYS_YBB_MAIL_TEMPLATE);
					fallbackTemplate = bankmailMailboxTemplateParserUtil.parseAndRetreiveGenesysMailboxTemplateYBB(genesysMailboxTemplateList);
				} else {
					List<GenesysMailboxTemplateJson> genesysMailboxTemplateASCJsons= bankMailResourceProvider.getData(MailResources.GENESYS_ASC_MAIL_TEMPLATE);
					fallbackTemplate = bankmailMailboxTemplateParserUtil.parseAndRetreiveGenesysMailboxTemplateAsc(genesysMailboxTemplateASCJsons);
				}

				if (null != fallbackTemplate) {
					super.setFallbackStrategy(fallbackTemplate);
				}

				// retrieve and set signatureTemplate
				String signatureTemplate=ccaTemplateElement.getSignatureTemplate();

				super.setSignatureTemplate(signatureTemplate);
			}
		}
	}
}
