package com.abnamro.nl.channels.geninfo.bankmail.util;

public enum BankmailResourceDataEnumKeys {


    FILTEREDBOS("filteredbos"),
    BO("BO"),
    CCAMAILBOXTEMPLATE("CCAMailboxTemplate"),
    CCAMAILBOXTEMPLATEPRIVATE("ccamailboxtemplateprivate"),
    BOMAILTEMPLATE("BOMailTemplate"),
    BOMAILBOXTEMPLATE("bomailboxtemplate"),
    GENESYSMAILBOXTEMPLATE("GenesysMailboxTemplate"),
    GENESYSMAILBOXTEMPLATEYBB("genesysmailboxtemplateybb"),
    CCAMAILBOXTEMPALTE("CCAMailboxTemplate"),
    CCAMAILBOXTEMPLATEPREFERRED("ccamailboxtemplatepreferred"),
    CGC("CGC"),
    FILTEREDCUSTOMERGROUPS("filteredcustomergroups"),
    GENESYSMAILBOXTEMPLATEASC("genesysmailboxtemplateasc"),
    SERVICECONCEPT("ServiceConcept"),
    SERVICECONCEPTBYCGC("serviceconceptbycgc"),
    SERVICECONCEPTBYSEGMENT("serviceconceptbysegment");

    private final String dataKey;

    /**
     * Constructor to assign the field value of the Key enum values.
     * @param dataKey Enum to assign field value to
     */
    BankmailResourceDataEnumKeys(String dataKey) {
        this.dataKey = dataKey;
    }

    /**
     * Returns the log key.
     * @return String
     */
    public String getLogKey() {
        return this.dataKey;
    }
}