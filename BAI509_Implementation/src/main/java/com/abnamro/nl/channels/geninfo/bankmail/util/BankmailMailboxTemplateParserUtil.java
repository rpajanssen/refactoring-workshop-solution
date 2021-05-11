package com.abnamro.nl.channels.geninfo.bankmail.util;

import com.abnamro.nl.channels.geninfo.bankmail.asc.implementation.BankmailEmployeeASCImpl;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BOMailboxTemplate;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.GenesysMailboxTemplate;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.BOMailTemplate;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.GenesysMailboxTemplateJson;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.List;

/**
 * Parses the BankmailMailboxTemplate
 * @author C59896
 */
@LogInterceptorBinding
@Named
@Singleton
public class BankmailMailboxTemplateParserUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(BankmailEmployeeASCImpl.class);

	@Inject
	private BankmailResourceDataUtil bankmailResourceDataUtil;

	/**
	 * parseAndRetreiveGenesysMailboxTemplate : This method parses genesysMailboxTemplate From Tridion
	 * @param genesysMailboxTemplateFromTridion XMl Document Object
	 * @return genesysMailboxTemplate GenesysMailboxTemplate
	 * @throws BankmailApplicationException Application exception
	 */
	public GenesysMailboxTemplate parseAndRetreiveGenesysMailboxTemplate(Document genesysMailboxTemplateFromTridion)
			throws BankmailApplicationException {
		GenesysMailboxTemplate genesysMailboxTemplate = new GenesysMailboxTemplate();
		final String LOG_METHOD = "parseAndRetreiveGenesysMailboxTemplate(Document): GenesysMailboxTemplate";
		NodeList genesysMailboxTemplateList = genesysMailboxTemplateFromTridion
			.getElementsByTagName(BankmailConstants.GENESYS_MAILBOX_TEMPLATE);
		LOGGER.debugHardCodedMessage(LOG_METHOD, " genesysMailboxTemplateList : LENGTH :{0}", genesysMailboxTemplateList.getLength());
		if (genesysMailboxTemplateList.getLength() > 0) {

			for (int i = 0; i < genesysMailboxTemplateList.getLength(); i++) {
				// get the genesysTemplateElement element
				Element genesysTemplateElement = (Element) genesysMailboxTemplateList.item(i);

				// retrieve and set display name
				String displayName = getTextValue(genesysTemplateElement, BankmailConstants.DISPLAYNAME);
				genesysMailboxTemplate.setDisplayName(displayName);

				// retrieve and set signature
				String signature = getTextValue(genesysTemplateElement, BankmailConstants.SIGNATURE);
				genesysMailboxTemplate.setSignature(signature);
			}
		}
		return genesysMailboxTemplate;
	}

//For YBB GenesysMailboxtemplate
	public GenesysMailboxTemplate parseAndRetreiveGenesysMailboxTemplateYBB(List<GenesysMailboxTemplateJson> genesysMailboxTemplateList)
			throws BankmailApplicationException {
		GenesysMailboxTemplate genesysMailboxTemplate = new GenesysMailboxTemplate();
		final String LOG_METHOD = "parseAndRetreiveGenesysMailboxTemplate(Document): GenesysMailboxTemplate";

		LOGGER.debugHardCodedMessage(LOG_METHOD, " genesysMailboxTemplateList : LENGTH :{0}", genesysMailboxTemplateList.size());
		if (genesysMailboxTemplateList.size() > 0) {

			for (int i = 0; i < genesysMailboxTemplateList.size(); i++) {
				// get the genesysTemplateElement element
				GenesysMailboxTemplateJson genesysTemplateElement=genesysMailboxTemplateList.get(i);

				// retrieve and set display name
				String displayName=genesysTemplateElement.getDisplayName();
				genesysMailboxTemplate.setDisplayName(displayName);

				// retrieve and set signature
				String signature=genesysTemplateElement.getSignature();
				genesysMailboxTemplate.setSignature(signature);
			}
		}
		return genesysMailboxTemplate;
	}


	//for ASC GensysMailboxtemplate
	public GenesysMailboxTemplate parseAndRetreiveGenesysMailboxTemplateAsc(List<GenesysMailboxTemplateJson>  genesysMailboxTemplateList)
			throws BankmailApplicationException {
		GenesysMailboxTemplate genesysMailboxTemplate = new GenesysMailboxTemplate();
		final String LOG_METHOD = "parseAndRetreiveGenesysMailboxTemplate(Document): GenesysMailboxTemplate";

		LOGGER.debugHardCodedMessage(LOG_METHOD, " genesysMailboxTemplateList : LENGTH :{0}", genesysMailboxTemplateList.size());
		if (genesysMailboxTemplateList.size() > 0) {

			for (int i = 0; i < genesysMailboxTemplateList.size(); i++) {
				// get the genesysTemplateElement element
				GenesysMailboxTemplateJson genesysTemplateElement=genesysMailboxTemplateList.get(i);

				// retrieve and set display name
				String displayName=genesysTemplateElement.getDisplayName();
				genesysMailboxTemplate.setDisplayName(displayName);

				// retrieve and set signature
				String signature=genesysTemplateElement.getSignature();
				genesysMailboxTemplate.setSignature(signature);
			}
		}
		return genesysMailboxTemplate;
	}

	/**
	 * parseAndRetreiveBOMailboxTemplate : This method parses BOMailboxTemplate From Tridion
	 * @return boMailboxTemplate BOMailboxTemplate
	 * @throws BankmailApplicationException Application exception
	 */
	public BOMailboxTemplate parseAndRetreiveBOMailboxTemplate()
			throws BankmailApplicationException {
		BOMailboxTemplate boMailboxTemplate = new BOMailboxTemplate();
		final String LOG_METHOD = "parseAndRetreiveBOMailboxTemplate(Document): BOMailboxTemplate";
		List<GenesysMailboxTemplateJson> genesysMailboxTemplateYBBJsons=null;
		List<GenesysMailboxTemplateJson> genesysMailboxTemplateASCJsons=null;

	List<BOMailTemplate> boMailboxTemplateList = (List<BOMailTemplate>) bankmailResourceDataUtil.getData(MailResources.BO_MAIL_TEMPLATE.getCacheKey());

			LOGGER.debugHardCodedMessage(LOG_METHOD, " boMailboxTemplateList : LENGTH :{0}", boMailboxTemplateList.size());
			if (boMailboxTemplateList.size() > 0) {

				for (int i = 0; i < boMailboxTemplateList.size(); i++) {
					// get the genesysTemplateElement element
					BOMailTemplate boTemplateElement=boMailboxTemplateList.get(i);
					
					// retrieve and set display name
					String displayName=boTemplateElement.getDisplayName();
					boMailboxTemplate.setDisplayName(displayName);

					// retrieve and set signature
					String signature=boTemplateElement.getSignature();
					boMailboxTemplate.setSignature(signature);

					String fallbackStrategy=boTemplateElement.getFallbackStrategy();

					// Since fallBackStrategy is genesysmailboxTemplate get details
					GenesysMailboxTemplate fallbackTemplate = new GenesysMailboxTemplate();
					fallbackTemplate.setDisplayName(GenesysMailboxTemplate.MAILBOX_NAME);

					if (BankmailConstants.CNMB_YBB.equalsIgnoreCase(fallbackStrategy)) {
						genesysMailboxTemplateYBBJsons= (List<GenesysMailboxTemplateJson>) bankmailResourceDataUtil.getData(MailResources.GENESYS_YBB_MAIL_TEMPLATE.getCacheKey());
						fallbackTemplate = parseAndRetreiveGenesysMailboxTemplateYBB(genesysMailboxTemplateYBBJsons);


					} else {
						genesysMailboxTemplateASCJsons= (List<GenesysMailboxTemplateJson>) bankmailResourceDataUtil.getData(MailResources.GENESYS_ASC_MAIL_TEMPLATE.getCacheKey());
						fallbackTemplate = parseAndRetreiveGenesysMailboxTemplateAsc(genesysMailboxTemplateASCJsons);
					}
					if (null != fallbackTemplate) {
						boMailboxTemplate.setFallbackStrategy(fallbackTemplate);
					}
				}
			}
			return boMailboxTemplate;
		}

	/**
	 * getTextValue: It takes a xml element and the tag name, look for the tag and get the text content i.e for
	 * <employee><name>John</name></employee> xml snippet if the Element points to employee node and tagName is 'name' It
	 * will return John
	 * @param ele Element
	 * @param tagName String
	 * @return String tag value
	 */
	public String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			if(el.getFirstChild()!=null) {
				textVal = el.getFirstChild().getNodeValue();
			}
		}
		return textVal;
	}

}
