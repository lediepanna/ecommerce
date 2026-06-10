package com.cloudstore.ecommerce;

import com.cloudstore.ecommerce.model.Cart;
import com.cloudstore.ecommerce.model.CartItem;
import com.cloudstore.ecommerce.model.Order;
import com.cloudstore.ecommerce.model.User;
import com.cloudstore.ecommerce.repository.OrderRepository;
import com.cloudstore.ecommerce.service.CartService;
import com.cloudstore.ecommerce.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrderSuccess() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        CartItem item = new CartItem(1L, "Test Product", 99.99, 2);
        Map<Long, CartItem> items = new LinkedHashMap<>();
        items.put(1L, item);
        cart.setItems(items);

        when(cartService.getCart()).thenReturn(cart);

        Order savedOrder = new Order();
        savedOrder.setId(100L);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order order = orderService.createOrder(user);
        assertNotNull(order);
        assertEquals(100L, order.getId());
        verify(cartService).clearCart();
    }

    @Test
    void testCreateOrderEmptyCart() {
        User user = new User();
        Cart emptyCart = new Cart();
        emptyCart.setItems(new LinkedHashMap<>());
        when(cartService.getCart()).thenReturn(emptyCart);

        assertThrows(IllegalStateException.class, () -> orderService.createOrder(user));
        verify(orderRepository, never()).save(any());
        verify(cartService, never()).clearCart();
    }
}