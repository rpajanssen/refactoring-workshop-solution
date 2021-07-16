package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import java.util.List;

import com.abnamro.nl.dto.util.AbstractDTO;


/**
 * SendBankmailPolicyListDTO 
 * List of SendBankmailPolicies.
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				29-08-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public class SendBankmailPolicyListDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * List of sendBankmailPolicies
	 */
	private List<SendBankmailPolicyDTO> sendBankmailPolicies;
	/**
	 * @return the sendBankmailPolicies
	 */
	public List<SendBankmailPolicyDTO> getSendBankmailPolicies() {
		return sendBankmailPolicies;
	}

	/**
	 * @param sendBankmailPolicies
	 *            the sendBankmailPolicies to set
	 */
	public void setSendBankmailPolicies(
			List<SendBankmailPolicyDTO> sendBankmailPolicies) {
		this.sendBankmailPolicies = sendBankmailPolicies;
	}
	
}
