package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import java.util.List;

public class CCAMailboxTemplateList implements ItemContainer<CCAMailboxTemplateJson>{
    public List<CCAMailboxTemplateJson> items;

    public List<CCAMailboxTemplateJson> getItems() {
        return items;
    }

    public void setItems(List<CCAMailboxTemplateJson> items) {
        this.items = items;
    }
}
