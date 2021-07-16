package com.abnamro.nl.channels.restservice.util;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.msec.integrationservices.ContUsershipsResult;
import com.abnamro.msec.integrationservices.ContractUsership;
import com.abnamro.msec.integrationservices.MSecException;
import com.abnamro.msec.integrationservices.SecurityAgent;
import com.abnamro.msec.integrationservices.services.SecurityAgentFactory;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.resources.*;
import com.abnamro.nl.channels.restservice.MailboxConstants;
import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.messages.Message;
import com.abnamro.nl.messages.MessageType;
import com.abnamro.nl.messages.Messages;
import com.abnamro.nl.rest.actions.JSONContentEncapsulator;
import com.abnamro.nl.security.util.SecurityAgentUtils;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
/**
 * MailboxUtil: It is a general util class for Mailbox Service.
 * 
 * <PRE>
 * 
 * Developer          Date       Change Reason	  		Change
 * ------------------ ---------- ----------------- ---------------------------------------------- * 
 * TCS			  	10-10-2012	Initial version 	Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
@LogInterceptorBinding
public class MailboxUtil {
	private static final LogHelper LOGGER = new LogHelper(MailboxUtil.class);

	/**
	 * getConcerningBCs:
	 * @param  securityContext SecurityContext
	 * @return ArrayList<String> get all Concerning BCs associated to securitycontext.
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public List<String> getConcerningBCs(SecurityContext securityContext) throws BankmailApplicationException {

		final String LOG_METHOD = "getConcerningBCs(securitycontext):ArrayList<String>";

		ArrayList<String> concerningBCs = new ArrayList<>();

		SecurityAgent securityAgent = null;

		ContUsershipsResult contUsershipsResult = null;

		try {

			securityAgent = SecurityAgentFactory.getSecurityAgent();
			// retrieve the contUsershipsResult for the current
			// representative
			if (null != securityAgent) {
				contUsershipsResult = SecurityAgentUtils.getContUsershipsForUser(securityAgent,
					MailboxConstants.MSEC_APP_NAME, securityContext.getSessionSpecification());

				// for each user in the ContractUsership list retrieve the
				if (null != contUsershipsResult && null != contUsershipsResult.getContUserships()) {

					for (ContractUsership users : contUsershipsResult.getContUserships()) {
						concerningBCs.add(users.getBcOwner());

					}
				}

			}
			return concerningBCs;
		} catch (MSecException mSecException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ASC_MSEC_EXCEPTION, mSecException);
			Messages msgs = new Messages();
			msgs.addMessage(new Message(BankmailRestServiceMessageKeys.ERROR_MSEC_EXCEPTION), MessageType.getError());
			throw new BankmailApplicationException(msgs);
		}
	}

	/**
	 * This method copies data from MailboxMessageCustomDTO to MailboxMessage
	 * @param mailboxMessageCustomDTO MailboxMessageCustomDTO
	 * @return MailboxMessage
	 * @throws BankmailApplicationException bankmailApplicationException
	 */
	public MailboxMessage convertMessage(MailboxMessageCustomDTO mailboxMessageCustomDTO)
			throws BankmailApplicationException {
		final String LOG_METHOD = "convertMessage(MailboxMessageCustomDTO):MailboxMessage";
		MailboxMessage mailboxMessage = null;
		try {
			if (null != mailboxMessageCustomDTO && StringUtils.isNotBlank(mailboxMessageCustomDTO.getId())) {
				mailboxMessage = new MailboxMessage();

				mailboxMessage.setId(mailboxMessageCustomDTO.getId());
				// from
				MailContact from = new MailContact();
				from.setDisplayName(mailboxMessageCustomDTO.getFrom().getDisplayName());
				from.setAddress(mailboxMessageCustomDTO.getFrom().getAddress());
				mailboxMessage.setFrom(from);

				// to
				List<MailContactCustomDTO> orgTo = mailboxMessageCustomDTO.getTo();
				List<MailContact> to = new ArrayList<>();
				if (null != orgTo) {
					for (MailContactCustomDTO contactCustomDTO : orgTo) {
						MailContact contact = new MailContact();
						contact.setDisplayName(contactCustomDTO.getDisplayName());
						contact.setAddress(contactCustomDTO.getAddress());
						to.add(contact);
					}
				}
				mailboxMessage.setTo(to);

				// inReplyTo
				if (null != mailboxMessageCustomDTO.getInReplyTo()) {

					MailMessage inReplyTo = new MailMessage();
					// set id
					inReplyTo.setId(mailboxMessageCustomDTO.getInReplyTo().getId());

					mailboxMessage.setInReplyTo(inReplyTo);
				}

				// IS seen
				mailboxMessage.setIsSeen(mailboxMessageCustomDTO.getIsSeen());

				// IS_ANSWERED
				mailboxMessage.setIsAnswered(mailboxMessageCustomDTO.getIsAnswered());

				// message type
				mailboxMessage.setMessageType(mailboxMessageCustomDTO.getMessageType());

				// origination date
				mailboxMessage.setOriginationDate(mailboxMessageCustomDTO.getOriginationDate().getTime());

				//DeepLink
				MailDeepLink deepLink = new MailDeepLink();
				if(mailboxMessageCustomDTO.getDeepLink()!=null) {
					deepLink.setBcNumber(mailboxMessageCustomDTO.getDeepLink().getBcNumber());
					deepLink.setContractNumber(mailboxMessageCustomDTO.getDeepLink().getContractNumber());
					deepLink.setDocumentId(mailboxMessageCustomDTO.getDeepLink().getDocumentId());
					deepLink.setCreationDate(mailboxMessageCustomDTO.getDeepLink().getCreationDate());
					deepLink.setPostboxDocumentType(mailboxMessageCustomDTO.getDeepLink().getPostboxDocumentType());
					mailboxMessage.setDeepLink(deepLink);
				}

				// received date
				if (null != mailboxMessageCustomDTO.getReceivedDate()) {
					mailboxMessage.setReceivedDate(mailboxMessageCustomDTO.getReceivedDate().getTime());
				}

				mailboxMessage.setSubject(mailboxMessageCustomDTO.getSubject());

				mailboxMessage.setContent(mailboxMessageCustomDTO.getContent());

				// Concerning BC
				if (null != mailboxMessageCustomDTO.getConcerningCustomerBCNumber()
						&& !("".equals(mailboxMessageCustomDTO.getConcerningCustomerBCNumber()))) {
					BusinessContact concerningCustomer = new BusinessContact();
					concerningCustomer.setBcNumber(new Long(mailboxMessageCustomDTO.getConcerningCustomerBCNumber()));
					mailboxMessage.setConcerningCustomer(concerningCustomer);
				} else {
					mailboxMessage.setConcerningCustomer(null);
				}

				if (MailMessageType.ALERT.equals(mailboxMessage.getMessageType())) {
					mailboxMessage.setMessageSubType(mailboxMessageCustomDTO.getDocumentType());
					MailAction[] mailActionList = null;
					if (null != mailboxMessageCustomDTO.getMailActionList()
							&& mailboxMessageCustomDTO.getMailActionList().length > 0) {
						int size = mailboxMessageCustomDTO.getMailActionList().length;
						mailActionList = new MailAction[size];
						MailActionCustomDTO[] mailActionCustomDTOList = mailboxMessageCustomDTO.getMailActionList();
						for (int i = 0; i < size; i++) {
							MailAction mailActionDTO = new MailAction();
							MailActionCustomDTO mailActionCustomDTO = mailActionCustomDTOList[i];
							if(null!=mailActionCustomDTO){
							if (StringUtils.isNotEmpty(mailActionCustomDTO.getName())) {
								mailActionDTO.setName(mailActionCustomDTO.getName());
							}
							if (null != mailActionCustomDTO.getParams() && 0 != mailActionCustomDTO.getParams().length) {
								mailActionDTO.setParams(mailActionCustomDTO.getParams());
							}
							mailActionList[i] = mailActionDTO;
						}}
					}
					mailboxMessage.setMailActions(mailActionList);
				}
			}
			return mailboxMessage;
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * This method copies data from MailboxMessageListDTO to MailboxMessageList
	 * @param mailboxMessageListDTO MailboxMessageListDTO
	 * @return MailboxMessageList
	 * @throws BankmailApplicationException
	 */
	public Object convertMessagesList(MailboxMessageListDTO mailboxMessageListDTO)  {

		final String LOG_METHOD = "convertMessagesList(MailboxMessageListDTO):Object";
		List<JSONContentEncapsulator<MailboxMessage>> listMessages = null;
		try {
			listMessages = new ArrayList<>();

			if (mailboxMessageListDTO.getMailMessages() != null && !mailboxMessageListDTO.getMailMessages().isEmpty()) {

				List<MailboxMessageCustomDTO> mailboxMessageCustomDTOs = mailboxMessageListDTO.getMailMessages();
				for (MailboxMessageCustomDTO mailMessagesCustomDTO : mailboxMessageCustomDTOs) {

					MailboxMessage mailboxMessage = new MailboxMessage();

					mailboxMessage.setId(mailMessagesCustomDTO.getId());

					// from
					MailContact from = new MailContact();
					from.setDisplayName(mailMessagesCustomDTO.getFrom().getDisplayName());
					from.setAddress(mailMessagesCustomDTO.getFrom().getAddress());
					mailboxMessage.setFrom(from);

					// to
					List<MailContact> toDestination = new ArrayList<>();
					MailContact to = new MailContact();
					if (null != mailMessagesCustomDTO.getTo()) {
						to.setDisplayName(mailMessagesCustomDTO.getTo().get(0).getDisplayName());

						to.setAddress(mailMessagesCustomDTO.getTo().get(0).getAddress());
					}
					toDestination.add(to);
					mailboxMessage.setTo(toDestination);

					mailboxMessage.setOriginationDate(mailMessagesCustomDTO.getOriginationDate().getTime());

					if (null != mailMessagesCustomDTO.getReceivedDate()) {
						mailboxMessage.setReceivedDate(mailMessagesCustomDTO.getReceivedDate().getTime());
					}

					mailboxMessage.setMessageType(mailMessagesCustomDTO.getMessageType());

					mailboxMessage.setIsSeen(mailMessagesCustomDTO.getIsSeen());

					mailboxMessage.setIsAnswered(mailMessagesCustomDTO.getIsAnswered());

					mailboxMessage.setSubject(mailMessagesCustomDTO.getSubject());

					mailboxMessage.setContent(mailMessagesCustomDTO.getContent());

					// Concerning BC
					if (null != mailMessagesCustomDTO.getConcerningCustomerBCNumber()
							&& !("".equals(mailMessagesCustomDTO.getConcerningCustomerBCNumber()))) {
						BusinessContact concerningCustomer = new BusinessContact();
						concerningCustomer.setBcNumber(new Long(mailMessagesCustomDTO.getConcerningCustomerBCNumber()));
						mailboxMessage.setConcerningCustomer(concerningCustomer);
					} else {
						mailboxMessage.setConcerningCustomer(null);
					}

					if (MailMessageType.ALERT.equals(mailboxMessage.getMessageType())) {
						mailboxMessage.setMessageSubType(mailMessagesCustomDTO.getDocumentType());
					}

					JSONContentEncapsulator<MailboxMessage> encapsulatorMessage = new JSONContentEncapsulator<>(
						"message");
					encapsulatorMessage.setObject(mailboxMessage);
					listMessages.add(encapsulatorMessage);

				}
			}
			return listMessages;
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

}
