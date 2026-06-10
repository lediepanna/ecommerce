package com.cloudstore.ecommerce.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status;
    private List<OrderItemResponse> items;

    @Data
    public static class OrderItemResponse {
        private String productName;
        private Double price;
        private Integer quantity;
    }
}