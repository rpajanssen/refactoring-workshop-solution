package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import java.util.List;

public class FilteredBOs implements ItemContainer<String>{

    private List<String> items;

    public List<String> getItems() {
        return this.items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
