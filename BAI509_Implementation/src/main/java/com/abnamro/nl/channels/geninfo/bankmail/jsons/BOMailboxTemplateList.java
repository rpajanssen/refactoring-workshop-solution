package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BOMailboxTemplateList {

    @JsonProperty("BOMailTemplate")
    public List<BOMailTemplate> bOMailTemplateJson;

    public List<BOMailTemplate> getbOMailTemplateJson() {
        return bOMailTemplateJson;
    }

    public void setbOMailTemplateJson(List<BOMailTemplate> bOMailTemplateJson) {
        this.bOMailTemplateJson = bOMailTemplateJson;
    }


}
