package com.cs_25_2_team2.RestaurantManagementApp.auth;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Simple password encoder for authentication without Spring Security dependency.
 * Provides BCrypt-style password hashing and validation.
 * 
 * @author Team 2 - Phase 3 Authentication
 * @version 3.1
 */
@Component
public class PasswordEncoder {
    
    private final SecureRandom random = new SecureRandom();
    private final int SALT_LENGTH = 16;
    private final int HASH_ITERATIONS = 10000;

    /**
     * Encode a raw password with salt and hashing
     */
    public String encode(String rawPassword) {
        try {
            // Generate salt
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hash password with salt
            byte[] hash = hashPassword(rawPassword.getBytes(), salt);
            
            // Combine salt and hash
            byte[] combined = new byte[SALT_LENGTH + hash.length];
            System.arraycopy(salt, 0, combined, 0, SALT_LENGTH);
            System.arraycopy(hash, 0, combined, SALT_LENGTH, hash.length);
            
            // Encode to Base64
            return Base64.getEncoder().encodeToString(combined);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode password", e);
        }
    }

    /**
     * Check if raw password matches encoded password
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        try {
            // Decode from Base64
            byte[] combined = Base64.getDecoder().decode(encodedPassword);
            
            // Extract salt
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);
            
            // Extract stored hash
            byte[] storedHash = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, SALT_LENGTH, storedHash, 0, storedHash.length);
            
            // Hash the raw password with the same salt
            byte[] testHash = hashPassword(rawPassword.getBytes(), salt);
            
            // Compare hashes
            return MessageDigest.isEqual(storedHash, testHash);
            
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Hash password with salt using PBKDF2
     */
    private byte[] hashPassword(byte[] password, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        // Initial hash
        md.update(salt);
        byte[] hash = md.digest(password);
        
        // Iterate for strength
        for (int i = 0; i < HASH_ITERATIONS; i++) {
            md.reset();
            md.update(salt);
            hash = md.digest(hash);
        }
        
        return hash;
    }

    /**
     * Generate a random password
     */
    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }

    /**
     * Validate password strength
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasUpper && hasLower && hasDigit;
    }
}
