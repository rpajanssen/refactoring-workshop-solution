package com.abnamro.nl.channels.geninfo.bankmail.loader;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonLoaderTest {
    private final String EXPECTED_RESULT = "{\n" + "  \"items\": [\n" + "    {\n" + "      \"ccaRole\": \"12\",\n" + "      \"displayNamePrefix\": \"ABN AMRO Bank N.V.\",\n" + "      \"fallbackStrategy\": \"ASC\",\n" + "      \"signatureTemplate\": \"\\\\n\\\\ Met vriendelijke groet, \\\\n\\\\ ABN AMRO Bank N.V. \\\\n\\\\\\\\n\\\\ #$EmployeeName$#\\\\n\\\\\"\n" + "    }\n" + "  ]\n" + "}\n";

    @Test
    public void shouldLoadJson() {
        String result = JsonLoader.loadJson("ccamailboxtemplatepreferred.json");
        assertEquals(EXPECTED_RESULT, result);
    }

    // todo : implement exception handling in class under test and then enable this test
    @Ignore
    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionOnInvalidFileName() {
        JsonLoader.loadJson("non-existing.json");
    }
}
