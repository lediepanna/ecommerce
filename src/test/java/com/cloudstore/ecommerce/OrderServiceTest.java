package com.cloudstore.ecommerce;

import com.cloudstore.ecommerce.model.User;
import com.cloudstore.ecommerce.service.CartService;
import com.cloudstore.ecommerce.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {
    @MockBean
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @Test
    void testCreateOrderThrowsWhenCartEmpty() {
        when(cartService.getCart().isEmpty()).thenReturn(true);
        User user = new User();
        assertThrows(IllegalStateException.class, () -> orderService.createOrder(user));
    }
}