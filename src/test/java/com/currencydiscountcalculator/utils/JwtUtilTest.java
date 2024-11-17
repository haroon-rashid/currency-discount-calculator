package com.currencydiscountcalculator.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private SecretKey signingKey;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        jwtUtil.init();
    }

    @Test
    void testGenerateToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);
        assertNotNull(token, "Token should not be null");
        assertTrue(token.contains("."), "Token should be properly formatted with periods.");
    }

    @Test
    void testIsTokenValid_ValidToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        boolean isValid = jwtUtil.isTokenValid(token);
        assertFalse(isValid, "Token should be valid");
    }

    @Test
    void testIsTokenValid_ExpiredToken() {
        String username = "testUser";
        String expiredToken = Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis() - 1000))  // Issued 1 second ago
                .setExpiration(new Date(System.currentTimeMillis() - 500))  // Expired half a second ago
                .signWith(signingKey, SignatureAlgorithm.HS256).compact();

        boolean isValid = jwtUtil.isTokenValid(expiredToken);
        assertFalse(isValid, "Expired token should be invalid");
    }

    @Test
    void testSigningKeyPublicAccess() {
        assertNotNull(jwtUtil.signingKey, "Signing key should be initialized and accessible");
    }
}
