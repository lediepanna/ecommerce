package com.cloudstore.ecommerce;

import com.cloudstore.ecommerce.dto.RegisterRequest;
import com.cloudstore.ecommerce.model.User;
import com.cloudstore.ecommerce.repository.UserRepository;
import com.cloudstore.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUserSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("new@example.com");
        request.setPassword("password");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        boolean result = userService.registerUser(request);
        assertTrue(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUserDuplicate() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing");
        when(userRepository.existsByUsername("existing")).thenReturn(true);

        boolean result = userService.registerUser(request);
        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testFindByUsernameFound() {
        User user = new User();
        user.setUsername("found");
        when(userRepository.findByUsername("found")).thenReturn(Optional.of(user));

        User found = userService.findByUsername("found");
        assertNotNull(found);
        assertEquals("found", found.getUsername());
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.findByUsername("missing"));
    }
}