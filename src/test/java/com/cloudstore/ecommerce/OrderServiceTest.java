package com.cloudstore.ecommerce;

import com.cloudstore.ecommerce.model.Cart;
import com.cloudstore.ecommerce.model.User;
import com.cloudstore.ecommerce.repository.OrderRepository;
import com.cloudstore.ecommerce.service.CartService;
import com.cloudstore.ecommerce.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void testCreateOrderThrowsWhenCartEmpty() {
        User user = new User();
        Cart emptyCart = new Cart();

        when(cartService.getCart()).thenReturn(emptyCart);

        assertThrows(IllegalStateException.class, () -> orderService.createOrder(user));
        verify(orderRepository, never()).save(any());
        verify(cartService, never()).clearCart();
    }
}