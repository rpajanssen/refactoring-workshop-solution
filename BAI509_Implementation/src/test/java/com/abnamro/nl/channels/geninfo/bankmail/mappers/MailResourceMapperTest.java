package com.abnamro.nl.channels.geninfo.bankmail.mappers;

import com.abnamro.nl.channels.geninfo.bankmail.jsons.CCAMailboxTemplateJson;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.FilteredBOs;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MailResourceMapperTest {
    // content of ccamailboxtemplatepreferred.json
    private final String JSON_TEMPLATE_ONE = "{\n" + "         \"ccaRole\": \"12\",\n" + "         \"displayNamePrefix\": \"ABN AMRO Bank N.V.\",\n" + "         \"fallbackStrategy\": \"ASC\",\n" + "         \"signatureTemplate\": \"\\\\n\\\\ Met vriendelijke groet, \\\\n\\\\ ABN AMRO Bank N.V. \\\\n\\\\\\\\n\\\\ #$EmployeeName$#\\\\n\\\\\"\n" + "      }";
    // content of ccamailboxtemplateprivate.json
    private final String JSON_TEMPLATE_TWO = "{\n" + "         \"ccaRole\": \"33\",\n" + "         \"displayNamePrefix\": \"ABN AMRO MeesPierson\",\n" + "         \"fallbackStrategy\": \"ASC\",\n" + "         \"signatureTemplate\": \"\\\\n\\\\ Met vriendelijke groet, \\\\n\\\\ ABN AMRO MeesPierson \\\\n\\\\\\\\n\\\\ #$EmployeeName$#\\\\n\\\\\"\n" + "      }";
    // content of filteredbos.json
    private final String JSON_TEMPLATE_THREE = "{\n" + "      \"items\": [\n" + "         \"0\",\n" + "         \"4\",\n" + "         \"2200\",\n" + "         \"2201\",\n" + "         \"2202\",\n" + "         \"2203\",\n" + "         \"2204\",\n" + "         \"3300\",\n" + "         \"831656\",\n" + "         \"985600\",\n" + "         \"985601\",\n" + "         \"985602\",\n" + "         \"985603\",\n" + "         \"985604\",\n" + "         \"985605\",\n" + "         \"985606\",\n" + "         \"985607\",\n" + "         \"985608\",\n" + "         \"988900\",\n" + "         \"999999\"\n" + "      ]\n" + "}";

    private MailResourceMapper underTest;

    @Before
    public void setup() {
        underTest = new MailResourceMapper();
    }

    @Test
    public void shouldMapToACCAMail() throws JsonProcessingException {
        CCAMailboxTemplateJson result = underTest.map(JSON_TEMPLATE_ONE, CCAMailboxTemplateJson.class);
        assertEquals("12", result.getCcaRole());
        assertEquals("ABN AMRO Bank N.V.", result.getDisplayNamePrefix());
    }

    @Test
    public void shouldMapToPrivateACCAMail() throws JsonProcessingException {
        CCAMailboxTemplateJson result = underTest.map(JSON_TEMPLATE_TWO, CCAMailboxTemplateJson.class);
        assertEquals("33", result.getCcaRole());
        assertEquals("ABN AMRO MeesPierson", result.getDisplayNamePrefix());
    }

    @Test
    public void shouldMapToFilteredBOs() throws JsonProcessingException {
        FilteredBOs result = underTest.map(JSON_TEMPLATE_THREE, FilteredBOs.class);
        assertEquals(20, result.getItems().size());
        assertEquals("985606", result.getItems().get(15));
    }
}
