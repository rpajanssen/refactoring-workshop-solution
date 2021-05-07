package com.abnamro.nl.channels.restservice.utils;

import static com.abnamro.nl.channels.restservice.utils.MailMessageServiceConstants.JSON_STRING_MAIL_MESSAGE;
import static com.abnamro.nl.channels.restservice.utils.MailMessageServiceConstants.JSON_STRING_MAIL_MESSAGES;
import static com.abnamro.nl.channels.restservice.utils.MailMessageServiceConstants.JSON_STRING_MAIL_MESSAGE_LIST;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailContactCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailMessageCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailMessageListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.resources.*;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.rest.actions.JSONContentEncapsulator;

/**
 * MailMessageRestServiceResponseBuilder: 
 * Response Builder for MailMessage RestService
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	04-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
@LogInterceptorBinding
public class MailMessageServiceResponseBuilder {

	private MailMessageServiceResponseBuilder(){

	}
    /**
	* This method is used to build successful json representations
	* of retrieved mailMessage.
	* 
	* @param mailMessageList  : [ MailMessageListDTO ] MailMessageList.
	* @return response        : [ Response ] Json representation of retrieved MailMessage.
	*/
    public static Response buildSuccessResponse ( MailMessageListDTO mailMessageList) {
    	
    	JSONContentEncapsulator<Object> encapsulatorMailMessageList = new JSONContentEncapsulator<>(
				JSON_STRING_MAIL_MESSAGE_LIST);
		JSONContentEncapsulator<List<JSONContentEncapsulator<MailMessage>>> encapsulator = new JSONContentEncapsulator<>(
				JSON_STRING_MAIL_MESSAGES);
		List<JSONContentEncapsulator<MailMessage>> lstMailMessages = new ArrayList<>();
		
		encapsulatorMailMessageList.setObject(encapsulator);
		encapsulator.setObject(lstMailMessages);
		
		if (null != mailMessageList && null != mailMessageList.getMailMessages() && !mailMessageList.getMailMessages().isEmpty() ) {
			
			for(MailMessageCustomDTO mailMessageCustomDTO : mailMessageList.getMailMessages()){
				
				MailMessage mailMessage =  convertToMailMessage(mailMessageCustomDTO);
				
				if(mailMessage != null){
					JSONContentEncapsulator<MailMessage> encapsulatorMailMessage = new JSONContentEncapsulator<>(JSON_STRING_MAIL_MESSAGE);
					encapsulatorMailMessage.setObject(mailMessage);
					lstMailMessages.add(encapsulatorMailMessage);
				}
			}
		}
		
		return Response.status(Status.OK).entity(encapsulatorMailMessageList).build();

    }
    
    /**
	* This method converts MailMessageCustomDTO to MailMessage
	*  
	* @param mailMessageCustomDTO   : [ MailMessageCustomDTO[] ] mailMessageCustomDTO.
	* @return mailMessage           : [ MailMessage[] ] mailMessage.
	*/
    private static MailMessage convertToMailMessage( MailMessageCustomDTO mailMessageCustomDTO ) {
    	
    	MailMessage mailMessage = null;
    	
    	if(mailMessageCustomDTO!=null){
    		mailMessage = new MailMessage();
			mailMessage.setId(mailMessageCustomDTO.getId());
			
			MailContact mailFromContact = convertToMailContact(mailMessageCustomDTO.getFrom());
			mailMessage.setFrom(mailFromContact);
			
			List<MailContact> mailToContacts = convertToMailContacts(mailMessageCustomDTO.getTo());
			mailMessage.setTo(mailToContacts);
			
			mailMessage.setOriginationDate(mailMessageCustomDTO
					.getOriginationDate() != null ? mailMessageCustomDTO
					.getOriginationDate().getTime() : null);
			
			mailMessage.setSubject(mailMessageCustomDTO.getSubject());
			mailMessage.setMessageType(mailMessageCustomDTO.getMessageType());
    	}
    	
    	return mailMessage;
	}
    
    /**
	* This method converts MailMessageCustomDTO to MailMessage for MailMessageHistory
	*  
	* @param mailMessageCustomDTO   : [ MailMessageCustomDTO ] mailMessageCustomDTO.
	* @return mailMessage           : [ MailMessage ] mailMessage.
	*/
    private static MailMessage convertToMailMessageHistory ( MailMessageCustomDTO mailMessageCustomDTO ) {
    	
    	MailMessage mailMessage = null;
    	
    	if(mailMessageCustomDTO!=null){
    		mailMessage = new MailMessage();
			mailMessage.setId(mailMessageCustomDTO.getId());
			
			MailContact mailFromContact = convertToMailContact(mailMessageCustomDTO.getFrom());
			mailMessage.setFrom(mailFromContact);
			
			List<MailContact> mailToContacts = convertToMailContacts(mailMessageCustomDTO.getTo());
			mailMessage.setTo(mailToContacts);
			
			if ( mailMessageCustomDTO.getInReplyTo() != null) {
				MailMessage inReplyTo = new MailMessage();
				inReplyTo.setId(mailMessageCustomDTO.getInReplyTo().getId());
				mailMessage.setInReplyTo(inReplyTo);
			}
			
			mailMessage.setMessageType(mailMessageCustomDTO.getMessageType());
			mailMessage.setOriginationDate(mailMessageCustomDTO
					.getOriginationDate() != null ? mailMessageCustomDTO
					.getOriginationDate().getTime() : null);
			mailMessage
					.setExpiryDate(mailMessageCustomDTO.getExpiryDate() != null ? mailMessageCustomDTO
							.getExpiryDate().getTime()
							: null);
			mailMessage.setSubject(mailMessageCustomDTO.getSubject());
			mailMessage.setContent(mailMessageCustomDTO.getContent());
			
			if (null != mailMessageCustomDTO.getConcerningCustomerBCNumber()
					&& !("".equals(mailMessageCustomDTO.getConcerningCustomerBCNumber()))) {
				BusinessContact concerningCustomer = new BusinessContact();
				concerningCustomer.setBcNumber(new Long(
						mailMessageCustomDTO
							.getConcerningCustomerBCNumber()));
				mailMessage.setConcerningCustomer(concerningCustomer);
			} else {
				mailMessage.setConcerningCustomer(null);
			}
			
		}
    	
    	return mailMessage;
	}

	/**
	* This method converts List<MailContactCustomDTO> to List<MailContact>
	*  
	* @param mailContactsCustomDTO  : [ List<MailContactCustomDTO> ] list of MailContactCustomDTO.
	* @return mailContacts          : [ List<MailContact> ] list of MailContact.
	*/
    private static List<MailContact> convertToMailContacts(List<MailContactCustomDTO> mailContactsCustomDTO) {
    	List<MailContact> mailContacts = new ArrayList<>();
    	
    	for(MailContactCustomDTO mailContactCustomDTO: mailContactsCustomDTO) {
    		MailContact mailContact= convertToMailContact(mailContactCustomDTO);
    		mailContacts.add(mailContact);
    	}
    	return mailContacts;
	}

    /**
	* This method converts MailContactCustomDTO to MailContact
	*  
	* @param mailContactCustomDTO  : [ MailContactCustomDTO ] MailContactCustomDTO.
	* @return mailContact          : [ MailContact ] MailContact.
	*/
	private static MailContact convertToMailContact(MailContactCustomDTO mailContactCustomDTO) {
    	MailContact mailContact = null;
    	
    	if(mailContactCustomDTO != null) {
    		mailContact = new MailContact();
    		mailContact.setAddress(mailContactCustomDTO.getAddress());
			mailContact.setDisplayName(mailContactCustomDTO.getDisplayName());
    	}
    	return mailContact;
	}

	/**
	* This method is used to build successful json representations
	* of retrieved mailMessage.
	* 
	* @param mailMessageCustomDTO  : [ MailMessageCustomDTO ] MailMessage.
	* @return response    : [ Response ] Json representation of retrieved MailMessage.
	*/
    public static Response buildSuccessResponse ( MailMessageCustomDTO mailMessageCustomDTO) {
    	
    	JSONContentEncapsulator<Object> encapsulatorMailMessage = new JSONContentEncapsulator<>(JSON_STRING_MAIL_MESSAGE);
    	MailMessage mailMessage = convertToMailMessageHistory(mailMessageCustomDTO);
    	encapsulatorMailMessage.setObject(mailMessage);
    	
		return Response.status(Status.OK).entity(encapsulatorMailMessage).build();

    }
}
