package com.abnamro.nl.channels.geninfo.bankmail.mappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class MailResourceMapper {
    private final ObjectMapper mapper;

    public MailResourceMapper() {
        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public <T> T map(String json, Class type) throws JsonProcessingException {
        return (T)mapper.readValue(json, type);
    }
}
