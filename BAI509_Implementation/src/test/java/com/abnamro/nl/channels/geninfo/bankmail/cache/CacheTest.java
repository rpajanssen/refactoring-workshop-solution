package com.abnamro.nl.channels.geninfo.bankmail.cache;

import com.abnamro.nl.channels.geninfo.bankmail.jsons.CCAMailboxTemplateJson;
import com.abnamro.nl.channels.geninfo.bankmail.jsons.GenesysMailboxTemplateJson;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CacheTest {
    private Cache underTest;

    @Before
    public void setup() {
        underTest = new Cache();
    }

    @Test
    public void shouldCacheAndReturnDataOfDifferentClasses() {
        CCAMailboxTemplateJson firstObject = new CCAMailboxTemplateJson();
        firstObject.setCcaRole("role");
        GenesysMailboxTemplateJson secondObject = new GenesysMailboxTemplateJson();
        secondObject.setDisplayName("name two");

        underTest.put("key1", firstObject);
        underTest.put("key2", secondObject);

        CCAMailboxTemplateJson resultOne = underTest.get("key1");
        GenesysMailboxTemplateJson resultTwo = underTest.get("key2");

        assertEquals(firstObject.getCcaRole(), resultOne.getCcaRole());
        assertEquals(secondObject.getDisplayName(), resultTwo.getDisplayName());
    }
}
