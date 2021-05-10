package com.abnamro.nl.channels.geninfo.bankmail.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonLoader {
    private JsonLoader() {
        throw new UnsupportedOperationException();
    }

    public static String loadJson(String fileName) {
        StringBuilder textBuilder = new StringBuilder();

        try (InputStream resourceAsStream = JsonLoader.class.getClassLoader().getResourceAsStream(fileName);
             InputStreamReader streamReader = new InputStreamReader(resourceAsStream);
             BufferedReader bufferedReader = new BufferedReader(streamReader)) {
            int c = 0;
            while ((c = bufferedReader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException ioException) {
            // todo : handle exception
        }

        return textBuilder.toString();
    }
}
