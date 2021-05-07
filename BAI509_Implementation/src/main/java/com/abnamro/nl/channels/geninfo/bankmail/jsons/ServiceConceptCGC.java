package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceConceptCGC {
    @JsonProperty("CGC")
    public String cGC;
    public String slaMessageKey;
    public String canSelectSubject;
    public String destinationMailboxTemplate;

    public String getcGC() {
        return cGC;
    }

    public void setcGC(String cGC) {
        this.cGC = cGC;
    }

    public String getSlaMessageKey() {
        return slaMessageKey;
    }

    public void setSlaMessageKey(String slaMessageKey) {
        this.slaMessageKey = slaMessageKey;
    }

    public String getCanSelectSubject() {
        return canSelectSubject;
    }

    public void setCanSelectSubject(String canSelectSubject) {
        this.canSelectSubject = canSelectSubject;
    }

    public String getDestinationMailboxTemplate() {
        return destinationMailboxTemplate;
    }

    public void setDestinationMailboxTemplate(String destinationMailboxTemplate) {
        this.destinationMailboxTemplate = destinationMailboxTemplate;
    }
}
