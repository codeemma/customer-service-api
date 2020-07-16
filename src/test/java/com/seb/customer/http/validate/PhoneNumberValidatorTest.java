package com.seb.customer.http.validate;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhoneNumberValidatorTest {

    private PhoneNumberValidator phoneNumberValidator;

    @Before
    public void setUp() throws Exception {
        phoneNumberValidator = new PhoneNumberValidator();
    }

    @Test
    public void isValid_FalseForEmptyString() {
        String phoneNumber = "";

        assertFalse(phoneNumberValidator.isValid(phoneNumber, null));
    }

     @Test
    public void isValid_FalseForInvalidPhoneNumber() {
        String phoneNumber = "+37062011";

        assertFalse(phoneNumberValidator.isValid(phoneNumber, null));
    }

    @Test
    public void isValid_TrueForValidPhoneNumber() {
        String phoneNumber = "+37062011111";

        assertTrue(phoneNumberValidator.isValid(phoneNumber, null));
    }


}