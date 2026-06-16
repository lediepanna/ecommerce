package com.cloudstore.ecommerce.service;

import com.cloudstore.ecommerce.model.Order;
import com.cloudstore.ecommerce.model.OrderItem;
import com.cloudstore.ecommerce.model.User;
import com.cloudstore.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createDirectOrder(User user, Long productId, String productName, Double price, Integer quantity) {
        if (quantity == null || quantity <= 0) quantity = 1;

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(price * quantity);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductId(productId);
        item.setProductName(productName);
        item.setPrice(price);
        item.setQuantity(quantity);

        order.getItems().add(item);

        return orderRepository.save(order);
    }

    @Transactional
    public Order payOrder(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order hittades inte"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Du äger inte denna order");
        }
        if ("PAID".equals(order.getStatus())) {
            throw new RuntimeException("Redan betald");
        }

        order.setStatus("PAID");
        return orderRepository.save(order);
    }

    public List<Order> getOrdersForUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
}