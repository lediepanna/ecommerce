package com.cloudstore.ecommerce.service;

import com.cloudstore.ecommerce.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class ProductService {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public ProductService(RestTemplate restTemplate,
                          @Value("${fakestore.api.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public List<ProductDTO> getAllProducts() {
        return restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProductDTO>>() {}).getBody();
    }

    public ProductDTO getProductById(Long id) {
        String url = apiUrl + "/" + id;
        return restTemplate.getForObject(url, ProductDTO.class);
    }
}