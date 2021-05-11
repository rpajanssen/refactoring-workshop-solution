package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import java.util.List;

public class ServiceConceptList implements ItemContainer<ServiceConcept>{
    public List<ServiceConcept> items;

    public List<ServiceConcept> getItems() {
        return items;
    }

    public void setItems(List<ServiceConcept> items) {
        this.items = items;
    }
}
