package com.cloudstore.ecommerce.service;

import com.cloudstore.ecommerce.dto.ProductDTO;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductService productService;

    private final String apiUrl = "https://fakestoreapi.com/products";
    private ProductDTO mockProduct;

    @BeforeEach
    void setUp() {
        // Sätt apiUrl i ProductService via Reflection (om den inte har konstruktor)
        ReflectionTestUtils.setField(productService, "apiUrl", apiUrl);

        mockProduct = new ProductDTO();
        mockProduct.setId(1L);
        mockProduct.setTitle("Test Product");
        mockProduct.setPrice(99.99);
        mockProduct.setDescription("Test Description");
        mockProduct.setCategory("Test Category");
        mockProduct.setImage("https://test.com/image.jpg");
    }

    @Test
    void getAllProducts_shouldReturnProductList() {
        List<ProductDTO> mockProducts = Arrays.asList(mockProduct);
        ResponseEntity<List<ProductDTO>> responseEntity = new ResponseEntity<>(mockProducts, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(apiUrl),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        List<ProductDTO> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getTitle());
        verify(restTemplate, times(1)).exchange(
                eq(apiUrl),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void getProductById_shouldReturnProduct() {
        when(restTemplate.getForObject(
                eq(apiUrl + "/1"),
                eq(ProductDTO.class)
        )).thenReturn(mockProduct);

        ProductDTO product = productService.getProductById(1L);

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getTitle());
        verify(restTemplate, times(1)).getForObject(
                eq(apiUrl + "/1"),
                eq(ProductDTO.class)
        );
    }

    @Test
    void getProductById_notFound_shouldReturnNull() {
        when(restTemplate.getForObject(
                eq(apiUrl + "/999"),
                eq(ProductDTO.class)
        )).thenReturn(null);

        ProductDTO product = productService.getProductById(999L);

        assertNull(product);
        verify(restTemplate, times(1)).getForObject(
                eq(apiUrl + "/999"),
                eq(ProductDTO.class)
        );
    }

    @Test
    void getProductById_shouldHandleException() {
        when(restTemplate.getForObject(
                eq(apiUrl + "/1"),
                eq(ProductDTO.class)
        )).thenThrow(new RuntimeException("API Error"));

        assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
        verify(restTemplate, times(1)).getForObject(
                eq(apiUrl + "/1"),
                eq(ProductDTO.class)
        );
    }

    @Test
    void getAllProducts_shouldHandleEmptyList() {
        List<ProductDTO> emptyList = Arrays.asList();
        ResponseEntity<List<ProductDTO>> responseEntity = new ResponseEntity<>(emptyList, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(apiUrl),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        List<ProductDTO> products = productService.getAllProducts();

        assertNotNull(products);
        assertTrue(products.isEmpty());
        verify(restTemplate, times(1)).exchange(
                eq(apiUrl),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );
    }
}