package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import java.util.List;

public class ServiceConceptByCGCList implements ItemContainer<ServiceConceptCGC> {
    public List<ServiceConceptCGC> items;

    public List<ServiceConceptCGC> getItems() {
        return items;
    }

    public void setItems(List<ServiceConceptCGC> items) {
        this.items = items;
    }
}
