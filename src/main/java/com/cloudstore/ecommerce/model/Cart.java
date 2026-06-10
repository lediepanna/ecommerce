package com.cloudstore.ecommerce.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@SessionScope
@Data
public class Cart {
    private Map<Long, CartItem> items = new LinkedHashMap<>();

    public void addItem(CartItem item) {
        Long id = item.getProductId();
        if (items.containsKey(id)) {
            CartItem existing = items.get(id);
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        } else {
            items.put(id, item);
        }
    }

    public void removeItem(Long productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}