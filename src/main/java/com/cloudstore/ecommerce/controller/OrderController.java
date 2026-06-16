package com.cloudstore.ecommerce.controller;

import com.cloudstore.ecommerce.client.FakeStoreClient;
import com.cloudstore.ecommerce.dto.ProductDTO;
import com.cloudstore.ecommerce.model.Order;
import com.cloudstore.ecommerce.model.User;
import com.cloudstore.ecommerce.service.OrderService;
import com.cloudstore.ecommerce.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final FakeStoreClient fakeStoreClient;

    public OrderController(OrderService orderService, UserService userService, FakeStoreClient fakeStoreClient) {
        this.orderService = orderService;
        this.userService = userService;
        this.fakeStoreClient = fakeStoreClient;
    }

    @GetMapping("/orders")
    public String showOrders(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<Order> orders = orderService.getOrdersForUser(user);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @PostMapping("/order/create")
    public String createOrder(@RequestParam Long productId,
                              @RequestParam(defaultValue = "1") Integer quantity,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);

            ProductDTO product = fakeStoreClient.getProductById(productId);
            if (product == null) {
                throw new RuntimeException("Produkten hittades inte");
            }

            Order order = orderService.createDirectOrder(user, productId, product.getTitle(), product.getPrice(), quantity);
            redirectAttributes.addFlashAttribute("message", "✅ Order #" + order.getId() + " skapad! Betala nu.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Fel: " + e.getMessage());
        }
        return "redirect:/orders";
    }

    @PostMapping("/order/pay/{id}")
    public String payOrder(@PathVariable Long id,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            Order paidOrder = orderService.payOrder(id, user);
            redirectAttributes.addFlashAttribute("message", "🎉 Order #" + paidOrder.getId() + " betald! Tack!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Betalning misslyckades: " + e.getMessage());
        }
        return "redirect:/orders";
    }
}