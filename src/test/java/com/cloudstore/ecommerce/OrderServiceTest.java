// OrderServiceTest.java (uppdaterad med alla tester)
package com.cloudstore.ecommerce.service;

import com.cloudstore.ecommerce.model.Order;
import com.cloudstore.ecommerce.model.User;
import com.cloudstore.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private final Long productId = 1L;
    private final String productName = "Test Product";
    private final Double price = 99.99;
    private final Integer quantity = 2;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
    }

    @Test
    void createDirectOrder_shouldCreateOrder() {
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order order = orderService.createDirectOrder(testUser, productId, productName, price, quantity);

        assertNotNull(order);
        assertEquals(1L, order.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void payOrder_shouldUpdateStatusToPaid() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(testUser);
        order.setStatus("PENDING");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order paidOrder = orderService.payOrder(1L, testUser);

        assertEquals("PAID", paidOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void payOrder_orderNotFound_shouldThrowException() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.payOrder(99L, testUser));
    }

    @Test
    void payOrder_wrongUser_shouldThrowException() {
        Order order = new Order();
        order.setId(1L);
        User otherUser = new User();
        otherUser.setId(2L);
        order.setUser(otherUser);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> orderService.payOrder(1L, testUser));
    }

    @Test
    void payOrder_alreadyPaid_shouldThrowException() {
        Order order = new Order();
        order.setId(1L);
        order.setUser(testUser);
        order.setStatus("PAID");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> orderService.payOrder(1L, testUser));
    }

    @Test
    void getOrdersForUser_shouldReturnOrders() {
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findByUserOrderByOrderDateDesc(testUser)).thenReturn(orders);

        List<Order> result = orderService.getOrdersForUser(testUser);

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findByUserOrderByOrderDateDesc(testUser);
    }
}