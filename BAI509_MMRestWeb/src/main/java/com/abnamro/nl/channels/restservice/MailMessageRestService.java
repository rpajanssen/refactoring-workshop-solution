package com.abnamro.nl.channels.restservice;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.genj.securitycontext.rest.interceptors.requestinterceptors.SecurityContextEnricherBinding;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.interfaces.BankmailABPC;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailRestServiceLogKeys;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailMessageCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailMessageListDTO;
import com.abnamro.nl.channels.rest.interceptors.requestinterceptors.RequestValidatorBinding;
import com.abnamro.nl.channels.rest.interceptors.responseinterceptors.SuppressCacheBinding;
import com.abnamro.nl.channels.rest.interceptors.responseinterceptors.accesslog.AccessLogInterceptorBinding;
import com.abnamro.nl.channels.restservice.interfaces.PeriodDTO;
import com.abnamro.nl.channels.restservice.utils.LogHelper;
import com.abnamro.nl.channels.restservice.utils.MailMessageServiceRequestValidator;
import com.abnamro.nl.channels.restservice.utils.MailMessageServiceResponseBuilder;
import com.abnamro.nl.enumeration.MailMessageType;
import com.abnamro.nl.exceptions.ConstraintsViolatedException;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * MailMessageRestService: 
 * Rest Service for Bankmail Plus for MailMessage Resource.
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	04-10-2012	Initial version	  Release 1.0
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
public class MailMessageRestService {
	
    /**
	 * Singleton instance of LogHelper.
	 */
	private static final LogHelper LOGGER = new LogHelper(MailMessageRestService.class);

	//plz implement Employee file as part of GHIA
	@Inject
	private BankmailABPC bankmailABPC;
    /**
     * Singleton instance of validator.
     */
    private static MailMessageServiceRequestValidator validatorUtil = new MailMessageServiceRequestValidator();
	
    /**
	 * Retrieves mail message history of an input customer.
	 * 
	 * @param request                       : [HttpServletRequest] httpservletrequest
	 * @param response                      : [HttpServletResponse] httpservletresponse
	 * @param concerningCustomerBCNumberStr : [String] concerningCustomerBCNumber 
	 * @param periodValueStr                : [String] periodValue
	 * @param periodUnitStr                 : [String] periodUnit
	 * @param messageTypesStr               : [String] messageTypes
     * @param securityContext					: [SecurityContext] securityContext
	 * @return response                     : [Response] Json representaion of retrieved mail message history.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailMessageHistory (
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@QueryParam("concerningCustomerBCNumber") String concerningCustomerBCNumberStr,
			@QueryParam("periodValue") String periodValueStr,
			@QueryParam("periodUnit") String periodUnitStr,
			@QueryParam("messageTypes") String messageTypesStr,
			@Context SecurityContext securityContext) {
		
		final String LOG_METHOD = "getMailMessageHistory(HttpServletRequest, HttpServletResponse, String, String, String, String):Response";		
		try {
			
			//Valid input parameters.
			validatorUtil.validateConcerningCustomerBCNumber(concerningCustomerBCNumberStr);
			PeriodDTO period = validatorUtil.validatePeriod(periodUnitStr, periodValueStr);
			List<MailMessageType> mailMessageTypes = validatorUtil.validateMessageTypes(messageTypesStr);			
			
			MailMessageListDTO mailMessageList = bankmailABPC.getMailMessageHistory(securityContext, concerningCustomerBCNumberStr, period.getPeriodValue(), period.getPeriodUnit(), mailMessageTypes);
			
			return MailMessageServiceResponseBuilder.buildSuccessResponse( mailMessageList );
						
		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.logError(LOG_METHOD, BankmailRestServiceLogKeys.LOG_MAILMESSAGE_CONSTRAINTS_VIOLATED_EXCEPTION);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);			
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.logError(LOG_METHOD, BankmailRestServiceLogKeys.LOG_MAILMESSAGE_BANKMAIL_EXCEPTION, bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Retrieves MailMessage for input MessageId
	 * 
	 * @param request                : [HttpServletRequest] httpservletrequest
	 * @param response               : [HttpServletResponse] httpservletresponse
	 * @param mailMessageIdStr       : [String] mailMessageId
	 * @param securityContext			 : [SecurityContext] securityContext
	 * @return response              : [Response] Json representation of retreived mail contacts.
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMailMessage (
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("id") String mailMessageIdStr,
			@Context SecurityContext securityContext) {
		
		final String LOG_METHOD = "getMailMessage(HttpServletRequest, HttpServletResponse, String):Response";		
		
		try {
			
			//Validate input parameters.
			validatorUtil.validateMailMessageId(mailMessageIdStr);
			
			MailMessageCustomDTO mailMessage = bankmailABPC.getMailMessage(securityContext, mailMessageIdStr);
			
			return MailMessageServiceResponseBuilder.buildSuccessResponse( mailMessage );
						
		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.logError(LOG_METHOD, BankmailRestServiceLogKeys.LOG_MAILMESSAGE_CONSTRAINTS_VIOLATED_EXCEPTION);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.logError(LOG_METHOD, BankmailRestServiceLogKeys.LOG_MAILMESSAGE_BANKMAIL_EXCEPTION, null, bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);			
		}
	}
}
