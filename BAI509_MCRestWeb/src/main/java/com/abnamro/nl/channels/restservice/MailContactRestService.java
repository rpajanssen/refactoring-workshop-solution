package com.abnamro.nl.channels.restservice;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.genj.securitycontext.annotations.EndFunction;
import com.abnamro.genj.securitycontext.annotations.EndFunctions;
import com.abnamro.genj.securitycontext.rest.interceptors.requestinterceptors.SecurityContextEnricherBinding;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.interfaces.BankmailABPC;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailRestServiceLogKeys;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailContactListDTO;
import com.abnamro.nl.channels.rest.interceptors.responseinterceptors.SuppressCacheBinding;
import com.abnamro.nl.channels.rest.interceptors.responseinterceptors.accesslog.AccessLogInterceptorBinding;
import com.abnamro.nl.channels.restservice.utils.MailContactServiceRequestValidator;
import com.abnamro.nl.channels.restservice.utils.MailContactServiceResponseBuilder;
import com.abnamro.nl.exceptions.ConstraintsViolatedException;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * MailContactRestService:
 * Rest Service for Bankmail Plus for MailContact Resource.
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	27-09-2012	Initial version	  Release 1.0
 * </PRE>
 *
 * @author
 * @see
 */
@SuppressCacheBinding
@AccessLogInterceptorBinding
@SecurityContextEnricherBinding
@LogInterceptorBinding
@Path("/")
public class MailContactRestService {
    /**
	 * Singleton instance of LogHelper.
	 */
	private static final LogHelper LOGGER = new LogHelper(MailContactRestService.class);

	//plz implemnt Employee file as part of GHIA
	@Inject
	private BankmailABPC bankmailABPC;
    /**
     * Singleton instance of validator.
     */
    private static MailContactServiceRequestValidator validatorUtil = new MailContactServiceRequestValidator();

	/**
	 * Retrieves all mail contacts for a selected customer.
	 *
	 * @param request                       HttpServletRequest
	 * @param response                      HttpServletResponse
	 * @param concerningCustomerBCNumberStr name of the mailbox
	 * @param securityContext               securityContext
	 * @return Response with MailContacts
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@EndFunctions(endFunctions=	{
			@EndFunction(name="EmployeeClientReach",product = "",action = "")
	 })
	public Response getMailContacts (
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@QueryParam("concerningCustomerBCNumber") String concerningCustomerBCNumberStr ,
			@Context SecurityContext securityContext) {
		
		final String LOG_METHOD = "getMailContacts(HttpServletRequest, HttpServletResponse, String):Response";		
		
		try {
			
			Long concerningCustomerBCNumber = validatorUtil.validateConcerningCustomerBCNumber(concerningCustomerBCNumberStr);
			
			MailContactListDTO mailContacts = bankmailABPC.getMailContacts(securityContext, concerningCustomerBCNumber);
			
			return MailContactServiceResponseBuilder.buildSuccessResponse( mailContacts );
						
		} catch (ConstraintsViolatedException constraintsViolatedException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_MAILCONTACT_CONSTRAINTS_VIOLATED_EXCEPTION);
			throw new WebApplicationException(constraintsViolatedException, Response.Status.BAD_REQUEST);
		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_MAILCONTACT_BANKMAIL_EXCEPTION, bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

}
