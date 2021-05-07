package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * BusinessContactCustomDTO 
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS				01-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public class BusinessContactCustomDTO extends AbstractDTO {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * BC number of the customer
	 */
	private Long bcNumber;
	
	/**
	 * Short name of the customer
	 */
	private String shortName;

	/**
	 * @return Long:BC number of the customer
	 */
	public Long getBcNumber() {
		return bcNumber;
	}

	/**
	 * @param bcNumber Long:BC number of the customer
	 */
	public void setBcNumber(Long bcNumber) {
		this.bcNumber = bcNumber;
	}

	/**
	 * @return String:Short name of the customer
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName  String:Short name of the customer
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	

}
