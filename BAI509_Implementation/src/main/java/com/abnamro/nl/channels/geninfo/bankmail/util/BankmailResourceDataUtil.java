package com.abnamro.nl.channels.geninfo.bankmail.util;

import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants;
import com.abnamro.nl.channels.geninfo.bankmail.cache.Cache;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.*;
import com.abnamro.nl.channels.geninfo.bankmail.loader.JsonLoader;
import com.abnamro.nl.channels.geninfo.bankmail.mappers.MailTemplateMapper;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

import static com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants.*;

/**
 * todo : Since have a non-descriptive classname not helping us at all... I expected a bit of javadoc explaining the
 *        purpose and usage of this class... but is is missing...
 *
 *        Drawbacks: as a developer you always have to deep dive into the code to figure out what this class is meant
 *        for and you loose time and productive because of that, and it is never fun trying to deep dive into code you
 *        have not written trying to understand its purpose!
 *
 * todo : if we no longer need the org.json lib after factoring it out of this class then we can remove it from the pom
 */
@Named
@Singleton
public class BankmailResourceDataUtil {
	/**
	 * todo : why not inject the logger?
	 */
	private final LogHelper LOGGER = new LogHelper(BankmailResourceDataUtil.class);

	private Cache cache = new Cache();
	private MailTemplateMapper mapper = new MailTemplateMapper();



	/**
	 * todo : here we see the type safety issue exposed! The get-method returns an Object, so the consumer needs to
	 *        write code that casts it first.
	 *
	 *        Drawback: depending on how defensive that code is, it can lead to runtime exceptions.
	 *        As a developer you loose much productivity because you may only find bugs after you deployed your application
	 *        and NOT compile time (a factor 1000 less productive). And if you do not find the bug... it may lead to
	 *        a production incident. So we need to figure out a way to have type safe code
	 */
	public Object getData(String key) {
		return cache.get(key);
	}

	/**
	 * todo : doing a deep dive in the caching functionality, we observe that this class actually manages the content
	 *        of the cache... so no consumer of this class actually ever puts anything in this cache! This means that
	 *        this method should have been private! And if it's only private... why have it at all?
	 *
	 *        Drawback: illegal use of the cache functionality by developers resulting in unexpected runtime errors
	 *        because content managed by this class can be overwritten!
	 */
	public void putData(String key, Object Value) {
		cache.put(key, Value);
	}

	/**
	 * todo : there is a typo in the method name!
	 *
	 * todo : the methods suggests it retrieves/fetches information for you... but is a void?
	 *        This is a code smell of a bad method name. No javadoc... so we have to deep dive to find out
	 *        what it really does.
	 */
	public void retreiveBOData() throws BankmailApplicationException {

		/**
		 * todo : at least we log the typo in the method name as well
		 */
		final String LOG_METHOD = "retreiveBOData()";

		/**
		 * todo : here we get a clear indication that the cache is some kind of static in-memory data store where
		 *        data gets loaded only once and then is re-used for ever.
		 *        This method is called from the local - non static - "readJsons" method only so it suggest this
		 *        method is not for public use... so why have the public access modifier?
		 *        The "readJsons" method is called from service classes - using an instance of this class - many, many
		 *        times.
		 *
		 *        Drawback: exposing methods as public that should be private can lead to unwanted usage of the method
		 *        and that can lead to tightly coupled spaghetti code and that results in difficult to maintain code
		 *        and higher cost.
		 *
		 *        Drawback: since this cache is not loaded on startup, but each item is actually loaded on the first
		 *        call, the first call may be a lot slower. And if there is an error in the json files, you won't
		 *        know until a call using that file is being made, so you will find out very late. It is always best
		 *        to fail fast so if you load all json files on startup once, and stop the startup if a json fails,
		 *        to only hurt your own deployment - very fast - and not the consumers of your service!
		 */
		if (cache.get(FILTERED_BOS) == null) {
			try {
				/**
				 * todo : we see a magic string - the file name -  being used
				 */
				String json = JsonLoader.loadJson("filteredbos.json");
				FilteredBOs data = mapper.map(json, FilteredBOs.class);
				// todo : we should store the FilteredBOs instance - if we do this we need to refactor get operations
				//        to the cache fetching this specific data
				putData(FILTERED_BOS, data.getBos());
			} catch ( JsonProcessingException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}


	/**
	 * todo : see all the remarks for the "retreiveBOData" method
	 *
	 * todo : we see the body of this method is almost the same as for the "retreiveBOData" method. So we have a lot of
	 *        boilerplate code being duplicated.
	 *
	 *        Drawback: duplicating boilerplate code increase maintenance cost. If you need to change it... you need to
	 *        change it in multiple locations. Another drawback is that you run the risk of forgetting to update one
	 *        instance of the duplicated code... and now you introduced a bug that may lead to production incidents!
	 */
	public void retriveCCAMailboxTemplatePrivateData() throws BankmailApplicationException {
		final String LOG_METHOD = "retriveCCAMailboxTemplatePrivateData()";
		if (cache.get(CCA_MAILBOX_TEMPLATE_PRIVATE) == null) {
			try {
				String json = JsonLoader.loadJson("ccamailboxtemplateprivate.json");
				List<CCAMailboxTemplateJson> data = mapper.map(json, new TypeReference<List<CCAMailboxTemplateJson>>() { });
				putData(CCA_MAILBOX_TEMPLATE_PRIVATE, data);
			} catch ( JsonProcessingException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}


	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveBOMailboxTemplateData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveBOMailboxTemplateData()";
		if (cache.get(BO_MAILBOX_TEMPLATE) == null) {
			try {
				String json = JsonLoader.loadJson("bomailboxtemplate.json");
				List<BOMailTemplate> data = mapper.map(json, new TypeReference<List<BOMailTemplate>>() { });
				putData(BO_MAILBOX_TEMPLATE, data);
			} catch ( JsonProcessingException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}

	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveGenesysMailboxTemplateYbbData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveBOMailboxTemplateData()";
		if (cache.get(GENESYS_MAILBOX_TEMPLATE_YBB) == null) {
			try {
				String json = JsonLoader.loadJson("genesysmailboxtemplateybb.json");
				List<GenesysMailboxTemplateJson> data = mapper.map(json, new TypeReference<List<GenesysMailboxTemplateJson>>() { });
				putData(GENESYS_MAILBOX_TEMPLATE_YBB, data);
			} catch ( JsonProcessingException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}

	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveCCAMailboxTemplatePreferredData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveCCAMailboxTemplatePreferredData()";
		if (cache.get(CCA_MAILBOX_TEMPLATE_PREFERRED) == null) {
			try {
				String json = JsonLoader.loadJson("ccamailboxtemplatepreferred.json");
				List<CCAMailboxTemplateJson> data = mapper.map(json, new TypeReference<List<CCAMailboxTemplateJson>>() { });
				putData(CCA_MAILBOX_TEMPLATE_PREFERRED, data);
			} catch ( JsonProcessingException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}

	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveFilteredCustomerGroupsData() throws BankmailApplicationException {

		final String LOG_METHOD = "retrieveFilteredCustomerGroupsData()";

		if (cache.get(FILTERED_CUSTOMER_GROUPS) == null) {
			try {
				String json = JsonLoader.loadJson("filteredcustomergroups.json");
				// todo : we should store the FilteredBOs instance - if we do this we need to refactor get operations
				//        to the cache fetching this specific data
				//        note also that the FilteredCustomerGroups was present but never used before
				FilteredCustomerGroups data = mapper.map(json, FilteredCustomerGroups.class);
				putData(FILTERED_CUSTOMER_GROUPS, data.getcGC());
			} catch ( JsonProcessingException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}

	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveGenesysMailboxTemplateAscData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveGenesysMailboxTemplateAscData()";

		if (cache.get(GENESYS_MAILBOX_TEMPLATE_ASC) == null) {
			try {
				String json = JsonLoader.loadJson("genesysmailboxtemplateasc.json");
				List<GenesysMailboxTemplateJson> data = mapper.map(json, new TypeReference<List<GenesysMailboxTemplateJson>>() { });
				putData(GENESYS_MAILBOX_TEMPLATE_ASC, data);
			} catch ( JsonProcessingException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}

	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveServiceConceptCGCData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveServiceConceptCGCData()";

		if (cache.get(SERVICE_CONCEPT_BY_CGC) == null) {
			try {
				String json = JsonLoader.loadJson("serviceconceptbycgc.json");
				List<ServiceConceptCGC> data = mapper.map(json, new TypeReference<List<ServiceConceptCGC>>() { });
				putData(SERVICE_CONCEPT_BY_CGC, data);
			} catch ( JsonProcessingException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}

	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retreiveServiceConceptbySegmnetData() throws BankmailApplicationException {

		final String LOG_METHOD = "retreiveServiceConceptbySegmnetData()";

		if (cache.get(SERVICE_CONCEPT_BY_SEGMENT) == null) {
			try {
				String json = JsonLoader.loadJson("serviceconceptbysegment.json");
				List<ServiceConcept> data = mapper.map(json, new TypeReference<List<ServiceConcept>>() { });
				putData(SERVICE_CONCEPT_BY_SEGMENT, data);
			} catch ( JsonProcessingException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}

	/**
	 * todo : a typical piece of code that violates the open/closed principle
	 *
	 * todo : also a typical violation of the single responsibility principle
	 *
	 * todo : the above has lead to a misleading name, the consumers of this class only want one specific
	 *        mail template matching the given key, but the method name is "readJsons" which is confusing because
	 *        it suggest multiple "Jsons" - not even a proper English word - are being read and might be returned, we
	 *        don't know... weakly typed.... anything can be returned. So this is a mess. Of course no javadoc to help
	 *        out as well!
	 *
	 * todo : now that we have this method... what about the public "getData" method we saw earlier... if a developer
	 *        uses this class and then only uses the "getData" method - which makes more sense then using this method -
	 *        nothing will ever happen... the cache will remain empty... nothing will ever be returned... ohoh
	 */
	public Object readJsons(String key) throws BankmailApplicationException {

		switch (key) {
			case FILTERED_BOS: retreiveBOData();
				break;
			case CCA_MAILBOX_TEMPLATE_PRIVATE: retriveCCAMailboxTemplatePrivateData();
				break;
			case BO_MAILBOX_TEMPLATE:  retrieveBOMailboxTemplateData();
				break;
			case GENESYS_MAILBOX_TEMPLATE_YBB: retrieveGenesysMailboxTemplateYbbData();
				break;
			case CCA_MAILBOX_TEMPLATE_PREFERRED: retrieveCCAMailboxTemplatePreferredData();
				break;
			case FILTERED_CUSTOMER_GROUPS:  retrieveFilteredCustomerGroupsData();
				break;
			case GENESYS_MAILBOX_TEMPLATE_ASC: retrieveGenesysMailboxTemplateAscData();
				break;
			case SERVICE_CONCEPT_BY_CGC: retrieveServiceConceptCGCData();
				break;
			case SERVICE_CONCEPT_BY_SEGMENT:  retreiveServiceConceptbySegmnetData();
				break;

			default:
				break;
		}

		return getData(key);
	}
}


