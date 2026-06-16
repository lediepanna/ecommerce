// ProductServiceTest.java
package com.cloudstore.ecommerce.service;

import com.cloudstore.ecommerce.client.FakeStoreClient;
import com.cloudstore.ecommerce.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private FakeStoreClient fakeStoreClient;

    @InjectMocks
    private ProductService productService;

    private ProductDTO product1;
    private ProductDTO product2;

    @BeforeEach
    void setUp() {
        product1 = new ProductDTO();
        product1.setId(1L);
        product1.setTitle("Test Product 1");
        product1.setPrice(99.99);

        product2 = new ProductDTO();
        product2.setId(2L);
        product2.setTitle("Test Product 2");
        product2.setPrice(49.99);
    }

    @Test
    void getAllProducts_shouldReturnProductList() {
        List<ProductDTO> mockList = Arrays.asList(product1, product2);
        when(fakeStoreClient.getAllProducts()).thenReturn(mockList);

        List<ProductDTO> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Test Product 1", products.get(0).getTitle());
        verify(fakeStoreClient, times(1)).getAllProducts();
    }

    @Test
    void getProductById_shouldReturnProduct() {
        when(fakeStoreClient.getProductById(1L)).thenReturn(product1);

        ProductDTO product = productService.getProductById(1L);

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Test Product 1", product.getTitle());
        verify(fakeStoreClient, times(1)).getProductById(1L);
    }

    @Test
    void getProductById_notFound_shouldReturnNull() {
        when(fakeStoreClient.getProductById(99L)).thenReturn(null);

        ProductDTO product = productService.getProductById(99L);

        assertNull(product);
        verify(fakeStoreClient, times(1)).getProductById(99L);
    }
}