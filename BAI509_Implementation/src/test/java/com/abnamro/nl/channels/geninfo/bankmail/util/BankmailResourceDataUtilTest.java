package com.abnamro.nl.channels.geninfo.bankmail.util;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class BankmailResourceDataUtilTest {

    private BankmailResourceDataUtil underTest;

    @Before
    public void setup() throws BankmailApplicationException {
        underTest = new BankmailResourceDataUtil();
    }

    // todo : maybe further cleanup : note that deserialization did not work because of code naming violations, we had
    //        to refactor the data object
    //
    // todo : what is a BO? rename the data class and the property so no one has to guess anymore
    @Test
    public void shouldReturnFilteredBOs() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<String> result = (List<String>)underTest.getData(MailResources.FILTERED_BOS.getCacheKey());
        assertEquals(10189, result.size());
        assertEquals("323429", result.get(3672));
    }

    @Test
    public void shouldReturnCustomerGroups() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<String> result = (List<String>)underTest.getData(MailResources.FILTERED_CUSTOMER_GROUPS.getCacheKey());
        assertEquals(216, result.size());
        assertEquals("0449", result.get(150));
    }

    @Test
    public void shouldReturnCCAPrivateMailTemplate() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<CCAMailboxTemplateJson> result = (List<CCAMailboxTemplateJson>)underTest.getData(MailResources.CCA_PRIVATE_MAIL_TEMPLATE.getCacheKey());
        assertEquals(1, result.size());
        assertEquals("33", result.get(0).getCcaRole());
        assertEquals("ABN AMRO MeesPierson", result.get(0).getDisplayNamePrefix());
    }

    // todo : what does "BO" mean, and refactor the names so everyone understands is
    @Test
    public void shouldReturnBOMailtemplate() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<BOMailTemplate> result = (List<BOMailTemplate>)underTest.getData(MailResources.BO_MAIL_TEMPLATE.getCacheKey())
                ;
        assertEquals(1, result.size());
        assertEquals("ABN AMRO Client Services", result.get(0).getDisplayName());
        assertEquals("YBB", result.get(0).getFallbackStrategy());
    }

    @Test
    public void shouldReturnGenesysMailTemplate() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<GenesysMailboxTemplateJson> result = (List<GenesysMailboxTemplateJson>)underTest.getData(MailResources.GENESYS_YBB_MAIL_TEMPLATE.getCacheKey());
        assertEquals(1, result.size());
        assertEquals("ABN AMRO Kleinbedrijf", result.get(0).getDisplayName());
    }

    @Test
    public void shouldReturnCCAPreferredMailTemplate() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<CCAMailboxTemplateJson> result = (List<CCAMailboxTemplateJson>)underTest.getData(MailResources.CCA_PREFERRED_MAIL_TEMPLATE.getCacheKey());
        assertEquals(1, result.size());
        assertEquals("12", result.get(0).getCcaRole());
        assertEquals("ABN AMRO Bank N.V.", result.get(0).getDisplayNamePrefix());
    }

    @Test
    public void shouldReturnGenesysAscMailTemplate() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<GenesysMailboxTemplateJson> result = (List<GenesysMailboxTemplateJson>)underTest.getData(MailResources.GENESYS_ASC_MAIL_TEMPLATE.getCacheKey());
        assertEquals(1, result.size());
        assertEquals("ABN AMRO Contact Center", result.get(0).getDisplayName());
    }

    @Test
    public void shouldReturnServiceConceptByCGC() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<ServiceConceptCGC> result = (List<ServiceConceptCGC>)underTest.getData(MailResources.SERVICE_CONCEPT_BY_CGC.getCacheKey());
        assertEquals(179, result.size());
        assertEquals("0101", result.get(0).getcGC());
    }

    @Test
    public void shouldReturnServiceConceptBySegment() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<ServiceConcept> result = (List<ServiceConcept>)underTest.getData(MailResources.SERVICE_CONCEPT_BY_SEGMENT.getCacheKey());
        assertEquals(6, result.size());
        assertEquals("4000", result.get(0).getSegment());
    }
}
