package com.cloudstore.ecommerce.service;

import com.cloudstore.ecommerce.model.Cart;
import com.cloudstore.ecommerce.model.CartItem;
import com.cloudstore.ecommerce.dto.ProductDTO;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final Cart cart;
    private final ProductService productService;

    public CartService(Cart cart, ProductService productService) {
        this.cart = cart;
        this.productService = productService;
    }

    public void addToCart(Long productId, int quantity) {
        ProductDTO product = productService.getProductById(productId);
        CartItem item = new CartItem();
        item.setProductId(product.getId());
        item.setName(product.getTitle());
        item.setPrice(product.getPrice());
        item.setQuantity(quantity);
        cart.addItem(item);
    }

    public void removeFromCart(Long productId) {
        cart.removeItem(productId);
    }

    public Cart getCart() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }
}