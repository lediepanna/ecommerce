// AuthControllerTest.java
package com.cloudstore.ecommerce.controller;

import com.cloudstore.ecommerce.dto.RegisterRequest;
import com.cloudstore.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
    }

    @Test
    void login_shouldReturnLoginView() {
        String view = authController.login();
        assertEquals("login", view);
    }

    @Test
    void registerForm_shouldReturnRegisterView() {
        String view = authController.registerForm(model);
        assertEquals("register", view);
        verify(model, times(1)).addAttribute(eq("registerRequest"), any(RegisterRequest.class));
    }

    @Test
    void register_success_shouldRedirectToLogin() {
        when(userService.existsByUsername("testuser")).thenReturn(false);

        String result = authController.register(registerRequest, redirectAttributes);

        assertEquals("redirect:/login", result);
        verify(userService, times(1)).registerUser(registerRequest);
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "Registrering lyckades! Logga in nu.");
    }

    @Test
    void register_usernameTaken_shouldRedirectToRegister() {
        when(userService.existsByUsername("testuser")).thenReturn(true);

        String result = authController.register(registerRequest, redirectAttributes);

        assertEquals("redirect:/register", result);
        verify(userService, never()).registerUser(any());
        verify(redirectAttributes, times(1)).addFlashAttribute("error", "Användarnamnet är upptaget.");
    }

    @Test
    void register_exception_shouldRedirectToRegisterWithError() {
        when(userService.existsByUsername("testuser")).thenReturn(false);
        doThrow(new RuntimeException("Database error")).when(userService).registerUser(any());

        String result = authController.register(registerRequest, redirectAttributes);

        assertEquals("redirect:/register", result);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("error"), anyString());
    }
}