package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * DeleteMessagesInstructionWrapper Wrapper over DeleteMessageInstruction class
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
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DeleteMessagesInstructionWrapper {

    private DeleteMessagesInstruction deleteMessagesInstruction;

    /**
     * @return DeleteMessagesInstruction
     */
    public DeleteMessagesInstruction getDeleteMessagesInstruction() {
        return deleteMessagesInstruction;
    }

    /**
     * @param deleteMessagesInstruction DeleteMessagesInstruction
     */
    public void setDeleteMessagesInstruction(DeleteMessagesInstruction deleteMessagesInstruction) {
        this.deleteMessagesInstruction = deleteMessagesInstruction;
    }

}
