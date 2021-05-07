package com.abnamro.nl.channels.geninfo.bankmail.jsons;

public class CCAMailboxTemplateJson {
    public String ccaRole;
    public String displayNamePrefix;
    public String fallbackStrategy;
    public String signatureTemplate;

    public String getCcaRole() {
        return ccaRole;
    }

    public void setCcaRole(String ccaRole) {
        this.ccaRole = ccaRole;
    }

    public String getDisplayNamePrefix() {
        return displayNamePrefix;
    }

    public void setDisplayNamePrefix(String displayNamePrefix) {
        this.displayNamePrefix = displayNamePrefix;
    }

    public String getFallbackStrategy() {
        return fallbackStrategy;
    }

    public void setFallbackStrategy(String fallbackStrategy) {
        this.fallbackStrategy = fallbackStrategy;
    }

    public String getSignatureTemplate() {
        return signatureTemplate;
    }

    public void setSignatureTemplate(String signatureTemplate) {
        this.signatureTemplate = signatureTemplate;
    }
}
