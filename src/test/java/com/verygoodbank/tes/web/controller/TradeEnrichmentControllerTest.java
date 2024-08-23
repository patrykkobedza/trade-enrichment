package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.service.BulkEnrichmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TradeEnrichmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    BulkEnrichmentService bulkEnrichmentService;

    @Test
    void e2eTest() throws Exception {
        ClassPathResource resource = new ClassPathResource("trades.csv");

        MockMultipartFile file = new MockMultipartFile("file", "trades.csv", null, resource.getInputStream());
        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/enrich")
                        .file(file)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("""
                        date,product_name,currency,price\r
                        20160101,test1,EUR,10.0\r
                        20160101,test2,EUR,20.1\r
                        20160101,Missing Product Name,EUR,35.34\r
                        """));


    }

}