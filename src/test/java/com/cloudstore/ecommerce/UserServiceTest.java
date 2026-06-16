// UserServiceTest.java (uppdaterad)
package com.cloudstore.ecommerce.service;

import com.cloudstore.ecommerce.dto.RegisterRequest;
import com.cloudstore.ecommerce.model.User;
import com.cloudstore.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole("USER");
    }

    @Test
    void registerUser_shouldSaveUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registerUser(registerRequest);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_usernameAlreadyExists_shouldThrowException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> userService.registerUser(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByUsername_userExists_shouldReturnUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    void findByUsername_userNotFound_shouldThrowException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findByUsername("unknown"));
    }

    @Test
    void existsByUsername_shouldReturnTrueIfExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean exists = userService.existsByUsername("testuser");
        assertTrue(exists);
    }

    @Test
    void existsByUsername_shouldReturnFalseIfNotExists() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        boolean exists = userService.existsByUsername("unknown");
        assertFalse(exists);
    }

    @Test
    void findUserByUsername_shouldReturnOptional() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> optionalUser = userService.findUserByUsername("testuser");
        assertTrue(optionalUser.isPresent());
        assertEquals("testuser", optionalUser.get().getUsername());
    }
}