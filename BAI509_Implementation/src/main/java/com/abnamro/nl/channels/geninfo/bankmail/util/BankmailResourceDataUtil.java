package com.abnamro.nl.channels.geninfo.bankmail.util;

import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.*;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.json.JsonSanitizer;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants.*;


public class BankmailResourceDataUtil {

	private static Map<String, Object> cache=new ConcurrentHashMap<>();
	private static final ClassLoader classLoader = BankmailResourceDataUtil.class.getClassLoader();
	private static ObjectMapper objectMapper = new ObjectMapper();

	/** The Constant CLAZZ. used for logging */
	private static final Class<BankmailResourceDataUtil> CLASS = BankmailResourceDataUtil.class;

	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(CLASS);

	public Object getData(String key) {
		return cache.get(key);
	}

	public static void putData(String key, Object Value) {
		cache.put(key, Value);
	}

	/**
	 * Creates ObjectMapper object and initiates values
	 *
	 * @return ObjectMapper result object
	 */
	public static ObjectMapper createObjectMapper() {


		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		return objectMapper;
	}

	/**
	 * This method reads the object data and converts it into JSON
	 *
	 * @param inputData resultJson Object
	 * @return ArrayNode result Json as ArrayNode
	 * @throws IOException May thorw IO Exception
	 */
	public static ArrayNode retreiveDataSource(Object inputData,String tagName) throws IOException {

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		String jsonStr = objectMapper.writeValueAsString(inputData);
		JsonNode rootNode = objectMapper.readTree(JsonSanitizer.sanitize(jsonStr));
		return  (ArrayNode) rootNode.path(tagName);
	}




	public static void retreiveBOData() throws BankmailApplicationException {

		final String LOG_METHOD = "retreiveBOData()";

		if (!cache.containsKey(FILTERED_BOS)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(classLoader.getResourceAsStream("filteredbos.json"))) {
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				ArrayNode result = retreiveDataSource(obj, "BO");
				objectMapper = createObjectMapper();
				List<String> temp = objectMapper.readValue(JsonSanitizer.sanitize(result.toString()),
						new TypeReference<List<String>>() {
						});
				putData(FILTERED_BOS, temp);
			} catch ( IOException | ParseException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}


	public static void retriveCCAMailboxTemplatePrivateData() throws BankmailApplicationException {
		final String LOG_METHOD = "retriveCCAMailboxTemplatePrivateData()";
		if (!cache.containsKey(CCA_MAILBOX_TEMPLATE_PRIVATE)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(classLoader.getResourceAsStream("ccamailboxtemplateprivate.json"))) {
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				ArrayNode result = retreiveDataSource(obj, "CCAMailboxTemplate");
				objectMapper = createObjectMapper();
				List<CCAMailboxTemplateJson> temp = objectMapper.readValue(JsonSanitizer.sanitize(result.toString()),
						new TypeReference<List<CCAMailboxTemplateJson>>() {
						});
				putData(CCA_MAILBOX_TEMPLATE_PRIVATE, temp);
			} catch ( IOException | ParseException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}


	public static void retrieveBOMailboxTemplateData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveBOMailboxTemplateData()";
		if (!cache.containsKey(BO_MAILBOX_TEMPLATE)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(classLoader.getResourceAsStream("bomailboxtemplate.json"))) {
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				ArrayNode result = retreiveDataSource(obj, "BOMailTemplate");
				objectMapper = createObjectMapper();
				List<BOMailTemplate> temp = objectMapper.readValue(JsonSanitizer.sanitize(result.toString()),
						new TypeReference<List<BOMailTemplate>>() {
						});
				putData(BO_MAILBOX_TEMPLATE, temp);
			} catch ( IOException | ParseException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}

	public static void retrieveGenesysMailboxTemplateYbbData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveBOMailboxTemplateData()";
		if (!cache.containsKey(GENESYS_MAILBOX_TEMPLATE_YBB)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(classLoader.getResourceAsStream("genesysmailboxtemplateybb.json")))
			{
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				ArrayNode result = retreiveDataSource(obj,"GenesysMailboxTemplate");
				objectMapper = createObjectMapper();
				List<GenesysMailboxTemplateJson> temp = objectMapper.readValue(JsonSanitizer.sanitize(result.toString()),
						new TypeReference<List<GenesysMailboxTemplateJson>>() {
						});
				putData(GENESYS_MAILBOX_TEMPLATE_YBB,temp);
			} catch ( IOException | ParseException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}

		}
	}



	public static void retrieveCCAMailboxTemplatePreferredData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveCCAMailboxTemplatePreferredData()";
		if (!cache.containsKey(CCA_MAILBOX_TEMPLATE_PREFERRED)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(classLoader.getResourceAsStream("ccamailboxtemplatepreferred.json"))) {
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				ArrayNode result = retreiveDataSource(obj, "CCAMailboxTemplate");
				objectMapper = createObjectMapper();
				List<CCAMailboxTemplateJson> temp = objectMapper.readValue(JsonSanitizer.sanitize(result.toString()),
						new TypeReference<List<CCAMailboxTemplateJson>>() {
						});
				putData(CCA_MAILBOX_TEMPLATE_PREFERRED, temp);
			} catch ( IOException | ParseException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}

		}
	}


	public static void retrieveFilteredCustomerGroupsData() throws BankmailApplicationException {

		final String LOG_METHOD = "retrieveFilteredCustomerGroupsData()";

		if (!cache.containsKey(FILTERED_CUSTOMER_GROUPS)) {
			JSONParser jsonParser = new JSONParser();

			try (InputStream reader = Objects.requireNonNull(classLoader.getResourceAsStream("filteredcustomergroups.json"))) {
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				ArrayNode result = retreiveDataSource(obj, "CGC");
				objectMapper = createObjectMapper();
				List<String> temp = objectMapper.readValue(JsonSanitizer.sanitize(result.toString()),
						new TypeReference<List<String>>() {
						});
				putData(FILTERED_CUSTOMER_GROUPS, temp);
			} catch ( IOException | ParseException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}

		}
	}


	public static void retrieveGenesysMailboxTemplateAscData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveGenesysMailboxTemplateAscData()";

		if (!cache.containsKey(GENESYS_MAILBOX_TEMPLATE_ASC)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(classLoader.getResourceAsStream("genesysmailboxtemplateasc.json"))) {
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				ArrayNode result = retreiveDataSource(obj, "GenesysMailboxTemplate");
				objectMapper = createObjectMapper();
				List<GenesysMailboxTemplateJson> temp = objectMapper.readValue(JsonSanitizer.sanitize(result.toString()),
						new TypeReference<List<GenesysMailboxTemplateJson>>() {
						});
				putData(GENESYS_MAILBOX_TEMPLATE_ASC, temp);

			} catch ( IOException | ParseException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}


	public static void retrieveServiceConceptCGCData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveServiceConceptCGCData()";

		if (!cache.containsKey(SERVICE_CONCEPT_BY_CGC)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(classLoader.getResourceAsStream("serviceconceptbycgc.json"))) {
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				ArrayNode result = retreiveDataSource(obj, "ServiceConcept");
				objectMapper = createObjectMapper();
				List<ServiceConceptCGC> temp = objectMapper.readValue(JsonSanitizer.sanitize(result.toString()),
						new TypeReference<List<ServiceConceptCGC>>() {
						});
				putData(SERVICE_CONCEPT_BY_CGC, temp);

			} catch ( IOException | ParseException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}


	public static void retreiveServiceConceptbySegmnetData() throws BankmailApplicationException {

		final String LOG_METHOD = "retreiveServiceConceptbySegmnetData()";

		if (!cache.containsKey(SERVICE_CONCEPT_BY_SEGMENT)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(classLoader.getResourceAsStream("serviceconceptbysegment.json"))) {
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				ArrayNode result = retreiveDataSource(obj, "ServiceConcept");
				objectMapper = createObjectMapper();
				List<ServiceConcept> temp = objectMapper.readValue(JsonSanitizer.sanitize(result.toString()),
						new TypeReference<List<ServiceConcept>>() {
						});
				putData(SERVICE_CONCEPT_BY_SEGMENT, temp);

			} catch ( IOException | ParseException e) {
				LOGGER.error(LOG_METHOD, BankmailConstants.BANKMAIL_JSON_DATA_ISSUE, e);
				Messages msgs = new Messages();
				msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNEXPECTED_EXCEPTION), MessageType.getError());
				throw new BankmailApplicationException(msgs);
			}
		}
	}

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


