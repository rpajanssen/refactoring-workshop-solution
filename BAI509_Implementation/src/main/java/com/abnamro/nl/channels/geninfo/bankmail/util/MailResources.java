package com.abnamro.nl.channels.geninfo.bankmail.util;

import com.abnamro.nl.channels.geninfo.bankmail.jsons.*;

/**
 * This enum binds the json resource files to the classes they represent and the cache key that will be used.
 *
 * todo : is CCA_PREFERRED_MAIL_TEMPLATE used?
 */
public enum MailResources {
    FILTERED_BOS(FilteredBOs.class, "filteredbos.json", "filteredbos"),
    FILTERED_CUSTOMER_GROUPS(FilteredBOs.class, "filteredcustomergroups.json", "filteredcustomergroups"),

    SERVICE_CONCEPT_BY_CGC(ServiceConceptByCGCList.class, "serviceconceptbycgc.json", "serviceconceptbycgc"),
    SERVICE_CONCEPT_BY_SEGMENT(ServiceConceptList.class, "serviceconceptbysegment.json", "serviceconceptbysegment"),

    CCA_PRIVATE_MAIL_TEMPLATE(CCAMailboxTemplateList.class, "ccamailboxtemplateprivate.json", "ccamailboxtemplateprivate"),
    CCA_PREFERRED_MAIL_TEMPLATE(CCAMailboxTemplateList.class, "ccamailboxtemplatepreferred.json", "ccamailboxtemplatepreferred"),
    BO_MAIL_TEMPLATE(BOMailboxTemplateList.class, "bomailboxtemplate.json", "bomailboxtemplate"),
    GENESYS_YBB_MAIL_TEMPLATE(GenesysMailboxTemplateList.class, "genesysmailboxtemplateybb.json", "genesysmailboxtemplateybb"),
    GENESYS_ASC_MAIL_TEMPLATE(GenesysMailboxTemplateList.class, "genesysmailboxtemplateasc.json", "genesysmailboxtemplateasc")
    ;

    Class type;
    String fileName;
    String cacheKey;

    MailResources(Class type, String fileName, String cacheKey) {
        this.type = type;
        this.fileName = fileName;
        this.cacheKey = cacheKey;
    }

    public Class getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
