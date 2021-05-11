package com.abnamro.nl.channels.geninfo.bankmail.asc.implementation;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.msec.integrationservices.ContUsershipsResult;
import com.abnamro.msec.integrationservices.ContractUsership;
import com.abnamro.msec.integrationservices.MSecException;
import com.abnamro.msec.integrationservices.SecurityAgent;
import com.abnamro.msec.integrationservices.services.SecurityAgentFactory;
import com.abnamro.nl.branch.service.bosearchandretrieval.interfaces.BOSearchAndRetrievalService;
import com.abnamro.nl.branch.service.bosearchandretrieval.interfaces.BOSearchAndRetrievalServiceException;
import com.abnamro.nl.branch.service.bosearchandretrieval.interfaces.BOStructureLookupInputDTO;
import com.abnamro.nl.branch.service.bosearchandretrieval.interfaces.BOStructureLookupOutputDTO;
import com.abnamro.nl.businesscontactgroup.service.businesscontact.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailContactCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.SendBankmailPolicyDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.SendBankmailPolicyListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.GenesysMailboxTemplateJson;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.ServiceConcept;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.ServiceConceptCGC;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailMailboxTemplateParserUtil;
import com.abnamro.nl.channels.geninfo.bankmail.util.BankmailResourceDataUtil;
import com.abnamro.nl.channels.geninfo.bankmail.util.MailResources;
import com.abnamro.nl.health.exceptions.HealthCheckException;
import com.abnamro.nl.health.interfaces.HealthCheckable;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import com.abnamro.nl.security.util.SecurityAgentUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.ejb.EJB;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.abnamro.nl.channels.geninfo.bankmail.asc.implementation.BankmailASCLogConstants.LOG_BO_SEARCH_RETRIEVAL_SERVICE_EXCEPTION;

/**
 * SendBankmailPolicyASCImpl This implementation class for 'Application Specific Controller' handles all business logic
 * to related to SendBankmailPolicy.
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-08-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
@LogInterceptorBinding
public class SendBankmailPolicyASCImpl implements SendBankmailPolicyASC, HealthCheckable {

	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(SendBankmailPolicyASCImpl.class);

	/** bankmailMailboxTemplateParserUtil util class object **/
	@Inject
	private BankmailMailboxTemplateParserUtil bankmailMailboxTemplateParserUtil;

	
	@EJB(lookup = "java:global/BS001_BusinessContactService/BS001_EJB/BusinessContactServiceImpl!com.abnamro.nl.businesscontactgroup.service.businesscontact.interfaces.BusinessContactService")
	private BusinessContactService businessContactService;

	@EJB(lookup = "java:global/BS130_BOSearchAndRetrievalService/BSI130_EJB/BOSearchAndRetrievalServiceImpl!com.abnamro.nl.branch.service.bosearchandretrieval.interfaces.BOSearchAndRetrievalService")
	private BOSearchAndRetrievalService boSearchAndRetrievalService;

	@Inject
	private BankmailResourceDataUtil bankmailResourceDataUtil;

//	@Inject

	/**
	 * Retrieves SendBankmailPolicy for a customer. If applyRolloutFilter is false, no filtering is applied. If no
	 * messages can be sent concerning given customer, null is returned.
	 * @param securityContext securityContext
	 * @param bcNumber String
	 * @param applyRolloutFilter boolean
	 * @return sendBankmailPolicyDTO SendBankmailPolicyDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public SendBankmailPolicyDTO getSendBankmailPolicy(SecurityContext securityContext, String bcNumber, boolean applyRolloutFilter)
			throws BankmailApplicationException, IOException, SAXException {
		final String LOG_METHOD = "getSendBankmailPolicy(securityContext, String, boolean):SendBankmailPolicyDTO";
		SendBankmailPolicyDTO sendBankmailPolicyDTO = null;
		SecurityAgent securityAgent = null;
		ContUsershipsResult contUsershipsResult = null;

		try {
			securityAgent = SecurityAgentFactory.getSecurityAgent();
			// retrieve the contUsershipsResult for the current representative
			contUsershipsResult = SecurityAgentUtils.getContUsershipsForUser(securityAgent,BankmailConstants.MSEC_APP_NAME, securityContext
				.getSessionSpecification());
			LOGGER.debugHardCodedMessage(LOG_METHOD, "Contract UsershipsResult :: result code : {0}", contUsershipsResult.getResultCode());
			if (contUsershipsResult.getResultCode() == ContUsershipsResult.SUCCESS) {
				// for each user in the ContractUsership list retrieve the SendbankmailPolicy
				for (ContractUsership users : contUsershipsResult.getContUserships()) {
					LOGGER.debugHardCodedMessage(LOG_METHOD, "users: {0} :", users.getBcOwner(), users.getUsership());
					// If bcNumber Exists in the contUsershipsResult list return bankmailPolicyDTO
					if (null != users.getBcOwner() && users.getBcOwner().equalsIgnoreCase(bcNumber)) {
						// If applyRolloutFilter is false, no filtering is applied.
						sendBankmailPolicyDTO = retrieveSendBankmailPolicy(bcNumber, applyRolloutFilter);
						return sendBankmailPolicyDTO;
					}
				}
			}
		} catch (MSecException mSecException) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_MSEC_EXCEPTION, mSecException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MSEC_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		}
		return sendBankmailPolicyDTO;
	}

	/**
	 * Description Retrieves a list of SendBankmailPolicy for customers which can be selected by logged in user in the
	 * attribute "concerningCustomer" in new messages. List is always empty for employees. List can be empty for Internet
	 * Banking clients, in which case they are not allowed to create/send new messages. Actions CREATE_MESSAGE,
	 * SEND_MESSAGE will be disallowed in this case (see getMailbox, getMailboxes, MailboxActionName).
	 * @param securityContext securityContext
	 * @return sendBankmailPolicyListDTO SendBankmailPolicyListDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public SendBankmailPolicyListDTO getSendBankmailPolicies(SecurityContext securityContext) throws BankmailApplicationException, IOException, SAXException {
		final String LOG_METHOD = "getSendBankmailPolicies(securityContext):SendBankmailPolicyListDTO";
		SendBankmailPolicyListDTO sendBankmailPolicyListDTO = null;
		List<SendBankmailPolicyDTO> dtoList = new ArrayList<SendBankmailPolicyDTO>();		
		SecurityAgent securityAgent = null;
		ContUsershipsResult contUsershipsResult = null;
		try {
			if (null != securityContext) {
				securityAgent = SecurityAgentFactory.getSecurityAgent();
				// retrieve the contUsershipsResult for the current representative
				contUsershipsResult = SecurityAgentUtils.getContUsershipsForUser(securityAgent,BankmailConstants.MSEC_APP_NAME, securityContext
					.getSessionSpecification());
				// for each user in the ContractUsership list retrieve the SendbankmailPolicy
				if (contUsershipsResult.getResultCode() == ContUsershipsResult.SUCCESS) {
					for (ContractUsership users : contUsershipsResult.getContUserships()) {
						SendBankmailPolicyDTO bankmailPolicyDTO = retrieveSendBankmailPolicy(users.getBcOwner(), true);
						// If SendBankmailPolicy exists, add to the policyList
						if (null != bankmailPolicyDTO) {
							dtoList.add(bankmailPolicyDTO);
						}
					}
				}
				// If policy list is populated, set it in sendBankmailPolicyListDTO
				sendBankmailPolicyListDTO = new SendBankmailPolicyListDTO();
				sendBankmailPolicyListDTO.setSendBankmailPolicies(dtoList);
			}
		} catch (MSecException mSecException) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_MSEC_EXCEPTION, mSecException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_MSEC_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		}
		return sendBankmailPolicyListDTO;
	}

	/**
	 * Retrieves SendBankmailPolicy for a customer. Null is returned if no messages can be sent concerning given
	 * customer. This method doesn't contain any security checks. This method uses following tables (stored in Tridion):
	 * FilteredCustomerGroups FilteredBOs Tables are used for phased rollout: if customer's CGC and BO number are
	 * found in these tables then send functionality is (temporary) disabled for the customer. ServiceConceptByCGC
	 * ServiceConceptBySegment Tables are used to configure the Service Concept for incoming messages (from the
	 * representative to the bank). Service Concept is applied to the concerningCustomer of the message to calculate the
	 * SendBankmailPolicy.
	 * @param bcNumber String
	 * @param applyRolloutFilter flag whether to apply filter or not.
	 * @return sendBankmailPolicyDTO SendBankmailPolicyDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public  SendBankmailPolicyDTO retrieveSendBankmailPolicy(String bcNumber, boolean applyRolloutFilter)
			throws BankmailApplicationException, IOException, SAXException {

		final String LOG_METHOD = "retrieveSendBankmailPolicy(String, boolean): SendBankmailPolicyDTO";
		BusinessContactExtendedInfoDTO businessContactExtendedInfoDTO = null;
		SendBankmailPolicyDTO sendBankmailPolicyDTO = null;
		BODTO boDTO = null;
		String customerBO = null;		
		BusinessContactInputDTO retrieveSpecification = new BusinessContactInputDTO();
		retrieveSpecification.setIndicationRetrieveSegmentationInfo(true);
		retrieveSpecification.setIndicationRetrieveAccountCarryingBO(true);
		retrieveSpecification.setIndicationRetrieveInterpayName(true);
		retrieveSpecification.setIndicationRetrieveServiceSegment(true);
		retrieveSpecification.setIndicationRetrieveResponsiblePartyInfo(true);

		try {

			businessContactExtendedInfoDTO = businessContactService.readBusinessContactExtended(KeyType.getBcNumber(),
					bcNumber, retrieveSpecification, null, true);

			// retrieve customer's segmentation info
				SegmentationInfoDTO segmentationInfoDTO = businessContactExtendedInfoDTO.getSegmentationInfo();
			// retrieve the customer CGC from segmentationInfoDTO
			String clientGroupCode = segmentationInfoDTO.getClientGroupCode();
			if (applyRolloutFilter) {

				// retrieved (cached) filteredCustomerGroups from tridion
				List<String> fCGList = (List<String>) bankmailResourceDataUtil.getData(MailResources.FILTERED_CUSTOMER_GROUPS.getCacheKey());
				// If customer clientGroupCode exists in the filteredCustomerGroups
				if (fCGList.contains(clientGroupCode)) {
					boDTO = businessContactExtendedInfoDTO.getAccountCarryingBO();
					// retrieve customer BO
					customerBO = boDTO.getBO();

					// retrieved (cached) filteredBOs from tridion
					List<String> fBOList= (List<String>) bankmailResourceDataUtil.getData(MailResources.FILTERED_BOS.getCacheKey());
					// If customer BO is in the filteredBOs list return null
					if (fBOList.contains(customerBO)) {
						return null;
					}
				}
			}
			// retrieved (cached) serviceConceptByCGC from tridion
			HashMap<String, ServiceConceptDTO> serviceConceptDTOMap = parseAndRetrieveServiceConceptByCGC();

			// find ServiceConcept by customer CGC
			ServiceConceptDTO serviceConceptDTO = serviceConceptDTOMap.get(clientGroupCode);

			if (null == serviceConceptDTO) {
				// retrieved (cached) serviceConceptBySegmen
				//
				// t from tridion
				HashMap<String, ServiceConceptDTO> serviceConceptDTOSegmentMap = parseAndRetreiveServiceConceptBySegment();
				// find ServiceConcept by customer segment
				String segment = businessContactExtendedInfoDTO.getServiceSegment();
				serviceConceptDTO = serviceConceptDTOSegmentMap.get(segment);
			}

			// if serviceConceptDTO still not found return null
			if (null == serviceConceptDTO) {
				LOGGER.warn(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_SERVICE_CONCEPT_NOT_FOUND, bcNumber);
				return null;
			}

			// Create new sendBankmailPolicyDTO instance
			sendBankmailPolicyDTO = new SendBankmailPolicyDTO();

			// get the customer short name
			businessContactExtendedInfoDTO.getInterpayName();

			// add customer info in the policy
			sendBankmailPolicyDTO.setCustomer(businessContactExtendedInfoDTO);

			// add serviceConcept info into the policy
			sendBankmailPolicyDTO.setSlaMessageId(serviceConceptDTO.getSlaMessageId());
			sendBankmailPolicyDTO.setCanSelectSubject(serviceConceptDTO.isCanSelectSubject());

			EmployeeMailboxTemplate destinationMailboxTemplate = serviceConceptDTO.getDestinationMailboxTemplate();
			// set EmployeeMailboxTemplate details as per the destinationMailboxTemplate
			if (null != destinationMailboxTemplate) {
				// if destinationMailboxTemplate is of type GenesysMailboxTemplate
				if (destinationMailboxTemplate instanceof GenesysMailboxTemplate) {
					sendBankmailPolicyDTO
							.setDestination(applyGenesysMailboxTemplate((GenesysMailboxTemplate) destinationMailboxTemplate));
				}
				// if destinationmailboxTemplate is of type BOMailboxTemplate
				else if (destinationMailboxTemplate instanceof BOMailboxTemplate) {
					sendBankmailPolicyDTO.setDestination(applyCSUMailboxTemplate(
							(BOMailboxTemplate) destinationMailboxTemplate, Long.parseLong(bcNumber)));
				}// if destinationmailboxTemplate is of type PreferredBankerMailboxTemplate
				else if (destinationMailboxTemplate instanceof PreferredBankerMailboxTemplate) {
					sendBankmailPolicyDTO.setDestination(applyPreferredBankerMailboxTemplate(
							(PreferredBankerMailboxTemplate) destinationMailboxTemplate, Long.parseLong(bcNumber)));
				}// if destinationmailboxTemplate is of type PrivateBankerMailboxTemplate
				else if (destinationMailboxTemplate instanceof PrivateBankerMailboxTemplate) {
					sendBankmailPolicyDTO.setDestination(applyPrivateBankerMailboxTemplate(
							(PrivateBankerMailboxTemplate) destinationMailboxTemplate, Long.parseLong(bcNumber)));
				} else {
					// no policy:Use visitor pattern to avoid this case
				}
			}
			return sendBankmailPolicyDTO;
		} catch (BusinessContactServiceException businessContactServiceException) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_BUSINESS_CONTACT_SERVICE_EXCEPTION, businessContactServiceException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_BUSINESS_CONTACT_SERVICE_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		}

	}

	/**
	 * parseAndRetrieveServiceConceptByCGC This method parse ServiceConcept in SERVICECONCEPTBYCGC table by CGC
	 * @return HashMap<String, ServiceConceptDTO> hash map of cgc & resp. ServiceConceptDTO
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	private HashMap<String, ServiceConceptDTO> parseAndRetrieveServiceConceptByCGC()
			throws BankmailApplicationException, IOException, SAXException {

		final String LOG_METHOD = "parseAndRetrieveServiceConceptByCGC(Document):HashMap<String, ServiceConceptDTO> ";

		HashMap<String, ServiceConceptDTO> serviceConceptDTOMap = new HashMap<String, ServiceConceptDTO>();
		List<ServiceConceptCGC> serviceConceptList= (List<ServiceConceptCGC>) bankmailResourceDataUtil.getData(MailResources.SERVICE_CONCEPT_BY_CGC.getCacheKey());

		if (serviceConceptList != null && serviceConceptList.size() > 0) {
			LOGGER.debugHardCodedMessage(LOG_METHOD, " ServiceConceptByCGC : LENGTH : {0}", serviceConceptList.size());
			for (int i = 0; i < serviceConceptList.size(); i++) {
				// get the ServiceConcept element
				ServiceConceptCGC serviceConceptElement=serviceConceptList.get(i);
				String cgc=serviceConceptElement.getcGC();

				if (StringUtils.isNotBlank(cgc)) {
					// get the ServiceConcept object
					ServiceConceptDTO serviceConceptDTO = getServiceConcept(serviceConceptElement);
					// add it to list					
						serviceConceptDTOMap.put(cgc, serviceConceptDTO);					
				}
			}
		} else {
			LOGGER.warn(LOG_METHOD, BankmailASCLogConstants.LOG_SERVICE_CONCEPT_LIST_NOT_FOUND);
		}
		return serviceConceptDTOMap;
	}

	/**
	 * getServiceConcept: It takes a xml element, look for values of ServiceConceptDTO
	 * @param serviceConceptElement Element
	 * @return ServiceConceptDTO serviceConceptDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	private ServiceConceptDTO getServiceConcept(ServiceConceptCGC serviceConceptElement) throws BankmailApplicationException, IOException, SAXException {

		ServiceConceptDTO serviceConceptDTO = new ServiceConceptDTO();
		String slaMessageId=serviceConceptElement.getSlaMessageKey();
		serviceConceptDTO.setSlaMessageId(slaMessageId);
		boolean canSelectSubject=getBooleanValueFromJson(serviceConceptElement.getCanSelectSubject());
		serviceConceptDTO.setCanSelectSubject(canSelectSubject);

		String destinationMailboxTemplate =serviceConceptElement.getDestinationMailboxTemplate();

			if (StringUtils.isNotEmpty(destinationMailboxTemplate)) {
				if (BankmailConstants.ASC_RETAIL.equalsIgnoreCase(destinationMailboxTemplate)) {
					GenesysMailboxTemplate genesysMailboxTemplate = null;
					// genesysMailboxTemplate.setDisplayName(displayName)

					List<GenesysMailboxTemplateJson> genesysMailboxTemplateASCJsons= (List<GenesysMailboxTemplateJson>) bankmailResourceDataUtil.getData(MailResources.GENESYS_ASC_MAIL_TEMPLATE.getCacheKey());

					genesysMailboxTemplate = bankmailMailboxTemplateParserUtil
						.parseAndRetreiveGenesysMailboxTemplateAsc(genesysMailboxTemplateASCJsons);

					// Set the retrieved template in the serviceConceptDTO
					serviceConceptDTO.setDestinationMailboxTemplate(genesysMailboxTemplate);
				} else if (BankmailConstants.CNMB_YBB.equalsIgnoreCase(destinationMailboxTemplate)) {
					GenesysMailboxTemplate genesysMailboxTemplate = null;
					// genesysMailboxTemplate.setDisplayName(displayName)

					List<GenesysMailboxTemplateJson> genesysMailboxTemplateList = (List<GenesysMailboxTemplateJson>) bankmailResourceDataUtil.getData(MailResources.GENESYS_YBB_MAIL_TEMPLATE.getCacheKey());
					genesysMailboxTemplate = bankmailMailboxTemplateParserUtil
						.parseAndRetreiveGenesysMailboxTemplateYBB(genesysMailboxTemplateList);

					// Set the retrieved template in the serviceConceptDTO
					serviceConceptDTO.setDestinationMailboxTemplate(genesysMailboxTemplate);
				} else if (BankmailConstants.CNMB_RM.equalsIgnoreCase(destinationMailboxTemplate)) {
					BOMailboxTemplate boMailboxTemplate = null;

					boMailboxTemplate = bankmailMailboxTemplateParserUtil
						.parseAndRetreiveBOMailboxTemplate();

					// Set the retrieved template in the serviceConceptDTO
					serviceConceptDTO.setDestinationMailboxTemplate(boMailboxTemplate);
				} else if (BankmailConstants.PREFERRED_BANKING.equalsIgnoreCase(destinationMailboxTemplate)) {
					serviceConceptDTO.setDestinationMailboxTemplate(new PreferredBankerMailboxTemplate());
				} else if (BankmailConstants.PRIVATE_BANKING.equalsIgnoreCase(destinationMailboxTemplate)) {
					serviceConceptDTO.setDestinationMailboxTemplate(new PrivateBankerMailboxTemplate());
				}
			}

		return serviceConceptDTO;
	}


	//check for serviceconcept segment, remove after this

	private ServiceConceptDTO getServiceConceptFromJson(ServiceConcept serviceConceptElement) throws BankmailApplicationException, IOException, SAXException {

		ServiceConceptDTO serviceConceptDTO = new ServiceConceptDTO();


		String slaMessageId =serviceConceptElement.getSlaMessageKey();
		serviceConceptDTO.setSlaMessageId(slaMessageId);

		boolean canSelectSubject = getBooleanValueFromJson(serviceConceptElement.getCanSelectSubject());
		serviceConceptDTO.setCanSelectSubject(canSelectSubject);

		String destinationMailboxTemplate = serviceConceptElement.getDestinationMailboxTemplate();


		if (StringUtils.isNotEmpty(destinationMailboxTemplate)) {
			if (BankmailConstants.ASC_RETAIL.equalsIgnoreCase(destinationMailboxTemplate)) {
				GenesysMailboxTemplate genesysMailboxTemplate = null;
				// genesysMailboxTemplate.setDisplayName(displayName)

				List<GenesysMailboxTemplateJson> genesysMailboxTemplateASCJsons= (List<GenesysMailboxTemplateJson>) bankmailResourceDataUtil.getData(MailResources.GENESYS_ASC_MAIL_TEMPLATE.getCacheKey());
				genesysMailboxTemplate = bankmailMailboxTemplateParserUtil
						.parseAndRetreiveGenesysMailboxTemplateAsc(genesysMailboxTemplateASCJsons);

				// Set the retrieved template in the serviceConceptDTO
				serviceConceptDTO.setDestinationMailboxTemplate(genesysMailboxTemplate);
			} else if (BankmailConstants.CNMB_YBB.equalsIgnoreCase(destinationMailboxTemplate)) {
				GenesysMailboxTemplate genesysMailboxTemplate = null;
				// genesysMailboxTemplate.setDisplayName(displayName)

				List<GenesysMailboxTemplateJson> genesysMailboxTemplateList = (List<GenesysMailboxTemplateJson>) bankmailResourceDataUtil.getData(MailResources.GENESYS_YBB_MAIL_TEMPLATE.getCacheKey());
				genesysMailboxTemplate = bankmailMailboxTemplateParserUtil
						.parseAndRetreiveGenesysMailboxTemplateYBB(genesysMailboxTemplateList);

				// Set the retrieved template in the serviceConceptDTO
				serviceConceptDTO.setDestinationMailboxTemplate(genesysMailboxTemplate);
			} else if (BankmailConstants.CNMB_RM.equalsIgnoreCase(destinationMailboxTemplate)) {
				BOMailboxTemplate boMailboxTemplate = null;

				boMailboxTemplate = bankmailMailboxTemplateParserUtil
						.parseAndRetreiveBOMailboxTemplate();

				// Set the retrieved template in the serviceConceptDTO
				serviceConceptDTO.setDestinationMailboxTemplate(boMailboxTemplate);
			} else if (BankmailConstants.PREFERRED_BANKING.equalsIgnoreCase(destinationMailboxTemplate)) {
				serviceConceptDTO.setDestinationMailboxTemplate(new PreferredBankerMailboxTemplate());
			} else if (BankmailConstants.PRIVATE_BANKING.equalsIgnoreCase(destinationMailboxTemplate)) {
				serviceConceptDTO.setDestinationMailboxTemplate(new PrivateBankerMailboxTemplate());
			}
		}

		return serviceConceptDTO;
	}

	/**
	 * getTextValue: It takes a xml element and the tag name, look for the tag and get the text content i.e for
	 * <employee><name>John</name></employee> xml snippet if the Element points to employee node and tagName is 'name' It
	 * will return John
	 * @param ele Element
	 * @param tagName String
	 * @return String tag value
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}

	private boolean getBooleanValueFromJson(String tagName) {

		if (StringUtils.isNotBlank(tagName)) {

			if ("Y".equalsIgnoreCase(tagName)) {
				return true;
			} else if ("N".equalsIgnoreCase(tagName)) {
				return false;
			}
		}
		return false;
	}

	/**
	 * parseAndRetreiveServiceConceptBySegment: This method parse ServiceConcept in SERVICECONCEPTBYSEGMENT table by
	 * Segment
	 * @return HashMap<String, ServiceConceptDTO> hash map of Segment & resp ServiceConceptDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	private HashMap<String, ServiceConceptDTO> parseAndRetreiveServiceConceptBySegment()
			throws BankmailApplicationException, IOException, SAXException {
		final String LOG_METHOD = "parseAndRetreiveServiceConceptBySegment(Document):HashMap<String, ServiceConceptDTO>";
		HashMap<String, ServiceConceptDTO> serviceConceptDTOMap = new HashMap<String, ServiceConceptDTO>();

		List<ServiceConcept> serviceConceptList= (List<ServiceConcept>) bankmailResourceDataUtil.getData(MailResources.SERVICE_CONCEPT_BY_SEGMENT.getCacheKey());
		if (serviceConceptList.size() > 0) {
			for (int i = 0; i < serviceConceptList.size(); i++) {
				// get the ServiceConcept element
				ServiceConcept serviceConcept=serviceConceptList.get(i);
				String segment =serviceConcept.getSegment();
				if (StringUtils.isNotBlank(segment)) {
					// get the ServiceConcept object
					ServiceConceptDTO serviceConceptDTO = getServiceConceptFromJson(serviceConcept);
					// add it to list					
						serviceConceptDTOMap.put(segment, serviceConceptDTO);					
				}
			}
		} else {
			LOGGER.warn(LOG_METHOD, BankmailASCLogConstants.LOG_SERVICE_CONCEPT_LIST_NOT_FOUND);
		}
		return serviceConceptDTOMap;
	}

	/**
	 * @param template GenesysMailboxTemplate
	 * @return mailContactCustomDTO MailContactCustomDTO
	 */
	private MailContactCustomDTO applyGenesysMailboxTemplate(GenesysMailboxTemplate template) {
		MailContactCustomDTO mailContactCustomDTO = new MailContactCustomDTO();
		mailContactCustomDTO.setAddress(GenesysMailboxTemplate.MAILBOX_NAME);
		mailContactCustomDTO.setDisplayName(template.getDisplayName());
		return mailContactCustomDTO;
	}

	/**
	 * @param template BOMailboxTemplate
	 * @param bc long
	 * @return mailContactCustomDTO MailContactCustomDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	private MailContactCustomDTO applyCSUMailboxTemplate(BOMailboxTemplate template, long bc)
			throws BankmailApplicationException {
		final String LOG_METHOD = "applyCSUMailboxTemplate(BOMailboxTemplate, long):MailContactCustomDTO";
		MailContactCustomDTO mailContactCustomDTO = new MailContactCustomDTO();
		BusinessContactExtendedInfoDTO businessContactExtendedInfoDTO = null;
		BODTO boDTO = null;
		String customerBO = null;
		BusinessContactInputDTO retrieveSpecification = null;

		try {
			retrieveSpecification = new BusinessContactInputDTO();
			retrieveSpecification.setIndicationRetrieveSegmentationInfo(true);
			retrieveSpecification.setIndicationRetrieveAccountCarryingBO(true);
			retrieveSpecification.setIndicationRetrieveInterpayName(true);
			retrieveSpecification.setIndicationRetrieveServiceSegment(true);
			retrieveSpecification.setIndicationRetrieveResponsiblePartyInfo(true);

			businessContactExtendedInfoDTO = businessContactService.readBusinessContactExtended(KeyType.getBcNumber(), bc
					+ "", retrieveSpecification, null, true);

			// Retrieve customer BO
			boDTO = businessContactExtendedInfoDTO.getAccountCarryingBO();
			customerBO = boDTO.getBO();


			// boNumber to be set when retrieve CSU BO is implemented
			BOStructureLookupInputDTO boStructureLookupInputDTO = new BOStructureLookupInputDTO();
			// set Bonumber
			boStructureLookupInputDTO.setBoNumber(customerBO);
			// set characteristic as BSI130
			boStructureLookupInputDTO.setCharacteristic(BankmailConstants.CSU_BO_CHARACTERISTIC);
			// set subCharecteristic as null
			boStructureLookupInputDTO.setSubCharacteristic(null);
			// call BSI130 lookupBOStructure method
			BOStructureLookupOutputDTO boStructureLookupOutputDTO = boSearchAndRetrievalService
				.lookupBOStructure(boStructureLookupInputDTO);

			// If Found
			if (null != boStructureLookupOutputDTO) {
				mailContactCustomDTO.setAddress(template.getMailboxName(Long.parseLong(boStructureLookupOutputDTO
					.getBoNumber())));
				mailContactCustomDTO.setDisplayName(template.getDisplayName());
			} else {
				// Applying the Retail YBB Mailbox template.
				mailContactCustomDTO = applyGenesysMailboxTemplate(template.getFallbackStrategy());
			}
		} catch (BusinessContactServiceException businessContactServiceException) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_BUSINESS_CONTACT_SERVICE_EXCEPTION, businessContactServiceException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_BUSINESS_CONTACT_SERVICE_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		} catch (BOSearchAndRetrievalServiceException boSearchAndRetrievalServiceException) {
			LOGGER.error(LOG_METHOD, LOG_BO_SEARCH_RETRIEVAL_SERVICE_EXCEPTION, boSearchAndRetrievalServiceException, customerBO, boSearchAndRetrievalServiceException.getMessage());
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_BO_RETRIEVAL_SERVICE_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		}
		return mailContactCustomDTO;
	}

	/**
	 * @param template PreferredBankerMailboxTemplate
	 * @param bc long
	 * @return MailContactCustomDTO MailContactCustomDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public MailContactCustomDTO applyPreferredBankerMailboxTemplate(PreferredBankerMailboxTemplate template, long bc)
			throws BankmailApplicationException {
		final String LOG_METHOD = "applyPreferredBankerMailboxTemplate(PreferredBankerMailboxTemplate, long):MailContactCustomDTO";
		MailContactCustomDTO mailContactCustomDTO = null;
		BusinessContactExtendedInfoDTO businessContactExtendedInfoDTO = null;
		boolean isPartyFound = false;
		ResponsiblePartyDTO typeInvestmentAdvisor = null;
		ResponsiblePartyDTO typePreferredBanker = null;
		BusinessContactInputDTO retrieveSpecification = null;
		try {
			retrieveSpecification = new BusinessContactInputDTO();
			retrieveSpecification.setIndicationRetrieveSegmentationInfo(true);
			retrieveSpecification.setIndicationRetrieveAccountCarryingBO(true);
			retrieveSpecification.setIndicationRetrieveInterpayName(true);
			retrieveSpecification.setIndicationRetrieveServiceSegment(true);
			retrieveSpecification.setIndicationRetrieveResponsiblePartyInfo(true);
			businessContactExtendedInfoDTO = businessContactService.readBusinessContactExtended(KeyType.getBcNumber(), bc
					+ "", retrieveSpecification, null, true);

			// find responsibleParty with responsibleType as Investment Advisor
			ResponsiblePartyDTO[] responsiblePartyDTOs = businessContactExtendedInfoDTO.getResponsiblePartyInfo();
			for (ResponsiblePartyDTO responsiblePartyDTO : responsiblePartyDTOs) {
				if (responsiblePartyDTO.getResponsiblePartyType().equalsIgnoreCase(BankmailConstants.INVESTMENT_ADVISOR)) {
					typeInvestmentAdvisor = responsiblePartyDTO;
					isPartyFound = true;
					break;
				}
			}
			if (isPartyFound && !typeInvestmentAdvisor.isIndicationGroup()) {
				mailContactCustomDTO = new MailContactCustomDTO();
				mailContactCustomDTO.setAddress(template.getMailboxName(typeInvestmentAdvisor.getEmployeeNumber()));
				mailContactCustomDTO.setDisplayName(template.getDisplayNamePrefix().trim() + " "
						+ typeInvestmentAdvisor.getEmployeeName());
			} else {
				// If Investment banker not found then find responsibleParty with responsiblePartyType = Preferred banker.
				for (ResponsiblePartyDTO responsiblePartyDTO : responsiblePartyDTOs) {
					if (responsiblePartyDTO.getResponsiblePartyType().equalsIgnoreCase(BankmailConstants.PREFERRED_BANKER)) {
						typePreferredBanker = responsiblePartyDTO;
						isPartyFound = true;
						break;
					}
				}
				if (isPartyFound && typePreferredBanker!=null &&  !typePreferredBanker.isIndicationGroup()) {
					mailContactCustomDTO = new MailContactCustomDTO();
					mailContactCustomDTO.setAddress(template.getMailboxName(typePreferredBanker.getEmployeeNumber()));
					mailContactCustomDTO.setDisplayName(template.getDisplayNamePrefix().trim() + " "
							+ typePreferredBanker.getEmployeeName());
				} else {
					// Apply fallback strategy
					mailContactCustomDTO = applyGenesysMailboxTemplate(template.getFallbackStrategy());
				}
			}
			LOGGER.debugHardCodedMessage(LOG_METHOD, "mailContactCustomDTO : {0} ", mailContactCustomDTO);
		} catch (BusinessContactServiceException businessContactServiceException) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_BUSINESS_CONTACT_SERVICE_EXCEPTION, businessContactServiceException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_BUSINESS_CONTACT_SERVICE_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		}
		return mailContactCustomDTO;
	}

	/**
	 * @param template PrivateBankerMailboxTemplate
	 * @param bc long
	 * @return mailContactCustomDTO mailContactCustomDTO
	 * @throws BankmailApplicationException BankmailApplicationException
	 */
	public MailContactCustomDTO applyPrivateBankerMailboxTemplate(PrivateBankerMailboxTemplate template, long bc)
			throws BankmailApplicationException {
		final String LOG_METHOD = "applyPrivateBankerMailboxTemplate(PrivateBankerMailboxTemplate, long):MailContactCustomDTO";
		MailContactCustomDTO mailContactCustomDTO = null;
		BusinessContactExtendedInfoDTO businessContactExtendedInfoDTO = null;
		boolean isPartyFound = false;
		ResponsiblePartyDTO typePrivateBanker = null;
		BusinessContactInputDTO retrieveSpecification = null;
		try {
			retrieveSpecification = new BusinessContactInputDTO();
			retrieveSpecification.setIndicationRetrieveSegmentationInfo(true);
			retrieveSpecification.setIndicationRetrieveAccountCarryingBO(true);
			retrieveSpecification.setIndicationRetrieveInterpayName(true);
			retrieveSpecification.setIndicationRetrieveServiceSegment(true);
			retrieveSpecification.setIndicationRetrieveResponsiblePartyInfo(true);
			businessContactExtendedInfoDTO = businessContactService.readBusinessContactExtended(KeyType.getBcNumber(), bc
					+ "", retrieveSpecification, null, true);

			// find responsibleParty with responsibleType as Private Banker
			ResponsiblePartyDTO[] responsiblePartyDTOs = businessContactExtendedInfoDTO.getResponsiblePartyInfo();
			for (ResponsiblePartyDTO responsiblePartyDTO : responsiblePartyDTOs) {
				if (responsiblePartyDTO.getResponsiblePartyType().equalsIgnoreCase(BankmailConstants.PRIVATE_BANKER)) {
					typePrivateBanker = responsiblePartyDTO;
					isPartyFound = true;
					break;
				}
			}
			if (isPartyFound && !typePrivateBanker.isIndicationGroup()) {
				mailContactCustomDTO = new MailContactCustomDTO();
				mailContactCustomDTO.setAddress(template.getMailboxName(typePrivateBanker.getEmployeeNumber()));
				mailContactCustomDTO.setDisplayName(template.getDisplayNamePrefix().trim() + " "
						+ typePrivateBanker.getEmployeeName());
			} else {
				// Apply fallback strategy
				mailContactCustomDTO = applyGenesysMailboxTemplate(template.getFallbackStrategy());
			}
			LOGGER.debugHardCodedMessage(LOG_METHOD, "mailContactCustomDTO : {0} ", mailContactCustomDTO);
		} catch (BusinessContactServiceException businessContactServiceException) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_BUSINESS_CONTACT_SERVICE_EXCEPTION, businessContactServiceException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_BUSINESS_CONTACT_SERVICE_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		}
		return mailContactCustomDTO;
	}

	/*
	 * (non-Javadoc)
	 * @see com.abnamro.nl.health.interfaces.HealthCheckable#isHealthy()
	 */
	public boolean isHealthy() throws HealthCheckException {
		return true;
	}

}
