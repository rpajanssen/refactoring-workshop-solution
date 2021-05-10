package com.abnamro.nl.channels.geninfo.bankmail.util;

import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants.FILTERED_BOS;
import static com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces.BankmailConstants.FILTERED_CUSTOMER_GROUPS;
import static org.junit.Assert.assertEquals;

// todo : increase coverage
public class BankmailResourceDataUtilTest {

    private BankmailResourceDataUtil underTest;

    @Before
    public void setup() throws BankmailApplicationException {
        underTest = new BankmailResourceDataUtil();
    }

    // todo : maybe further cleanup : note that deserialization didd not work because of code naming violations, we had
    //        to refactor the data object
    //
    // todo : what is a BO? rename the data class and the property so no one has to guess anymore
    @Test
    public void shouldReturnFilteredBOs() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<String> result = (List<String>)underTest.getData(FILTERED_BOS);
        assertEquals(10189, result.size());
        assertEquals("323429", result.get(3672));
    }

    @Test
    public void shouldReturnCustomerGroups() {
        // todo : refactor the class under test so we do not have to cast anymore
        List<String> result = (List<String>)underTest.getData(FILTERED_CUSTOMER_GROUPS);
        assertEquals(216, result.size());
        assertEquals("0449", result.get(150));
    }
}
