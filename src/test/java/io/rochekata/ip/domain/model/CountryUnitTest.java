package io.rochekata.ip.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountryUnitTest {

    @Test
    void isNorthern() {
        assertTrue(new Country("Co", "123.1.1.1", 1).isNorthern());
    }

    @Test
    void isNotNorthern() {
        assertFalse(new Country("Co", "123.1.1.1", -1).isNorthern());
    }

}
