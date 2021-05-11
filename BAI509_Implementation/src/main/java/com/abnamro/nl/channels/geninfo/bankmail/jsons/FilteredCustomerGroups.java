package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import java.util.List;

public class FilteredCustomerGroups implements ItemContainer<String>{
    public List<String> items;

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
