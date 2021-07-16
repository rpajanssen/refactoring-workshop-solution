package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * BusinessContact Stores business contact information
 *
 * <PRE>
 * &lt;B&gt;History:&lt;/B&gt;
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 *
 * @author
 * @see
 */
public class BusinessContact {

    private long bcNumber;
    private String interpayName;

    /**
     * @return the bcNumber
     */
    public long getBcNumber() {
        return bcNumber;
    }

    /**
     * @param bcNumber the bcNumber to set
     */
    public void setBcNumber(long bcNumber) {
        this.bcNumber = bcNumber;
    }

    /**
     * @return String the interpayName as short name
     */
    @JsonProperty("shortName")
    public String getInterpayName() {
        return interpayName;
    }

    /**
     * @param interpayName the interpayName to set
     */
    public void setInterpayName(String interpayName) {
        this.interpayName = interpayName;
    }

}