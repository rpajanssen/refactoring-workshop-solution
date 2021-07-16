package com.abnamro.nl.channels.restservice.utils;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailContactCustomDTO;
import com.abnamro.nl.channels.geninfo.bankmail.interfaces.MailContactListDTO;
import com.abnamro.nl.channels.geninfo.bankmail.resources.MailContact;
import com.abnamro.nl.logging.log4j2.interceptors.LogInterceptorBinding;
import com.abnamro.nl.rest.actions.JSONContentEncapsulator;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

/**
 * MailContactRestServiceResponseBuilder: Response Builder for MailContact RestService
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	10-01-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
@LogInterceptorBinding
public class MailContactServiceResponseBuilder {
	/**
	 * This method is used to build successful json representations of retrieved mailcontacts.
	 * @param mailContacts : [ MailContactListDTO ] List of input retrieved mailcontacts.
	 * @return response : [ Response ] Json representation of mailcontacts.
	 */
	public static Response buildSuccessResponse(MailContactListDTO mailContacts) {

		JSONContentEncapsulator<Object> encapsulatorMailContactsList = new JSONContentEncapsulator<Object>(
			"mailContactList");
		JSONContentEncapsulator<List<JSONContentEncapsulator<MailContact>>> encapsulator = new JSONContentEncapsulator<List<JSONContentEncapsulator<MailContact>>>(
			"mailContacts");
		List<JSONContentEncapsulator<MailContact>> listMailContacts = new ArrayList<JSONContentEncapsulator<MailContact>>();
		encapsulator.setObject(listMailContacts);
		encapsulatorMailContactsList.setObject(encapsulator);

		if (null != mailContacts && null != mailContacts.getMailContactCustomDTOs()
				&& mailContacts.getMailContactCustomDTOs().size() > 0) {

			for (MailContactCustomDTO mailContactCustomDTO : mailContacts.getMailContactCustomDTOs()) {
				MailContact mailContact = new MailContact();
				if (mailContactCustomDTO != null) {
					mailContact.setDisplayName(mailContactCustomDTO.getDisplayName());
					mailContact.setAddress(mailContactCustomDTO.getAddress());
					JSONContentEncapsulator<MailContact> encapsulatorMailContact = new JSONContentEncapsulator<MailContact>(
						"mailContact");
					encapsulatorMailContact.setObject(mailContact);
					listMailContacts.add(encapsulatorMailContact);
				}
			}
		}

		return Response.status(Status.OK).entity(encapsulatorMailContactsList).build();

	}

}
