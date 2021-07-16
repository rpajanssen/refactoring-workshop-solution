package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.businesscontactgroup.service.businesscontact.interfaces.BusinessContactExtendedInfoDTO;
import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * SendBankmailPolicyDTO 
 * Bankmail policy related to sending of bankmails concerning particular customer
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				29-05-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */

public class SendBankmailPolicyDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * Description : Message key in Tridion which contains the SLA information
	 * for the bankmail customer.
	 */
	private String slaMessageId;

	/**
	 * Description : True when the bankmail subject is selectable.
	 */
	private Boolean canSelectSubject;

	/**
	 * Description : Destination bankmail address.
	 */
	private MailContactCustomDTO destination;

	/**
	 * Description : Bankmail message can be send concerning this customer.
	 */
	private BusinessContactExtendedInfoDTO customer;

	/**
	 * @return the slaMessageId
	 */
	public String getSlaMessageId() {
		return slaMessageId;
	}

	/**
	 * @param slaMessageId
	 *            the slaMessageId to set
	 */
	public void setSlaMessageId(String slaMessageId) {
		this.slaMessageId = slaMessageId;
	}
	

	/**
	 * @return the canSelectSubject
	 */
	public Boolean getCanSelectSubject() {
		return canSelectSubject;
	}

	/**
	 * @param canSelectSubject
	 *            the canSelectSubject to set
	 */
	public void setCanSelectSubject(Boolean canSelectSubject) {
		this.canSelectSubject = canSelectSubject;
	}

	/**
	 * @return the destination
	 */
	public MailContactCustomDTO getDestination() {
		return destination;
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public void setDestination(MailContactCustomDTO destination) {
		this.destination = destination;
	}

	/**
	 * @return the customer
	 */
	public BusinessContactExtendedInfoDTO getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(BusinessContactExtendedInfoDTO customer) {
		this.customer = customer;
	}

}
