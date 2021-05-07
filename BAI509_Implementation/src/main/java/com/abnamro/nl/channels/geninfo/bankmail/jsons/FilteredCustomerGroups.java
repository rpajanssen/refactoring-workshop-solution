package com.abnamro.nl.channels.geninfo.bankmail.jsons;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FilteredCustomerGroups {
    @JsonProperty("CGC")
    public List<String> cGC;

    public List<String> getcGC() {
        return cGC;
    }

    public void setcGC(List<String> cGC) {
        this.cGC = cGC;
    }
}
