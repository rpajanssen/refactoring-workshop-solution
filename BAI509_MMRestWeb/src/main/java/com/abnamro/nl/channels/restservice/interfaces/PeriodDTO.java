package com.abnamro.nl.channels.restservice.interfaces;

import com.abnamro.nl.dto.util.AbstractDTO;
import com.abnamro.nl.enumeration.PeriodUnit;

/**
 * PeriodDTO 
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	04-10-2012	Initial version	  Release 1.0
 * </PRE>
 * @author TCS 
 * @see
 */
public class PeriodDTO extends AbstractDTO{
	private static final long serialVersionUID = 1L;
	/**
	 * periodUnit.
	 */
	private PeriodUnit periodUnit;
	
	/**
	 * periodValue
	 */
	private Integer periodValue;
	
	/**
	 * @return the periodUnit
	 */
	public PeriodUnit getPeriodUnit() {
		return periodUnit;
	}


	/**
	 * @param periodUnit the periodUnit to set
	 */
	public void setPeriodUnit(PeriodUnit periodUnit) {
		this.periodUnit = periodUnit;
	}


	/**
	 * @return the periodValue
	 */
	public Integer getPeriodValue() {
		return periodValue;
	}


	/**
	 * @param periodValue the periodValue to set
	 */
	public void setPeriodValue(Integer periodValue) {
		this.periodValue = periodValue;
	}
	
}
