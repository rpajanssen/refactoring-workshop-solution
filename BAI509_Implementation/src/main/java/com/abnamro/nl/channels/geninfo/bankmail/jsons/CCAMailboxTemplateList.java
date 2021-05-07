package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CCAMailboxTemplateList {
    @JsonProperty("CCAMailboxTemplate")
    public List<CCAMailboxTemplateJson> CCAMailboxTemplateJson;

    public List<CCAMailboxTemplateJson> getCCAMailboxTemplateJson() {
        return CCAMailboxTemplateJson;
    }

    public void setCCAMailboxTemplateJson(List<com.abnamro.nl.channels.geninfo.bankmail.jsons.CCAMailboxTemplateJson> CCAMailboxTemplateJson) {
        this.CCAMailboxTemplateJson = CCAMailboxTemplateJson;
    }
}
