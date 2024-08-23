package com.verygoodbank.tes.service;

import com.verygoodbank.tes.util.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import static com.verygoodbank.tes.util.Const.MISSING_PRODUCT_PLACEHOLDER;

@Service
@Slf4j
public class ProductService {

    private final HashMap<String, String> productMap;

    public ProductService(@Value("${product.staticlist.filename}") String productListFileName) throws IOException {
        productMap = new HashMap<>();
        ClassPathResource resource = new ClassPathResource(productListFileName);
        if (!resource.exists()) {
            log.error("Non existing products file: {}", productListFileName);
            throw new FileNotFoundException("Non existing file");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            reader.lines().skip(1).forEach(this::putLineInProductMap);
        }
        log.info("Loading products static file: {}", productListFileName);
        log.info("Map of {} products initialized", productMap.size());
    }

    private void putLineInProductMap(String line) {
        String[] b = line.split(Const.CSV_SEPARATOR);
        productMap.put(b[0], b[1]);
    }

    public String resolveProductNameById(String id) {
        return productMap.getOrDefault(id, MISSING_PRODUCT_PLACEHOLDER);
    }
}
