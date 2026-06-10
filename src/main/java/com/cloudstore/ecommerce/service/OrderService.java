package com.cloudstore.ecommerce.service;

import com.cloudstore.ecommerce.model.*;
import com.cloudstore.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    @Transactional
    public Order createOrder(User user) {
        Cart cart = cartService.getCart();
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        double total = 0.0;

        for (CartItem cartItem : cart.getItems().values()) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(cartItem.getProductId());
            item.setProductName(cartItem.getName());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            order.getItems().add(item);
            total += cartItem.getPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(total);
        order.setStatus("CONFIRMED");
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart();
        return savedOrder;
    }
}