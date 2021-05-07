package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GenesysMailboxTemplateList {
    @JsonProperty("GenesysMailboxTemplate")
    public List<GenesysMailboxTemplateJson> GenesysMailboxTemplateJson;

    public List<GenesysMailboxTemplateJson> getGenesysMailboxTemplateJson() {
        return GenesysMailboxTemplateJson;
    }

    public void setGenesysMailboxTemplateJson(List<GenesysMailboxTemplateJson> genesysMailboxTemplateJson) {
        GenesysMailboxTemplateJson = genesysMailboxTemplateJson;
    }
}
