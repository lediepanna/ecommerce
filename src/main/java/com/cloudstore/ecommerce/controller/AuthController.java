package com.cloudstore.ecommerce.controller;

import com.cloudstore.ecommerce.dto.RegisterRequest;
import com.cloudstore.ecommerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(RegisterRequest request, RedirectAttributes redirectAttributes) {
        try {
            if (userService.existsByUsername(request.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "Användarnamnet är upptaget.");
                return "redirect:/register";
            }

            userService.registerUser(request);
            redirectAttributes.addFlashAttribute("message", "Registrering lyckades! Logga in nu.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registrering misslyckades: " + e.getMessage());
            return "redirect:/register";
        }
    }
}