package com.currencydiscountcalculator.service;

import com.currencydiscountcalculator.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, customUserDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        String validToken = "valid.jwt.token";
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtUtil.isTokenValid(validToken)).thenReturn(true);
        when(jwtUtil.extractUsername(validToken)).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(null);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(jwtUtil).isTokenValid(validToken);
        verify(jwtUtil).extractUsername(validToken);
        verify(customUserDetailsService).loadUserByUsername(username);
        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertEquals(userDetails, authentication.getPrincipal());
    }

    @Test
    void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
        String invalidToken = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        when(jwtUtil.isTokenValid(invalidToken)).thenReturn(false);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(jwtUtil).isTokenValid(invalidToken);
        verifyNoInteractions(customUserDetailsService);
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternalWithNoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verifyNoInteractions(jwtUtil);
        verifyNoInteractions(customUserDetailsService);
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testGetJwtFromRequestWithBearerToken() {
        String bearerToken = "Bearer valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        String token = jwtAuthenticationFilter.getJwtFromRequest(request);
        assertEquals("valid.jwt.token", token);
    }

    @Test
    void testGetJwtFromRequestWithoutBearerToken() {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");
        String token = jwtAuthenticationFilter.getJwtFromRequest(request);
        assertNull(token);
    }

    @Test
    void testGetJwtFromRequestWithNoAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);
        String token = jwtAuthenticationFilter.getJwtFromRequest(request);
        assertNull(token);
    }
}

