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

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants.*;

/**
 * todo : The class name : it ends with "Util" and any has no specific information on its purpose, so that is a code
 *        smell. It often indicates the class has to many responsibilities (or it's just a bad class name)
 *        in this case... there are too many responsibilities. It operates as some kind of cache, a file loader, an
 *        object mapper and does some "service" things, so we know we could extract at least four classes from it.
 *
 *        Drawbacks: this class will be harder to test, it will not be re-usable, it will be harder to maintain/extend,
 *        it will always take more time to find the piece of code you are looking for (more lines of code to browse through).
 *        The risk of bugs is higher. You will have more production incidents with code like this. Teams working on
 *        this class and runningthis code in production loose productivity because all of the above.
 *
 * todo : Since have a non-descriptive classname not helping us at all... I expected a bit of javadoc explaining the
 *        purpose and usage of this class... but is is missing...
 *
 *        Drawbacks: as a developer you always have to deep dive into the code to figure out what this class is meant
 *        for and you loose time and productive because of that, and it is never fun trying to deep dive into code you
 *        have not written trying to understand its purpose!
 */
@Named
@Singleton
public class BankmailResourceDataUtil {

	/**
	 * todo : the cache is implemented by a concurrent hashmap, but you have to ask yourselves why? If you investigate
	 *        the usage you'll see it stores a very small number of mail template object (5 maybe 10).
	 *
	 * todo : this cache is weakly typed (it stores instances of Object), this is a code smell!
	 *
	 *        Drawbacks: it may lead to type unsafe code, and that may lead to runtime exceptions and production
	 *        incidents. It also leads to ugly casts in your code!
	 */
	private Map<String, Object> cache=new ConcurrentHashMap<>();

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * todo : why not inject the logger?
	 * todo : is javadoc like below helpful, does it have any value? Or does it slow you down and wastes valuable space?
	 */
	/**
	 * Instantiate the Logger
	 */
	private final LogHelper LOGGER = new LogHelper(BankmailResourceDataUtil.class);

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
	 * todo : is this javadoc help full? The first part repeats what the method name already implies so that part
	 *        is useless. The second parts says it "initiates values"... helpful? Do you know what is happening
	 *        now?
	 *
	 *        Drawbacks: this kind of javadoc brings you down into a bad mood, which is never good. It wastes your time,
	 *        takes up valuable space in the code and makes the whole team less productive.
	 *
	 * todo : why the empty lines?
	 *
	 *        Drawbacks: inconsistent sloppy code formatting makes code harder to read. In this case it takes up space
	 *        and requires you to scroll more to read all the code. Do you think it is easier and faster to understand
	 *        a piece of code if you don't have to scroll a lot?
	 *
	 * todo : this method initiates the "objectMapper" static class variable, but it also returns it? Why? We would
	 *        expect either a void, or a method return an instance of a locally created variable? This is a code
	 *        smell!
	 *
	 *        Drawback: the state of this instance is modified as a hidden side effect of this method that the developer
	 *        may not know about! This is a disaster waiting to happen.
	 */
	/**
	 * Creates ObjectMapper object and initiates values
	 *
	 * @return ObjectMapper result object
	 */
	public ObjectMapper createObjectMapper() {


		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		return objectMapper;
	}

	/**
	 * todo : there is a typo in the method name!
	 * todo : it is not threadsafe! It modifies the state of a static class variable (problem would have been the
	 *        same if it was in instance variable) before using it. If this variable is concurrently used we have
	 *        a problem. So this is a programming BUG!
	 *
	 *        Drawback: leads to unexpected, unpredictable and inconsistent behavior of your application and will
	 *        lead to production incidents!
	 *
	 * todo : a deeper look into this thread safety issue may have you puzzled since the offending line of code
	 *               "objectMapper.enable(SerializationFeature.INDENT_OUTPUT);"
	 *        is part of the "createObjectMapper" method... so why have it in here again? It smells fishy!
	 *
	 * todo : the javadoc says it reads "object data", but what is the object data... and is it actually being read?
	 *        Or has it already been read maybe and this method does not read it at all? Looking at the code it
	 *        only seems to write something... so that is confusing! And... the javadoc says it "converts it to a JSON"
	 *        but the return type is an ArrayNode (???) and certainly not a JSON string?!?
	 *
	 *        Drawback: when the javadoc is incorrect or confusing it will cost developers a lot of productivity or
	 *        it will lead to runtime exceptions, certainly if the methods signature is weakly typed!
	 *
	 *  todo : the javadoc suggest this method has one argument, but it has two!
	 *
	 *        Drawback: what is correct, what is wrong? Is the javadoc buggy or is he code buggy. Their are no unit test
	 *        so no one knows! This is typically a piece of code no developer wants to touch because it is unclear
	 *        how it should work. A really bad situation!
	 *
	 *  todo : we are very confused right now because we see the ArrayNode, part of a Json (de)serialisation library
	 *         but we remember we have an ObjectMapper instance for another Json (de)serialisation library in this
	 *         class as well... so what is happening here... why are these two libs that have the same purpose
	 *         being used together in this class?
	 *
	 *         Drawback: code smell for overly complex code and mal-used libs. Unnecessary dependencies are added to
	 *         to your project that all require LCM which takes up time. You have a higher risk of running into a
	 *         security vulnerability in a third party lib that prevents you from deploying to production!
	 *
	 *  todo : why is this method followed by multiple empty lines?
	 *
	 *        Drawback : takes up valuable space, developers have to scroll more and loose productivity because of that.
	 */
	/**
	 * This method reads the object data and converts it into JSON
	 *
	 * @param inputData resultJson Object
	 * @return ArrayNode result Json as ArrayNode
	 * @throws IOException May thorw IO Exception
	 */
	public ArrayNode retreiveDataSource(Object inputData,String tagName) throws IOException {

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		String jsonStr = objectMapper.writeValueAsString(inputData);
		JsonNode rootNode = objectMapper.readTree(JsonSanitizer.sanitize(jsonStr));
		return  (ArrayNode) rootNode.path(tagName);
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
		if (!cache.containsKey(FILTERED_BOS)) {
			JSONParser jsonParser = new JSONParser();
			/**
			 * todo : we see a magic string - the file name -  being used
			 */
			try (InputStream reader = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("filteredbos.json"))) {
				//Read JSON file
				Object obj = jsonParser.parse(new BufferedReader(new InputStreamReader(reader)));
				/**
				 * todo : we see a magic string - the tagName name -  being used
				 *
				 * todo : here we see the combined usage of the two (de)serialization libraries. The problem is that
				 *        we don't know how it actually works? Nothing has been described in javadoc!
				 *        If we search in this code base for the file name "filteredbos.json" we find a hit... luckily!
				 *        This json file name has one property - with a bad name "BO" violating naming conventions -
				 *        that holds a list of strings. If we search the code for "BO" we find a java model class
				 *        FilteredBOs that is representation of the content of that Json file. One small additional
				 *        problem is that this class seems to be unused?!?!
				 *        And we see here that we pas a list of string to the cache instead of the model class, why?
				 *
				 *        At least it looks like we unnecessarily use the gson lib and we could have sufficed by only
				 *        using the Jackson object mapper to de-serialize the json file to the now unused model class,
				 *        and store that in the cache.
				 */
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
		if (!cache.containsKey(CCA_MAILBOX_TEMPLATE_PRIVATE)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("ccamailboxtemplateprivate.json"))) {
				/**
				 * todo : see all remarks as in "retreiveBOData" method. But here we see the model class is being unsed.
				 *        First we use gson to de-serialize to a gson model and then jackson to de-serialize further to
				 *        our actual model. We are now pretty sure we can do without gson!
				 */
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


	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveBOMailboxTemplateData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveBOMailboxTemplateData()";
		if (!cache.containsKey(BO_MAILBOX_TEMPLATE)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("bomailboxtemplate.json"))) {
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

	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveGenesysMailboxTemplateYbbData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveBOMailboxTemplateData()";
		if (!cache.containsKey(GENESYS_MAILBOX_TEMPLATE_YBB)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("genesysmailboxtemplateybb.json")))
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



	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveCCAMailboxTemplatePreferredData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveCCAMailboxTemplatePreferredData()";
		if (!cache.containsKey(CCA_MAILBOX_TEMPLATE_PREFERRED)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("ccamailboxtemplatepreferred.json"))) {
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


	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveFilteredCustomerGroupsData() throws BankmailApplicationException {

		final String LOG_METHOD = "retrieveFilteredCustomerGroupsData()";

		if (!cache.containsKey(FILTERED_CUSTOMER_GROUPS)) {
			JSONParser jsonParser = new JSONParser();

			try (InputStream reader = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("filteredcustomergroups.json"))) {
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


	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveGenesysMailboxTemplateAscData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveGenesysMailboxTemplateAscData()";

		if (!cache.containsKey(GENESYS_MAILBOX_TEMPLATE_ASC)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("genesysmailboxtemplateasc.json"))) {
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


	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retrieveServiceConceptCGCData() throws BankmailApplicationException {
		final String LOG_METHOD = "retrieveServiceConceptCGCData()";

		if (!cache.containsKey(SERVICE_CONCEPT_BY_CGC)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("serviceconceptbycgc.json"))) {
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


	/**
	 * todo : see all the remarks for the "retriveCCAMailboxTemplatePrivateData" method
	 */
	public void retreiveServiceConceptbySegmnetData() throws BankmailApplicationException {

		final String LOG_METHOD = "retreiveServiceConceptbySegmnetData()";

		if (!cache.containsKey(SERVICE_CONCEPT_BY_SEGMENT)) {
			JSONParser jsonParser = new JSONParser();
			try (InputStream reader = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("serviceconceptbysegment.json"))) {
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


