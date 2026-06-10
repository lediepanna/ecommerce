package com.cloudstore.ecommerce.controller;

import com.cloudstore.ecommerce.model.Order;
import com.cloudstore.ecommerce.model.User;
import com.cloudstore.ecommerce.repository.OrderRepository;
import com.cloudstore.ecommerce.service.OrderService;
import com.cloudstore.ecommerce.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderController(OrderService orderService,
                           OrderRepository orderRepository,
                           UserService userService) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @GetMapping("/checkout")
    public String checkoutForm() {
        // Visa kassasidan (redirect till varukorg eller en bekräftelse)
        // Här kan du returnera "cart" om du vill, eller en separat checkout-sida.
        // Vi låter den vara kvar på cart-sidan tills vidare.
        return "cart";
    }

    @PostMapping("/checkout")
    public String placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        orderService.createOrder(user);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String listOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("orders", orderRepository.findByUserOrderByOrderDateDesc(user));
        return "orders";
    }
}