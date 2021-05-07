package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * ServiceConceptDTO Defines how incoming Bankmail messages have to be served. ServiceConcept can be applied to a
 * customer. Result of application is SendBankmailPolicy
 * 
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				29-08-2012	Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */
public class ServiceConceptDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Description : Message key in Tridion which contains the SLA information for the bankmail customer.
	 */
	private String slaMessageId;

	/**
	 * Description : True when the bankmail subject is selectable.
	 */
	private boolean canSelectSubject;

	/**
	 * Description : Strategy to be applied to a customer to get destination address of incoming message.
	 */
	private EmployeeMailboxTemplate destinationMailboxTemplate;

	/**
	 * @return the slaMessageId
	 */
	public String getSlaMessageId() {
		return slaMessageId;
	}

	/**
	 * @param slaMessageId the slaMessageId to set
	 */
	public void setSlaMessageId(String slaMessageId) {
		this.slaMessageId = slaMessageId;
	}

	/**
	 * @return the canSelectSubject
	 */
	public boolean isCanSelectSubject() {
		return canSelectSubject;
	}

	/**
	 * @param canSelectSubject the canSelectSubject to set
	 */
	public void setCanSelectSubject(boolean canSelectSubject) {
		this.canSelectSubject = canSelectSubject;
	}

	/**
	 * @return EmployeeMailboxTemplate:Strategy to be applied to a customer to get destination address of incoming
	 *         message
	 */
	public EmployeeMailboxTemplate getDestinationMailboxTemplate() {
		return destinationMailboxTemplate;
	}

	/**
	 * @param destinationMailboxTemplate EmployeeMailboxTemplate:Strategy to be applied to a customer to get destination
	 *           address of incoming message
	 */
	public void setDestinationMailboxTemplate(EmployeeMailboxTemplate destinationMailboxTemplate) {
		this.destinationMailboxTemplate = destinationMailboxTemplate;
	}

}
