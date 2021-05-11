package com.abnamro.nl.channels.geninfo.bankmail.util;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BankMailResourceProviderTest {

    private BankMailResourceProvider underTest;

    @Before
    public void setup() throws BankmailApplicationException {
        underTest = new BankMailResourceProvider();
    }

    // todo : what is a BO? rename the data class and the property so no one has to guess anymore
    @Test
    public void shouldReturnFilteredBOs() {
        List<String> result = underTest.getData(MailResources.FILTERED_BOS);
        assertEquals(10189, result.size());
        assertEquals("323429", result.get(3672));
    }

    @Test
    public void shouldReturnCustomerGroups() {
        List<String> result = underTest.getData(MailResources.FILTERED_CUSTOMER_GROUPS);
        assertEquals(216, result.size());
        assertEquals("0449", result.get(150));
    }

    @Test
    public void shouldReturnCCAPrivateMailTemplate() {
        List<CCAMailboxTemplateJson> result = underTest.getData(MailResources.CCA_PRIVATE_MAIL_TEMPLATE);
        assertEquals(1, result.size());
        assertEquals("33", result.get(0).getCcaRole());
        assertEquals("ABN AMRO MeesPierson", result.get(0).getDisplayNamePrefix());
    }

    // todo : what does "BO" mean, and refactor the names so everyone understands is
    @Test
    public void shouldReturnBOMailtemplate() {
        List<BOMailTemplate> result = underTest.getData(MailResources.BO_MAIL_TEMPLATE);
        assertEquals(1, result.size());
        assertEquals("ABN AMRO Client Services", result.get(0).getDisplayName());
        assertEquals("YBB", result.get(0).getFallbackStrategy());
    }

    @Test
    public void shouldReturnGenesysMailTemplate() {
        List<GenesysMailboxTemplateJson> result =underTest.getData(MailResources.GENESYS_YBB_MAIL_TEMPLATE);
        assertEquals(1, result.size());
        assertEquals("ABN AMRO Kleinbedrijf", result.get(0).getDisplayName());
    }

    @Test
    public void shouldReturnCCAPreferredMailTemplate() {
        List<CCAMailboxTemplateJson> result = underTest.getData(MailResources.CCA_PREFERRED_MAIL_TEMPLATE);
        assertEquals(1, result.size());
        assertEquals("12", result.get(0).getCcaRole());
        assertEquals("ABN AMRO Bank N.V.", result.get(0).getDisplayNamePrefix());
    }

    @Test
    public void shouldReturnGenesysAscMailTemplate() {
        List<GenesysMailboxTemplateJson> result = underTest.getData(MailResources.GENESYS_ASC_MAIL_TEMPLATE);
        assertEquals(1, result.size());
        assertEquals("ABN AMRO Contact Center", result.get(0).getDisplayName());
    }

    @Test
    public void shouldReturnServiceConceptByCGC() {
        List<ServiceConceptCGC> result = underTest.getData(MailResources.SERVICE_CONCEPT_BY_CGC);
        assertEquals(179, result.size());
        assertEquals("0101", result.get(0).getcGC());
    }

    @Test
    public void shouldReturnServiceConceptBySegment() {
        List<ServiceConcept> result = underTest.getData(MailResources.SERVICE_CONCEPT_BY_SEGMENT);
        assertEquals(6, result.size());
        assertEquals("4000", result.get(0).getSegment());
    }
}
