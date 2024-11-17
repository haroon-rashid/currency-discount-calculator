package com.currencydiscountcalculator.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantsTest {

    @Test
    void testGroceryConstant() {
        assertEquals("Grocery", Constants.GROCERY, "Grocery constant should have the value 'Grocery'");
    }

    @Test
    void testUsdConstant() {
        assertEquals("USD", Constants.USD, "USD constant should have the value 'USD'");
    }

    @Test
    void testSuccessConstant() {
        assertEquals("success", Constants.SUCCESS, "SUCCESS constant should have the value 'success'");
    }
}

