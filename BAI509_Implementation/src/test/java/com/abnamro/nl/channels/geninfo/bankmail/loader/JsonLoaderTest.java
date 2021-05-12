package com.abnamro.nl.channels.geninfo.bankmail.loader;

import com.abnamro.nl.channels.geninfo.bankmail.util.MailResources;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;

public class JsonLoaderTest {
    private final String EXPECTED_RESULT = "{\n" + "  \"items\": [\n" + "    {\n" + "      \"ccaRole\": \"12\",\n" + "      \"displayNamePrefix\": \"ABN AMRO Bank N.V.\",\n" + "      \"fallbackStrategy\": \"ASC\",\n" + "      \"signatureTemplate\": \"\\\\n\\\\ Met vriendelijke groet, \\\\n\\\\ ABN AMRO Bank N.V. \\\\n\\\\\\\\n\\\\ #$EmployeeName$#\\\\n\\\\\"\n" + "    }\n" + "  ]\n" + "}\n";

    @Test
    public void shouldLoadJson() throws UnableToReadFileException {
        String result = JsonLoader.loadJson(MailResources.CCA_PREFERRED_MAIL_TEMPLATE.getFileName());
        assertEquals(EXPECTED_RESULT, result);
    }

    @Test(expected = UnableToReadFileException.class)
    public void shouldThrowExceptionOnNonExistingFile() throws UnableToReadFileException {
        JsonLoader.loadJson("non-existing.json");
    }

    /**
     * A smart (not professional) developer can still create a static utility class, even if it has a private constructor.
     * The example code below shows you how they can do it. So it is always wise to throw an exception in the private
     * constructor to block the smart (not professional) developers. And of course... having a unit test to proof this
     * is cool!
     */
    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotBeAllowedToCreateAnInstance() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        final Constructor<JsonLoader> constructor = JsonLoader.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch(InvocationTargetException exception) {
            assertEquals(JsonLoader.ERROR_MESSAGE_CONSTRUCTOR, exception.getTargetException().getMessage());
            throw (UnsupportedOperationException)exception.getTargetException();
        }
    }
}
