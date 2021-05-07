package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ServiceConceptSegmentList {


    @JsonProperty("ServiceConcept")
    public List<ServiceConcept> serviceConcept;

    public List<ServiceConcept> getServiceConcept() {
        return serviceConcept;
    }

    public void setServiceConcept(List<ServiceConcept> serviceConcept) {
        this.serviceConcept = serviceConcept;
    }


}
