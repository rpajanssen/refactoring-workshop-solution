package com.abnamro.nl.channels.restservices;

import com.abnamro.genj.generic.SecurityContext;
import com.abnamro.genj.securitycontext.rest.interceptors.requestinterceptors.SecurityContextEnricherBinding;
import com.abnamro.nl.businesscontactgroup.service.businesscontact.interfaces.BusinessContactExtendedInfoDTO;
import com.abnamro.nl.channels.geninfo.bankmail.abpc.interfaces.BankmailABPC;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailRestServiceLogKeys;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.SendBankmailPolicyDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.SendBankmailPolicyListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.resources.BusinessContact;
import com.abnamro.nl.channels.geninfo.bankmail.resources.MailContact;
import com.abnamro.nl.channels.geninfo.bankmail.resources.SendBankmailPolicy;
import com.abnamro.nl.channels.rest.interceptors.requestinterceptors.RequestValidatorBinding;
import com.abnamro.nl.channels.rest.interceptors.responseinterceptors.SuppressCacheBinding;
import com.abnamro.nl.channels.rest.interceptors.responseinterceptors.accesslog.AccessLogInterceptorBinding;
import com.abnamro.nl.logging.log4j2.helper.LogHelper;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.rest.actions.JSONContentEncapsulator;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;


/**
 * BankmailPolicyRestService: Retrieves the SendBankmailPolicy resources
 * applicable for the logged in user. The logged in user is allowed to send
 * bankmails concerning customers whom he/she is authorized for. For each of
 * these customers, a send policy maybe attached. Only customers (that have a
 * send policy) concerning whom bankmails can be sent are returned as part of
 * this operation..
 * 
 * <PRE>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	18-09-2012	Initial version	  Release 1.0
 * </PRE>
 * 
 * @author
 * @see
 */
@SuppressCacheBinding
@AccessLogInterceptorBinding
@SecurityContextEnricherBinding
@RequestValidatorBinding
@LogInterceptorBinding
@Path("/")
public class BankmailPolicyRestService {
	private static final LogHelper LOGGER = new LogHelper(BankmailPolicyRestService.class);

	@Inject
	private BankmailABPC bankmailABPC;
	/**
	 * Retrieves the SendBankmailPolicy resources applicable for the logged in
	 * user. The logged in user is allowed to send bankmails concerning
	 * customers whom he/she is autcom.fasterxml.jacksonhorized for. For each of these customers, a
	 * send policy maybe attached. Only customers (that have a send policy)
	 * concerning whom bankmails can be sent are returned as part of this
	 * operation.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param securityContext
	 * 	          SecurityContext
	 * @return Response SendBankmailPolicyList
	 */
	@GET
	@Path("/send")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSendBankmailPolicies(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@Context SecurityContext securityContext) {
		// Method signature to use in the log statements
		final String LOG_METHOD = "getSendBankmailPolicies():SendBankmailPolicyList";

		try {
			LOGGER.debugHardCodedMessage(LOG_METHOD, "SecurityContext:"+securityContext.toString());
			SendBankmailPolicyListDTO bankmailPolicyListDTO = bankmailABPC
					.getSendBankmailPolicies(securityContext);

			JSONContentEncapsulator<Object> encapsulatorPolicyList = new JSONContentEncapsulator<>(
					"sendBankmailPolicyList");
			JSONContentEncapsulator<List<JSONContentEncapsulator<SendBankmailPolicy>>> encapsulator = new JSONContentEncapsulator<>(
					"sendBankmailPolicies");
			List<JSONContentEncapsulator<SendBankmailPolicy>> listPolicys = new ArrayList<>();
			if (null == bankmailPolicyListDTO
					|| bankmailPolicyListDTO.getSendBankmailPolicies() == null) {


				//listPolicys
				encapsulator.setObject(listPolicys);
				encapsulatorPolicyList.setObject(encapsulator);
				return Response.status(Status.OK).entity(
						encapsulatorPolicyList).build();

			} else {

				List<SendBankmailPolicyDTO> sendBankmailPolicys = bankmailPolicyListDTO
						.getSendBankmailPolicies();

				for (SendBankmailPolicyDTO sendBankmailPolicyDTO2 : sendBankmailPolicys) {

					if (null != sendBankmailPolicyDTO2) {

						SendBankmailPolicy sendBankmailPolicy = new SendBankmailPolicy();
						sendBankmailPolicy
								.setSlaMessageKey(sendBankmailPolicyDTO2
										.getSlaMessageId());
						sendBankmailPolicy
								.setIsSubjectSelectable(sendBankmailPolicyDTO2
										.getCanSelectSubject());

						MailContact mailContact = new MailContact();
						mailContact.setDisplayName(sendBankmailPolicyDTO2
								.getDestination().getDisplayName());
						sendBankmailPolicy.setDestination(mailContact);

						BusinessContactExtendedInfoDTO extendedInfoDTO = sendBankmailPolicyDTO2
								.getCustomer();
						BusinessContact customer = new BusinessContact();
						customer.setBcNumber(new Long(extendedInfoDTO
								.getBusinessContactInfo().getBcNumber()));
						customer
								.setInterpayName(extendedInfoDTO.getInterpayName());
						sendBankmailPolicy.setCustomer(customer);

						JSONContentEncapsulator<SendBankmailPolicy> encapsulatorPolicy = new JSONContentEncapsulator<>(
								"sendBankmailPolicy");
						encapsulatorPolicy.setObject(sendBankmailPolicy);
						listPolicys.add(encapsulatorPolicy);
					}
				}

     			encapsulator.setObject(listPolicys);
				encapsulatorPolicyList.setObject(encapsulator);
				return Response.status(Status.OK)
						.entity(encapsulatorPolicyList).build();
			}

		} catch (BankmailApplicationException bankmailApplicationException) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BP_BANKMAIL_APPLICATION_EXCEPTION, bankmailApplicationException);
			throw new WebApplicationException(bankmailApplicationException, Response.Status.INTERNAL_SERVER_ERROR);
		} catch (Exception exception) {
			LOGGER.error(LOG_METHOD, BankmailRestServiceLogKeys.LOG_BP_TECHINCAL_EXCEPTION, exception);
			throw new WebApplicationException(exception, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
}