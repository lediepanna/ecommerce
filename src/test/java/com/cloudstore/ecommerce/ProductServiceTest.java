package com.cloudstore.ecommerce;

import com.cloudstore.ecommerce.dto.ProductDTO;
import com.cloudstore.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductService productService;

    private final String apiUrl = "https://fakestoreapi.com/products";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productService, "apiUrl", apiUrl);
    }

    @Test
    void testGetAllProducts() {
        ProductDTO p1 = new ProductDTO();
        p1.setId(1L);
        p1.setTitle("Test Product");
        List<ProductDTO> mockProducts = List.of(p1);
        ResponseEntity<List<ProductDTO>> responseEntity = new ResponseEntity<>(mockProducts, HttpStatus.OK);

        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        List<ProductDTO> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getTitle());
    }

    @Test
    void testGetProductById() {
        ProductDTO mockProduct = new ProductDTO();
        mockProduct.setId(5L);
        mockProduct.setTitle("Product 5");
        when(restTemplate.getForObject(apiUrl + "/5", ProductDTO.class)).thenReturn(mockProduct);

        ProductDTO product = productService.getProductById(5L);
        assertNotNull(product);
        assertEquals(5L, product.getId());
    }
}