package com.cloudstore.ecommerce.controller;

import com.cloudstore.ecommerce.dto.RegisterRequest;
import com.cloudstore.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterRequest request, Model model) {
        boolean success = userService.registerUser(request);
        if (!success) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}