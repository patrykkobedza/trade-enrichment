package com.verygoodbank.tes.service;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest {

    @Test
    void createServiceFileNotExistsTest() {
        assertThrows(FileNotFoundException.class, () -> new ProductService("invalid"));
    }

    @Test
    void resolveByIdTest() throws IOException {
        ProductService productService = new ProductService("test_products.csv");

        String result = productService.resolveProductNameById("2");
        assertEquals("test2", result);

        result = productService.resolveProductNameById("wrong");
        assertEquals("Missing Product Name", result);
    }

}