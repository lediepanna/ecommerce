package com.cloudstore.ecommerce;

import com.cloudstore.ecommerce.dto.RegisterRequest;
import com.cloudstore.ecommerce.repository.UserRepository;
import com.cloudstore.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Test
    void testRegisterUserSuccess() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        RegisterRequest req = new RegisterRequest();
        req.setUsername("testuser");
        req.setPassword("pass");
        req.setEmail("test@example.com");
        assertTrue(userService.registerUser(req));
        verify(userRepository, times(1)).save(any());
    }
}