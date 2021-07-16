package com.abnamro.nl.channels.geninfo.bankmail.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SendMessageInstructionWrapper {

    /**
     * wrapper for SendMessageInstruction
     */
    private SendMessageInstruction sendMessageInstruction;

    /**
     * @return SendMessageInstruction
     */
    public SendMessageInstruction getSendMessageInstruction() {
        return sendMessageInstruction;
    }

    /**
     * @param sendMessageInstruction SendMessageInstruction
     */
    public void setSendMessageInstruction(SendMessageInstruction sendMessageInstruction) {
        this.sendMessageInstruction = sendMessageInstruction;
    }

}
