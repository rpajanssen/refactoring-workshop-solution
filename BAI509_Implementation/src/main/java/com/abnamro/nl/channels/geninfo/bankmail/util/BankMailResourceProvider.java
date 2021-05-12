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

/**
 * This class provides mail resources from a cache. This cache should have only one instance and is static for that
 * reason.
 *
 * If we refactor this class into a managed bean with scope singleton then we can make the cache an instance variable
 * and instantiate it in the constructor.
 */
public class BankMailResourceProvider {
	private static final Cache cache = new Cache();

	private final LogHelper LOGGER = new LogHelper(BankMailResourceProvider.class);

	private MailResourceMapper mapper;

	public BankMailResourceProvider() throws BankmailApplicationException {
		mapper = new MailResourceMapper();

		initializeCache();
	}

	private void initializeCache() throws BankmailApplicationException {
		for(MailResources resource : MailResources.values()) {
			if(resourceNotLoaded(resource)) {
				loadResource(resource);
			}
		}
	}

	private boolean resourceNotLoaded(MailResources resource) {
		return cache.get(resource.getCacheKey()) == null;
	}

	private void loadResource(MailResources resource) throws BankmailApplicationException {
		final String LOG_METHOD = "initializeCache()";

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

	public <T> T getData(MailResources resource) {
		return cache.get(resource.getCacheKey());
	}
}


