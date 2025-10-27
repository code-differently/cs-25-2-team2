package com.cs_25_2_team2.RestaurantManagementApp.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new PasswordEncoder();
    }

    @Test
    void testEncodeAndMatches() {
        String rawPassword = "TestPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "Encoded password should match the raw password");
    }

    @Test
    void testGenerateRandomPassword() {
        String randomPassword = passwordEncoder.generateRandomPassword(12);
        assertNotNull(randomPassword, "Generated password should not be null");
        assertEquals(12, randomPassword.length(), "Generated password should have the specified length");
    }

    @Test
    void testIsValidPassword() {
        assertTrue(passwordEncoder.isValidPassword("Valid123"), "Password should be valid");
        assertFalse(passwordEncoder.isValidPassword("short"), "Password should be invalid if too short");
        assertFalse(passwordEncoder.isValidPassword("nouppercase1"), "Password should be invalid if missing uppercase letters");
        assertFalse(passwordEncoder.isValidPassword("NOLOWERCASE1"), "Password should be invalid if missing lowercase letters");
        assertFalse(passwordEncoder.isValidPassword("NoDigits"), "Password should be invalid if missing digits");
    }
}