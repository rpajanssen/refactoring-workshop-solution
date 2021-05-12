package com.abnamro.nl.channels.geninfo.bankmail.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Simple file loader that reads a file and returns the content as a string. If this functionality needs to be used
 * in other locations in the code as well... this file can be refactored to have a more generic name and then
 * it can be re-used because there is nothing json specific about it.
 */
public class JsonLoader {
    static final String ERROR_MESSAGE_CONSTRUCTOR = "not allowed to instantiate the json-loader utility class";

    /**
     * Private constructor throws an exception to prevent smart (not professional) developers from constructing an
     * instance anyway. See the unit test for an example!
     */
    private JsonLoader() {
        throw new UnsupportedOperationException(ERROR_MESSAGE_CONSTRUCTOR);
    }

    public static String loadJson(String fileName) throws UnableToReadFileException {
        StringBuilder textBuilder = new StringBuilder();

        try (InputStream resourceAsStream = JsonLoader.class.getClassLoader().getResourceAsStream(fileName);
             InputStreamReader streamReader = new InputStreamReader(resourceAsStream);
             BufferedReader bufferedReader = new BufferedReader(streamReader)) {
            int character;
            while ((character = bufferedReader.read()) != -1) {
                textBuilder.append((char) character);
            }
        } catch (IOException | NullPointerException exception) {
            throw new UnableToReadFileException(exception.getMessage(), exception);
        }

        return textBuilder.toString();
    }
}
