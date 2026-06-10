package com.cloudstore.ecommerce.repository;

import com.cloudstore.ecommerce.model.Order;
import com.cloudstore.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
}