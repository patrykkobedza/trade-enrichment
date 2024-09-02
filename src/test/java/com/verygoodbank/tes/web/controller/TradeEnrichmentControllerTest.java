package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.service.TradeBulkEnrichmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TradeEnrichmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    TradeBulkEnrichmentService tradeBulkEnrichmentService;

    @Test
    void e2eTest() throws Exception {
        ClassPathResource resource = new ClassPathResource("trades.csv");

        MockMultipartFile file = new MockMultipartFile("file", "trades.csv", null, resource.getInputStream());
        mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/enrich")
                        .file(file)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(request().asyncStarted())
                .andDo(MvcResult::getAsyncResult)
                .andExpect(content().string("""
                        date,product_name,currency,price
                        20160101,test1,EUR,10.0
                        20160101,test2,EUR,20.1
                        20160101,Missing Product Name,EUR,35.34
                        """));


    }

}