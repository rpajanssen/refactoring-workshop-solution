package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.abnamro.nl.dto.util.AbstractDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * SendMessageInstruction: Instructions for a sending bankmail
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
public class SendMessageInstruction extends AbstractDTO {
    private static final long serialVersionUID = 1L;
    /**
     * Reference to the message to be sent. Message must be in the "DRAFT" folder.
     */
    private String messageId;

    /**
     * @return String reference to the message to be sent.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId :Reference to the message to be sent.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}