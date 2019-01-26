package org.lightquark.parallelfileprocessor;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.lightquark.parallelfileprocessor.utils.CardNumberUtils.generate;
import static org.lightquark.parallelfileprocessor.utils.CardNumberUtils.validate;

public class CardNumberUtilsTest {

    @Test
    public void testValidate() {
        assertTrue(validate("5457623898234113"));
        assertTrue(validate("4405885818715291"));

        assertFalse(validate("5457623898234115"));
        assertFalse(validate("5457"));
        assertFalse(validate("4405885818715281"));
        assertFalse(validate("1234567890123456"));
        assertFalse(validate("1234"));
    }

    @Test
    public void testGenerate() {
        assertTrue(validate(generate("5457", 16)));
        assertTrue(validate(generate("4405", 16)));
    }
}
