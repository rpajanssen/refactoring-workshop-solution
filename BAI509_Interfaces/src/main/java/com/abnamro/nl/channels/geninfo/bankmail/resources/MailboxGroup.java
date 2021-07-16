package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.abnamro.nl.dto.util.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * MailboxGroup Stores properties for a mailbox group
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MailboxGroup extends AbstractDTO {
    private static final long serialVersionUID = 1L;
    /**
     * Name of the group
     */
    private String name;

    /**
     * The group code
     */
    private String code;

    /**
     * @return String:Name of the group
     */
    public String getName() {
        return name;
    }

    /**
     * @param name String:Name of the group
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String:The group code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code tring:The group code
     */
    public void setCode(String code) {
        this.code = code;
    }
}
