package com.abnamro.nl.channels.geninfo.bankmail.jsons;

public class BOMailTemplate {
    public String displayName;
    public String fallbackStrategy;
    public String signature;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFallbackStrategy() {
        return fallbackStrategy;
    }

    public void setFallbackStrategy(String fallbackStrategy) {
        this.fallbackStrategy = fallbackStrategy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
