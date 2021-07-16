package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * MailActionCustomDTO
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * Godwin			  31-05-2016  Initial version	  Release 1.0
 * </PRE>
 * @author
 * @see
 */

public class MailActionCustomDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * Identifying name of the mail action.
	 */
	private String name;

	/**
	 * A list of parameters that need to be passed to the triggered process.
	 */
	private String[] params;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the params
	 */
	public String[] getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(String[] params) {
		this.params = params;
	}

}
