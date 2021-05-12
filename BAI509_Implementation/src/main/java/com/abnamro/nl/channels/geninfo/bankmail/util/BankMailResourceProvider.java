package com.abnamro.nl.channels.geninfo.bankmail.util;

import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants;
import com.abnamro.nl.channels.geninfo.bankmail.cache.Cache;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.ItemContainer;
import com.abnamro.nl.channels.geninfo.bankmail.loader.JsonLoader;
import com.abnamro.nl.channels.geninfo.bankmail.loader.UnableToReadFileException;
import com.abnamro.nl.channels.geninfo.bankmail.mappers.MailResourceMapper;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * todo : if we no longer need the org.json lib after factoring it out of this class then we can remove it from the pom
 */
@Named
@Singleton
public class BankMailResourceProvider {
	/**
	 * todo : why not inject the logger?
	 */
	private final LogHelper LOGGER = new LogHelper(BankMailResourceProvider.class);

	private Cache cache = new Cache();
	private MailResourceMapper mapper = new MailResourceMapper();

	public BankMailResourceProvider() throws BankmailApplicationException {
		initializeCache();
	}

	private void initializeCache() throws BankmailApplicationException {
		final String LOG_METHOD = "initializeCache()";

		for(MailResources resource : MailResources.values()) {
			if(cache.get(resource.getCacheKey()) == null) {
				try {
					String json = JsonLoader.loadJson(resource.getFileName());
					ItemContainer data = mapper.map(json, resource.getType());
					cache.put(resource.getCacheKey(), data.getItems());
				} catch (UnableToReadFileException | JsonProcessingException exception) {
					LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, exception);
					Messages msgs = new Messages();
					msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
					throw new BankmailApplicationException(msgs);
				}
			}
		}
	}

	public <T> T getData(MailResources resource) {
		return cache.get(resource.getCacheKey());
	}
}


