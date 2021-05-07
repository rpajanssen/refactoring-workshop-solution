package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ServiceConceptByCGCList {
    @JsonProperty("ServiceConceptCGC")
    public List<ServiceConceptCGC> serviceConceptCGCJson;

    public List<ServiceConceptCGC> getServiceConceptCGCJson() {
        return serviceConceptCGCJson;
    }

    public void setServiceConceptCGCJson(List<ServiceConceptCGC> serviceConceptCGCJsonList) {
        this.serviceConceptCGCJson = serviceConceptCGCJsonList;
    }
}
