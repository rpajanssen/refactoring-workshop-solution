package com.abnamro.nl.channels.geninfo.bankmail.asc.implementation;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.implementation.BankmailABPCMessageKeys;
import com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.MessageArchiveASC;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.UserClass;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.ContactDTO;
import com.abnamro.nl.channels.geninfo.bankmailgateways.aci.interfaces.MessageDTO;
import com.abnamro.nl.generic.service.genericarchiving.interfaces.DocumentKeyDTO;
import com.abnamro.nl.generic.service.genericarchiving.interfaces.GenericArchivingInputDTO;
import com.abnamro.nl.generic.service.genericarchiving.interfaces.GenericArchivingService;
import com.abnamro.nl.generic.service.genericarchiving.interfaces.GenericArchivingServiceException;
import com.abnamro.nl.health.exceptions.HealthCheckException;
import com.abnamro.nl.health.interfaces.HealthCheckable;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;

import javax.ejb.EJB;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * MessageArchiveASCImpl
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
public class MessageArchiveASCImpl implements MessageArchiveASC, HealthCheckable{
	/**
	 * Instantiate the Logger
	 */
	private static final LogHelper LOGGER = new LogHelper(MessageArchiveASCImpl.class);

	private static final String EQUAL_DOUBLE_QUOTE_STRING = "=\"";
	private static final String DOUBLE_QUOTE_STRING = "\"";
	private static final int DOC_ID_MAX_LENGTH = 8;
	private static final String DOCTYPE = "INB10";
	private static final int DOCUMENT_LABEL = 1000;
	private static final String DOCUMENT_SOURCE = "INB#";
	private static final String EIGHT_ZEROS = "00000000";

	@EJB
	private GenericArchivingService archivingService;

	/**
	 * Archives a message.

	 * @param securityContext logged in user information
	 * @param messageDTO MailMessageDTO which holds message information
	 * @throws BankmailApplicationException  bankmailApplicationException
	 */
	public void archiveMessage(SecurityContext securityContext, MessageDTO messageDTO) throws BankmailApplicationException {
		final String LOG_METHOD = "archiveMessage(MailMessageCustomDTO)";
		try {
			GenericArchivingInputDTO genericArchivingInputDTO=new GenericArchivingInputDTO();
			String messageId = messageDTO.getId();
			String docId = "";

			if(null != messageId && messageId.length() > DOC_ID_MAX_LENGTH){
				docId = messageId.substring((messageId.length()-8));
			}else{
				if(messageId!= null) {
				docId = (EIGHT_ZEROS + messageId).substring(messageId.length());
				}
			}

			DocumentKeyDTO masterDocumentKey = new DocumentKeyDTO();
			masterDocumentKey.setBusinessContactNumber(Long.parseLong(messageDTO.getConcerningCustomerBCNumber()));
			masterDocumentKey.setDocId(docId);
			masterDocumentKey.setDocType(DOCTYPE);
			masterDocumentKey.setLabel(DOCUMENT_LABEL);
			masterDocumentKey.setSource(DOCUMENT_SOURCE);
			genericArchivingInputDTO.setMasterDocumentKey(masterDocumentKey);
			int  bcCardOwner =
					(int) securityContext.getSessionAttributes().get("bcnr");
			if (null!= securityContext.getUserClass() && securityContext.getUserClass() == UserClass.EMPLOYEE) {
				bcCardOwner = Integer.parseInt(messageDTO.getConcerningCustomerBCNumber());
			}

			String archivingXML = deriveArchivingXML(String.valueOf(bcCardOwner), messageDTO);
			
            setBinaryData(genericArchivingInputDTO,archivingXML);
			
			LOGGER.debugHardCodedMessage(LOG_METHOD, "dataXML sent for archiving : " + archivingXML);
			LOGGER.debugHardCodedMessage(LOG_METHOD, "Archive message sending to service : {0}", genericArchivingInputDTO);


			archivingService.storeDocuments(new GenericArchivingInputDTO[]{genericArchivingInputDTO});

		} catch (GenericArchivingServiceException e) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_ASC_UNEXPECTED_EXCEPTION, e);
			Messages msgs = e.getMessages();
			throw new BankmailApplicationException(msgs);
		}
	}
	
	/**
	 * set Binary Data.

	 * @param genericArchivingInputDTO input dto
	 * @param archivingXML input xml 
	 * @throws BankmailApplicationException  bankmailApplicationException
	 */
	private void setBinaryData(GenericArchivingInputDTO genericArchivingInputDTO, String archivingXML) throws BankmailApplicationException {
		final String LOG_METHOD = "setBinaryData(GenericArchivingInputDTO,String)";
		try {
			genericArchivingInputDTO.setBinaryData(archivingXML.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(LOG_METHOD, BankmailASCLogConstants.LOG_UNSUPPORTED_ENCODING_EXCEPTION, e);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailABPCMessageKeys.ERROR_UNSUPPORTED_ENCODING_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		}
	}

	/**
	 * @param bcCardOwner logged in user information
	 * @param messageDTO MessageDTO which holds message information
	 * @return returns the XML string of MessageDTO
	 */
	private String deriveArchivingXML(String bcCardOwner, MessageDTO messageDTO) {
		
		StringBuffer archivingXML = new StringBuffer();
		
		archivingXML.append(ArchivingConstants.STANDARD_XML_PI);
		archivingXML.append(ArchivingConstants.BODY_OPEN_TAG);
		archivingXML.append(ArchivingConstants.ARCH_DETL_OPEN_TAG);
		archivingXML.append(ArchivingConstants.APP_DATA_OPEN_TAG);
		archivingXML.append(ArchivingConstants.BC_CARD_OWNER_OPEN_TAG);
		archivingXML.append(bcCardOwner);
		archivingXML.append(ArchivingConstants.BC_CARD_OWNER_CLOSE_TAG);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyyMMdd HH:mm:ss");
		String todaysDate = dateFormat.format(calendar.getTime());
		archivingXML.append(ArchivingConstants.ARCHIVING_TIMESTAMP_OPEN_TAG);
		archivingXML.append(todaysDate);
		archivingXML.append(ArchivingConstants.ARCHIVING_TIMESTAMP_CLOSE_TAG);
		archivingXML.append(createBankmailMessageXML(messageDTO));
		archivingXML.append(ArchivingConstants.FROM_OPEN_TAG);
		archivingXML.append(createMailContactFromXML(messageDTO));
		archivingXML.append(ArchivingConstants.FROM_CLOSE_TAG);
		archivingXML.append(ArchivingConstants.TO_OPEN_TAG);
		ContactDTO[] listOfRecipients = messageDTO.getTo();
		for (ContactDTO mailContactDTO : listOfRecipients) {
			archivingXML.append(createMailContactToXML(mailContactDTO));
		}
		archivingXML.append(ArchivingConstants.TO_CLOSE_TAG);
		archivingXML.append(ArchivingConstants.SUBJECT_OPEN_TAG);
		archivingXML.append(ArchivingConstants.CDATA_OPEN_TAG);
		archivingXML.append(messageDTO.getSubject());
		archivingXML.append(ArchivingConstants.CDATA_CLOSE_TAG);
		archivingXML.append(ArchivingConstants.SUBJECT_CLOSE_TAG);
		archivingXML.append(ArchivingConstants.CONTENT_OPEN_TAG);
		archivingXML.append(ArchivingConstants.CDATA_OPEN_TAG);
		archivingXML.append(messageDTO.getContent());
		archivingXML.append(ArchivingConstants.CDATA_CLOSE_TAG);
		archivingXML.append(ArchivingConstants.CONTENT_CLOSE_TAG);
		archivingXML.append(ArchivingConstants.BANKMAIL_MSG_CLOSE_TAG);
		archivingXML.append(ArchivingConstants.APP_DATA_CLOSE_TAG);
		archivingXML.append(ArchivingConstants.ARCH_DETL_CLOSE_TAG);
		archivingXML.append(ArchivingConstants.BODY_CLOSE_TAG);
		return archivingXML.toString();
	}

	/**
	 * @param messageDTO MessageDTO which holds message information
	 * @return returns the bankmail message string from MessageDTO
	 */
	private String createBankmailMessageXML(MessageDTO messageDTO){
		StringBuffer archivingXML = new StringBuffer();
		archivingXML.append(ArchivingConstants.BANKMAIL_MSG_OPEN_TAG);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		archivingXML.append(ArchivingConstants.ID + EQUAL_DOUBLE_QUOTE_STRING + messageDTO.getId() + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		archivingXML.append(ArchivingConstants.SENDER + EQUAL_DOUBLE_QUOTE_STRING + messageDTO.getSender() + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		archivingXML.append(ArchivingConstants.MESSAGE_TYPE + EQUAL_DOUBLE_QUOTE_STRING + messageDTO.getMessageType() + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		archivingXML.append(ArchivingConstants.IS_REPLY_ALLWD + EQUAL_DOUBLE_QUOTE_STRING + messageDTO.isReplyAllowed() + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		archivingXML.append(ArchivingConstants.CONV_ID + EQUAL_DOUBLE_QUOTE_STRING + messageDTO.getConversationId() + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		if (messageDTO.getInReplyTo() != null) {
			archivingXML.append(ArchivingConstants.IN_REPLY_TO + EQUAL_DOUBLE_QUOTE_STRING + messageDTO.getInReplyTo().getId() + DOUBLE_QUOTE_STRING);
			archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ssZ");
		String originationDate = dateFormat.format(messageDTO.getOriginationTimestamp());
		originationDate = originationDate.substring(0, originationDate.length() - 2) + ":"
				+ originationDate.substring(originationDate.length() - 2);
		archivingXML.append(ArchivingConstants.ORGINATION_DATE + EQUAL_DOUBLE_QUOTE_STRING + originationDate + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		if (messageDTO.getExpiryDate() != null) {
			dateFormat.applyPattern("yyyy-MM-dd");
			String expiryDate = dateFormat.format(messageDTO.getExpiryDate());
			archivingXML.append(ArchivingConstants.EXPIRY_DATE + EQUAL_DOUBLE_QUOTE_STRING + expiryDate + DOUBLE_QUOTE_STRING);
			archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		}
		archivingXML.append(ArchivingConstants.CONCRN_CUST_BCNUM + EQUAL_DOUBLE_QUOTE_STRING + messageDTO.getConcerningCustomerBCNumber()
				+ DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.CLOSE_TAG);
		return archivingXML.toString();
	}

	/**
	 * @param messageDTO MessageDTO which holds message information
	 * @return returns the Contact From XML string from MessageDTO
	 */
	private String createMailContactFromXML(MessageDTO messageDTO) {
		StringBuffer archivingXML = new StringBuffer();
		archivingXML.append(ArchivingConstants.MAIL_CONTACT_OPEN_TAG);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		archivingXML.append(ArchivingConstants.ADDRESS + EQUAL_DOUBLE_QUOTE_STRING + messageDTO.getFrom().getBankmailAddress() + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		archivingXML.append(ArchivingConstants.DISPLAY_NAME + EQUAL_DOUBLE_QUOTE_STRING + messageDTO.getFrom().getName() + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.CLOSE_TAG_WITH_SLASH);
		return archivingXML.toString();
	}

	/**
	 * @param mailContactDTO MessageDTO which holds message information
	 * @return returns the MAil Contact XML string from MessageDTO
	 */
	private String createMailContactToXML(ContactDTO mailContactDTO) {
		StringBuffer archivingXML = new StringBuffer();
		archivingXML.append(ArchivingConstants.MAIL_CONTACT_OPEN_TAG);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		archivingXML.append(ArchivingConstants.ADDRESS + EQUAL_DOUBLE_QUOTE_STRING + mailContactDTO.getBankmailAddress() + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.EMPTY_SPACE);
		archivingXML.append(ArchivingConstants.DISPLAY_NAME + EQUAL_DOUBLE_QUOTE_STRING + mailContactDTO.getName() + DOUBLE_QUOTE_STRING);
		archivingXML.append(ArchivingConstants.CLOSE_TAG_WITH_SLASH);
		return archivingXML.toString();
	}

	/* (non-Javadoc)
	 * @see com.abnamro.nl.health.interfaces.HealthCheckable#isHealthy()
	 */
	/**
	 * HealthCheckable#isHealthy()
	 * @return boolean is healthy 
	 * @throws HealthCheckException  HealthCheckException
	 */

	//check
	public boolean isHealthy() throws HealthCheckException {
		return  Boolean.TRUE;
	}
}
