package com.abnamro.nl.channels.geninfo.bankmail.resources;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * IncludeActions Enum for IncludeActions
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
public enum IncludeActions {

    BASIC("BASIC"), EXTENDED("EXTENDED"), EMANDATES("EMANDATES");

    private static final Map<String, IncludeActions> lookup = new HashMap<>();

    static {
        for (IncludeActions includeAction : EnumSet.allOf(IncludeActions.class)) {
            lookup.put(includeAction.getValue(), includeAction); }
    }

    private String value;

    /**
     * @param value value
     */
    private IncludeActions(String value) {
        this.value = value;
    }

    /**
     * Returns IncludeActions based on the lookup
     * @return String IncludeActions
     */
    public String getValue() {
        return value;
    }

    public static IncludeActions get(String value) {
        return lookup.get(value);
    }
}
