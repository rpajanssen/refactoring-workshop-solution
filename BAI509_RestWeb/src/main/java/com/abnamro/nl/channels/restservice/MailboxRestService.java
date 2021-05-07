package com.abnamro.nl.channels.restservice;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.genj.securitycontext.rest.interceptors.requestinterceptors.SecurityContextEnricherBinding;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.interfaces.BankmailABPC;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.*;
import com.abnamro.nl.channels.geninfo.bankmail.resources.*;
import com.abnamro.nl.channels.rest.interceptors.requestinterceptors.RequestValidatorBinding;
import com.abnamro.nl.channels.rest.interceptors.responseinterceptors.SuppressCacheBinding;
import com.abnamro.nl.channels.rest.interceptors.responseinterceptors.accesslog.AccessLogInterceptorBinding;
import com.abnamro.nl.channels.restservice.util.MailboxUtil;
import com.abnamro.nl.channels.restservice.util.ValidatorUtil;
import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.enumeration.MailboxFolderName;
import com.abnamro.nl.enumeration.MessagesSortOrder;
import com.abnamro.nl.enumeration.util.EnumException;
import com.abnamro.nl.exceptions.ConstraintsViolatedException;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.rest.actions.JSONContentEncapsulator;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * MailboxRestService: Rest Service for Bankmail Plus Mailbox
 * 
 * <PRE>
 * 
 * Developer          Date       Change Reason	  		Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  		Release 1.0
 * TCS			  	09-09-2012	Changes for employee 	Release 1.0
 * </PRE>
 * @author
 * @see
 */
@SuppressCacheBinding
@AccessLogInterceptorBinding
@LogInterceptorBinding
@SecurityContextEnricherBinding
@RequestValidatorBinding
@Path("/")
public class MailboxRestService {


	private static final LogHelper LOGGER = new LogHelper(MailboxRestService.class);
	/**
	 * Validator Util class
	 */
	private static ValidatorUtil validatorUtil = new ValidatorUtil();

	private static final String SecurityContext = "securityContext:";
	@Inject
	private BankmailABPC bankmailABPC;


	/**
	 * Retrieves list of Mailboxes which can be accessed by logged in user. Current implementation returns list
	 * consisting of 1 element: mailbox of current user. If includeActions parameter is provided, actions allowed on each
	 * mailbox are returned (see MailboxActionName)..
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param securityContext securityContext
	 * @param includeActionsString provided actions allowed on each mailbox are returned.
	 * @return Response with status of operation & MailboxList object
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailboxes(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@Context SecurityContext securityContext, @QueryParam("includeActions") String includeActionsString) {

		// Method signature to use in the log statements
		final String LOG_METHOD = "getMailboxes(String):MailboxList";


		try{

			LOGGER.debugHardCodedMessage(LOG_METHOD, SecurityContext + securityContext.toString());
			IncludeActions includeActions = validatorUtil.validateIncludeAction(includeActionsString);

			MailboxListDTO mailboxListDTO = bankmailABPC.getMailboxes(securityContext, includeActions);
			LOGGER.debugHardCodedMessage(LOG_METHOD, "Rest mailboxListDTO {0}", mailboxListDTO.toString());
			JSONContentEncapsulator<Object> encapsulatorMailboxList = new JSONContentEncapsulator<Object>("mailboxList");
			JSONContentEncapsulator<List<JSONContentEncapsulator<Mailbox>>> encapsulator = new JSONContentEncapsulator<List<JSONContentEncapsulator<Mailbox>>>(
				"mailboxes");

			if (null == mailboxListDTO || null == mailboxListDTO.getMailboxes()) {

				return Response.status(Status.NOT_FOUND).entity(encapsulatorMailboxList).build();
			} else {
				List<MailboxCustomDTO> mailboxCustomDTOs = mailboxListDTO.getMailboxes();

				List<JSONContentEncapsulator<Mailbox>> listMailboxes = new ArrayList<JSONContentEncapsulator<Mailbox>>();

				for (MailboxCustomDTO mailboxCustomDTO : mailboxCustomDTOs) {

					Mailbox mailbox = new Mailbox();

					if (null != mailboxCustomDTO) {

						JSONContentEncapsulator<Mailbox> encapsulatorMailbox = new JSONContentEncapsulator<Mailbox>("mailbox");

						mailbox.setName(mailboxCustomDTO.getName());
						mailbox.setDisplayName(mailboxCustomDTO.getDisplayName());
						mailbox.setIsSubjectSelectable(mailboxCustomDTO.getIsSubjectSelectable());

						// for employee only start
						mailbox.setSignature(mailboxCustomDTO.getSignature());
						if (null != mailboxCustomDTO.getGroup()) {
							MailboxGroupCustomDTO customGroup = mailboxCustomDTO.getGroup();
							MailboxGroup group = new MailboxGroup();
							group.setName(customGroup.getName());
							group.setCode(customGroup.getCode());
							mailbox.setGroup(group);
						}
						// for employee only end

						encapsulatorMailbox.setObject(mailbox);
						encapsulatorMailbox.setActions(mailboxCustomDTO.getActions());

						listMailboxes.add(encapsulatorMailbox);
					}
				}

				encapsulator.setObject(listMailboxes);
				encapsulatorMailboxList.setObject(encapsulator);
				return Response.status(Status.OK).entity(encapsulatorMailboxList).build();
			}

		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Retrieves a mailbox matching the input name. If includeActions parameter is provided, actions allowed on the
	 * mailbox are returned (see MailboxActionName).
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param mailboxName mailbox's name
	 * @param securityContext securityContext
	 * @param includeActionsString provided actions allowed on each mailbox are returned.
	 * @return Response with status of operation & mailbox list object
	 */
	@GET
	@Path("/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailbox(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("name") String mailboxName, @Context SecurityContext securityContext,
			@QueryParam("includeActions") String includeActionsString) {

		// Method signature to use in the log statements
		final String LOG_METHOD = "getMailbox(String, IncludeActions):Mailbox";

		try {

			LOGGER.debugHardCodedMessage(LOG_METHOD, SecurityContext + securityContext.toString());
			validatorUtil.validateMailboxName(mailboxName);

			IncludeActions includeActions = validatorUtil.validateIncludeAction(includeActionsString);
			MailboxCustomDTO mailboxCustomDTO = bankmailABPC.getMailbox(securityContext, mailboxName, includeActions);

			JSONContentEncapsulator<Mailbox> encapsulatorMailBox = new JSONContentEncapsulator<Mailbox>("mailbox");

			// output
			if (null != mailboxCustomDTO) {
				Mailbox mailbox = new Mailbox();
				mailbox.setName(mailboxCustomDTO.getName());
				mailbox.setDisplayName(mailboxCustomDTO.getDisplayName());
				mailbox.setIsSubjectSelectable(mailboxCustomDTO.getIsSubjectSelectable());

				// for employee only start
				mailbox.setSignature(mailboxCustomDTO.getSignature());
				if (null != mailboxCustomDTO.getGroup()) {
					MailboxGroupCustomDTO customGroup = mailboxCustomDTO.getGroup();
					MailboxGroup group = new MailboxGroup();
					group.setName(customGroup.getName());
					group.setCode(customGroup.getCode());
					mailbox.setGroup(group);
				}
				// for employee only end

				encapsulatorMailBox.setObject(mailbox);
				encapsulatorMailBox.setActions(mailboxCustomDTO.getActions());
			}
			return Response.status(Status.OK).entity(encapsulatorMailBox).build();

		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Saves new message. Only message attributes isSeen, inReplyTo, subject and content are taken from input. Attributes
	 * isSeen, subject and content are required. Employee must also choose message recipient. Reply messages have to have
	 * inReplyTo attribute filled in, only message id has to be provided. New message is created in "Draft" folder.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param mailboxName name of the mailbox
	 * @param securityContext securityContext
	 * @param mailboxMessageWrapper required message details
	 * @return Response with message id
	 */
	@POST
	@Path("/{name}/messages")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMessage(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("name") String mailboxName, @Context SecurityContext securityContext,
			MailboxMessageWrapper mailboxMessageWrapper) {

		// Method signature to use in the log statements
		final String LOG_METHOD = "createMessage(String,MailboxMessage):String";

		String id = null;
		try {

			LOGGER.debugHardCodedMessage(LOG_METHOD, securityContext + securityContext.toString());
			validatorUtil.validateCreateMessage(securityContext, mailboxName, mailboxMessageWrapper);
			MailboxMessage mailboxMessage = mailboxMessageWrapper.getMailboxMessage();

			MailboxMessageCustomDTO message = new MailboxMessageCustomDTO();

			// IS_SEEN
			message.setIsSeen(mailboxMessage.getIsSeen());

			// Only message id has to be provided when inReplyTo available
			if (null != mailboxMessage.getInReplyTo() && StringUtils.isNotBlank(mailboxMessage.getInReplyTo().getId())) {
				MailboxMessageCustomDTO inReplyTo = new MailboxMessageCustomDTO();
				inReplyTo.setId(mailboxMessage.getInReplyTo().getId());
				message.setInReplyTo(inReplyTo);
			}

			message.setSubject(mailboxMessage.getSubject());
			message.setContent(mailboxMessage.getContent());

			// Concerning Customer
			message.setConcerningCustomerBCNumber(String.valueOf(mailboxMessage.getConcerningCustomer().getBcNumber()));

			// recipient
			if (null != mailboxMessage.getTo() && mailboxMessage.getTo().size() > 0) {

				List<MailContact> orgTo = mailboxMessage.getTo();
				ArrayList<MailContactCustomDTO> arrTo = new ArrayList<MailContactCustomDTO>();

				for (MailContact mailContact : orgTo) {
					String address = mailContact.getAddress();
					if (StringUtils.isNotBlank(address)) {
						MailContactCustomDTO contactCustomDTO = new MailContactCustomDTO();
						contactCustomDTO.setAddress(address);
						arrTo.add(contactCustomDTO);
					}
				}

				// boolean flag for at least one contact should be provided
				if (arrTo.size() > 0) {
					message.setTo(arrTo);
				}
			}

			id = bankmailABPC.createMailboxMessage(securityContext, mailboxName, message);

			// Id encapsulatot
			JSONContentEncapsulator<String> encapsulatorId = new JSONContentEncapsulator<String>("id");
			encapsulatorId.setObject(id);
			return Response.status(Status.CREATED).entity(encapsulatorId).build();

		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Sends given message. Message has to be saved prior to sending. Message is removed from the folder after it is
	 * sent. During delivery it will be placed in the "Sent" folder. This operation is restricted to messages in "Draft"
	 * folder.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param mailboxName mailbox's name
	 * @param sendMessageInstructionWrapper SendMessageInstructionWrapper
	 * @param securityContext securityContext
	 * @return Response with with status code
	 */
	@POST
	@Path("/{name}/sendMessageInstruction")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMessage(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("name") String mailboxName, @Context SecurityContext securityContext,
			SendMessageInstructionWrapper sendMessageInstructionWrapper) {

		final String LOG_METHOD = "sendMessage(String,SendMessageInput):void";

		try {

			// validate send message input
			validatorUtil.validateSendMessage(mailboxName, sendMessageInstructionWrapper);

			String messageId = sendMessageInstructionWrapper.getSendMessageInstruction().getMessageId();

			bankmailABPC.sendMailboxMessage(securityContext, mailboxName, messageId);

			return Response.status(Status.NO_CONTENT).build();

		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Permanently delete messages from the mailbox.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param mailboxName mailbox name
	 * @param securityContext securityContext
	 * @param deleteMessagesInstructionWrapper DeleteMessagesInstructionWrapper
	 * @return Response with status code
	 */
	@POST
	@Path("/{name}/deleteMessagesInstruction")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteMessages(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("name") String mailboxName, @Context SecurityContext securityContext,
			DeleteMessagesInstructionWrapper deleteMessagesInstructionWrapper) {

		// Method signature to use in the log statements
		final String LOG_METHOD = "deleteMessages(String,DeleteMessagesInstruction):void";

		try {

			LOGGER.debugHardCodedMessage(LOG_METHOD, securityContext + securityContext.toString());
			validatorUtil.validateDeleteMessages(mailboxName, deleteMessagesInstructionWrapper);

			DeleteMessagesInstruction deleteMessagesInstruction = deleteMessagesInstructionWrapper
				.getDeleteMessagesInstruction();

			List<String> messageIds = deleteMessagesInstruction.getMessageIds();

			bankmailABPC.deleteMailboxMessages(securityContext, mailboxName, messageIds, deleteMessagesInstruction
				.getFromFolderName().getCode());
			return Response.status(Status.NO_CONTENT).build();
		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Retrieves messages from given folder within given mailbox. MailboxMessage contains following attributes: "id",
	 * "from", "to", "originationDate", "receivedDate", "messageType", "isSeen", "isAnswered" and "subject". If employee
	 * is logged in then bankmailAddress is returned in Contact otherwise it is skipped from output. Messages can be
	 * searched by given text. Messages can be filtered by MailboxMessageTypes. More than 1 MailboxMessageType can be
	 * provided, "|" used as separator. Result can be sorted by one of the available sort orders. See MessagesSortOrder.
	 * Result can be paged.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param securityContext securityContext
	 * @param mailboxName mailbox's name
	 * @param folderName folder name
	 * @param messageTypeFilter message Types(CONVERSATIOON|SERVICE)
	 * @param searchText search text string
	 * @param sortByString sorting order
	 * @param pageNumberString page no in paging result
	 * @param pageSizeString page size for paging
	 * @return Response with MailboxMessageList
	 */
	@GET
	@Path("/{name}/messages")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessages(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@Context SecurityContext securityContext, @PathParam("name") String mailboxName,
			@QueryParam("folderName") String folderName, @QueryParam("messageTypeFilter") String messageTypeFilter,
			@QueryParam("searchText") String searchText, @QueryParam("sortBy") String sortByString,
			@QueryParam("pageNumber") String pageNumberString, @QueryParam("pageSize") String pageSizeString) {

		final String LOG_METHOD = "getMessages(String,String,String,String,MessagesSortOrder,Integer,Integer):MailboxMessageList";

		Object mailboxMessageList = null;
		try {

			LOGGER.debugHardCodedMessage(LOG_METHOD, securityContext + securityContext.toString());
			validatorUtil.validateMailboxName(mailboxName);
			MailboxFolderName folder = validatorUtil.validateFolderName(folderName);
			MailMessageType[] mailboxMessageTypes = validatorUtil.validateAndMapMessageTypes(messageTypeFilter);
			MessagesSortOrder sortBy = validatorUtil.validateSortBy(sortByString);
			int pageSize = validatorUtil.validatePageSize(pageSizeString);
			int pageNumber = validatorUtil.validatePageNumber(pageNumberString, pageSize);

			List<MailMessageType> mailboxMessageTypesList = null;

			if (null != messageTypeFilter && messageTypeFilter.length() > 0) {
				mailboxMessageTypesList = Arrays.asList(mailboxMessageTypes);
			}

			MailboxMessageListDTO mailboxMessageListDTO = bankmailABPC.getMailboxMessages(securityContext, mailboxName, folder,
				mailboxMessageTypesList, searchText, sortBy, pageNumber, pageSize);

			MailboxUtil mailboxUtil = new MailboxUtil();
			mailboxMessageList = mailboxUtil.convertMessagesList(mailboxMessageListDTO);

			JSONContentEncapsulator<Object> encapsulatorMessages = new JSONContentEncapsulator<Object>(
				"mailboxMessageList");
			JSONContentEncapsulator<Object> encapsulator = new JSONContentEncapsulator<Object>("messages");
			encapsulator.setObject(mailboxMessageList);
			encapsulatorMessages.setObject(encapsulator);

			return Response.status(Status.OK).entity(encapsulatorMessages).build();

		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Retrieves a message. If employee is logged in then bankmailAddress is returned in Contact otherwise it is skipped
	 * from output. Employee gets also "senderName" attribute. If includeActions parameter is provided, actions allowed
	 * on the message are returned.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param mailboxName mailbox's name
	 * @param messageId message id
	 * @param securityContext securityContext
	 * @param includeActionsString IncludeActions
	 * @return Response with MailboxMessage
	 */
	@GET
	@Path("/{name}/messages/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessage(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("name") String mailboxName, @PathParam("id") String messageId, @Context SecurityContext securityContext,
			@QueryParam("includeActions") String includeActionsString) {

		// Method signature to use in the log statements
		final String LOG_METHOD = "getMessage(String,String,IncludeActions):MailboxMessage";

		try {
			LOGGER.debugHardCodedMessage(LOG_METHOD, securityContext + securityContext.toString());
			validatorUtil.validateMailboxName(mailboxName);
			validatorUtil.validateMessageId(messageId);
			IncludeActions includeActions = validatorUtil.validateIncludeAction(includeActionsString);

			MailboxMessageCustomDTO mailboxMessageCustomDTO = bankmailABPC.getMailboxMessage(securityContext, mailboxName,
				messageId, includeActions);
			MailboxUtil mailboxUtil = new MailboxUtil();
			MailboxMessage mailboxMessage = mailboxUtil.convertMessage(mailboxMessageCustomDTO);
			JSONContentEncapsulator<MailboxMessage> encapsulatorMessage = new JSONContentEncapsulator<MailboxMessage>(
				"mailboxMessage");

			if (mailboxMessage == null || StringUtils.isBlank(mailboxMessage.getId())) {
				return Response.status(Status.NOT_FOUND).entity(encapsulatorMessage).build();
			} else {

				if (null != includeActions && StringUtils.isNotBlank(includeActions.getValue())) {
					encapsulatorMessage.setActions(mailboxMessageCustomDTO.getActions());
				}

				encapsulatorMessage.setObject(mailboxMessage);
				return Response.status(Status.OK).entity(encapsulatorMessage).build();
			}
		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Retrieves all preceding messages in the reply messages chain. Only messages available in the folders "Inbox",
	 * "Archive" and "Sent" are returned. Messages are returned in reversed chronological order (latest first). If
	 * message has receivedDate (received messages only), then it is used for sorting, otherwise originationDate is used.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param securityContext securityContext
	 * @param mailboxName mailbox's name
	 * @param messageId message id
	 * @return Response with MailboxMessageList
	 */
	@GET
	@Path("/{name}/messages/{id}/thread")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMessageThread(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@Context SecurityContext securityContext, @PathParam("name") String mailboxName, @PathParam("id") String messageId) {

		// Method signature to use in the log statements
		final String LOG_METHOD = "getMessageThread(String,String):MailboxMessageList";

		try {
			// validate mailboxname
			validatorUtil.validateMailboxName(mailboxName);

			// validate messageid
			validatorUtil.validateMessageId(messageId);
			LOGGER.debugHardCodedMessage(LOG_METHOD, securityContext + securityContext.toString());

			MailboxMessageListDTO mailboxMessageListDTO = bankmailABPC.getMailboxMessageThread(securityContext, mailboxName,
				messageId);

			MailboxUtil mailboxUtil = new MailboxUtil();
			Object object = mailboxUtil.convertMessagesList(mailboxMessageListDTO);

			JSONContentEncapsulator<Object> encapsulatorMessages = new JSONContentEncapsulator<Object>(
				"mailboxMessageList");
			JSONContentEncapsulator<Object> encapsulator = new JSONContentEncapsulator<Object>("messages");
			encapsulator.setObject(object);
			encapsulatorMessages.setObject(encapsulator);

			return Response.status(Status.OK).entity(encapsulatorMessages).build();

		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Updates given message. Only "isSeen" attribute can be set to "true". All other changes will be ignored. For
	 * messages in "Draft" folder subject and content can be updated. Employee can also change message recipient.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param securityContext securityContext
	 * @param mailboxName mailbox's name
	 * @param messageId message id
	 * @param mailboxMessageWrapper MailboxMessageWrapper
	 * @return Response with message id
	 */
	@PUT
	@Path("/{name}/messages/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateMessage(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@Context SecurityContext securityContext, @PathParam("name") String mailboxName, @PathParam("id") String messageId,
			MailboxMessageWrapper mailboxMessageWrapper) {

		final String LOG_METHOD = "updateMessage(String,String,MailboxMessage):String";
		String id = null;
		try {
			LOGGER.debugHardCodedMessage(LOG_METHOD, securityContext + securityContext.toString());
			validatorUtil.validateUpdateMessage(mailboxName, messageId, mailboxMessageWrapper);

			MailboxMessage mailboxMessage = mailboxMessageWrapper.getMailboxMessage();
			MailboxMessageCustomDTO message = new MailboxMessageCustomDTO();
			message.setId(messageId);
			message.setIsSeen(mailboxMessage.getIsSeen());
			message.setSubject(mailboxMessage.getSubject());
			message.setContent(mailboxMessage.getContent());

			// recipient
			if (null != mailboxMessage.getTo() && mailboxMessage.getTo().size() > 0) {

				List<MailContact> orgTo = mailboxMessage.getTo();

				List<MailContactCustomDTO> to = new ArrayList<MailContactCustomDTO>();

				for (MailContact mailContact : orgTo) {
					String address = mailContact.getAddress();
					if (StringUtils.isNotBlank(address)) {
						MailContactCustomDTO contactCustomDTO = new MailContactCustomDTO();
						contactCustomDTO.setAddress(address);
						to.add(contactCustomDTO);
					}
				}

				if (to.size() > 0) {
					message.setTo(to);
				}
			}
			id = bankmailABPC.updateMailboxMessage(securityContext, mailboxName, message);
			JSONContentEncapsulator<String> encapsulatorId = new JSONContentEncapsulator<String>("id");
			encapsulatorId.setObject(id);
			return Response.status(Status.OK).entity(encapsulatorId).build();

		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Retrieve mailbox folders.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param securityContext securityContext
	 * @param mailboxName name of the mailbox
	 * @return Response
	 */
	@GET
	@Path("/{name}/folders/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFolders(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@Context SecurityContext securityContext, @PathParam("name") String mailboxName) {

		// Method signature to use in the log statements
		final String LOG_METHOD = "getFolders(String):MailboxFolderList";

		try {
			LOGGER.debugHardCodedMessage(LOG_METHOD, securityContext + securityContext.toString());
			validatorUtil.validateMailboxName(mailboxName);

			FolderListDTO folderListDTO = bankmailABPC.getFolders(securityContext, mailboxName);

			// output check
			JSONContentEncapsulator<Object> encapsulatorFolderList = new JSONContentEncapsulator<Object>(
				"mailboxFolderList");
			JSONContentEncapsulator<List<JSONContentEncapsulator<MailboxFolder>>> encapsulator = new JSONContentEncapsulator<List<JSONContentEncapsulator<MailboxFolder>>>(
				"folders");

			if (null == folderListDTO || folderListDTO.getFolders() == null) {
				return Response.status(Status.NOT_FOUND).entity(encapsulatorFolderList).build();
			} else {
				List<FolderCustomDTO> folders = folderListDTO.getFolders();

				List<JSONContentEncapsulator<MailboxFolder>> listFolders = new ArrayList<JSONContentEncapsulator<MailboxFolder>>();

				for (FolderCustomDTO folderCustomDTO2 : folders) {
					MailboxFolder mailboxFolder = new MailboxFolder();

					if (null != folderCustomDTO2) {
						JSONContentEncapsulator<MailboxFolder> encapsulatorFolder = new JSONContentEncapsulator<MailboxFolder>(
							"folder");
						mailboxFolder.setName(MailboxFolderName.fromString(folderCustomDTO2.getName()));

						mailboxFolder.setUnseenMessagesCount(folderCustomDTO2.getUnseenMessagesCount());
						encapsulatorFolder.setObject(mailboxFolder);
						listFolders.add(encapsulatorFolder);
					}
				}
				encapsulator.setObject(listFolders);
				encapsulatorFolderList.setObject(encapsulator);
				return Response.status(Status.OK).entity(encapsulatorFolderList).build();
			}
		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (EnumException enumException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_ENUM_EXCEPTION);
			throw new WebApplicationException(enumException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Retrieves mailbox folder.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param securityContext securityContext
	 * @param mailboxName name of the mailbox
	 * @param folderName folder Name
	 * @return Response with MailboxFolder
	 */
	@GET
	@Path("/{name}/folders/{folderName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFolder(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@Context SecurityContext securityContext, @PathParam("name") String mailboxName,
			@PathParam("folderName") String folderName) {

		// Method signature to use in the log statements
		final String LOG_METHOD = "getFolder(String,MailboxFolderName ):MailboxFolder";

		try {
			LOGGER.debugHardCodedMessage(LOG_METHOD, securityContext + securityContext.toString());
			// validate mailbox name
			validatorUtil.validateMailboxName(mailboxName);

			// validate Folder Name
			MailboxFolderName folder = validatorUtil.validateFolderName(folderName);

			FolderCustomDTO folderCustomDTO = bankmailABPC.getFolder(securityContext, mailboxName, folder);

			// Folder encapsulator
			JSONContentEncapsulator<MailboxFolder> encapsulatorFolder = new JSONContentEncapsulator<MailboxFolder>(
				"mailboxFolder");
			// check output
			if (null != folderCustomDTO) {
				MailboxFolder mailboxFolder = new MailboxFolder();
				mailboxFolder.setName(MailboxFolderName.fromString(folderCustomDTO.getName()));
				mailboxFolder.setUnseenMessagesCount(folderCustomDTO.getUnseenMessagesCount());
				mailboxFolder.setUnseenMessagesCount(folderCustomDTO.getUnseenMessagesCount());
				mailboxFolder.setUnseenAlertMessagesCount(folderCustomDTO.getUnseenAlertMessagesCount());
				encapsulatorFolder.setObject(mailboxFolder);
			}
			return Response.status(Status.OK).entity(encapsulatorFolder).build();

		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_CONSTRAINTS_VIOLATED_EXCEPTION,
				constraintsViolatedException);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BANKMAIL_APPLICATION_EXCEPTION,
				bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}

	}

}
