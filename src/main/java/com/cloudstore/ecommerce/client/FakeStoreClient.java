package com.cloudstore.ecommerce.client;

import com.cloudstore.ecommerce.dto.ProductDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

@Component
public class FakeStoreClient {
    private final RestClient restClient;

    public FakeStoreClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://fakestoreapi.com").build();
    }

    public List<ProductDTO> getAllProducts() {
        return restClient.get()
                .uri("/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProductDTO>>() {});
    }

    public ProductDTO getProductById(Long id) {
        return restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(ProductDTO.class);
    }
}