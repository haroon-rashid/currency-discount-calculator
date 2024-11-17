package com.currencydiscountcalculator.service;

import com.currencydiscountcalculator.model.User;
import com.currencydiscountcalculator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserName("testuser");
        user.setPassword("password");
        user.setRoles("USER,ADMIN");
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertArrayEquals(new String[]{"ROLE_ADMIN", "ROLE_USER"}, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));
        verify(userRepository, times(1)).findByUserName("testuser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUserName("nonexistentuser")).thenReturn(Optional.empty());
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistentuser");
        });

        assertEquals("User not found: nonexistentuser", exception.getMessage());
        verify(userRepository, times(1)).findByUserName("nonexistentuser");
    }
}
