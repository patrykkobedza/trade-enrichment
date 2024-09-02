package com.verygoodbank.tes.service;

import com.opencsv.exceptions.CsvValidationException;
import com.verygoodbank.tes.validator.CachedDateValidationService;
import com.verygoodbank.tes.validator.TradeCsvValidationService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TradeBulkEnrichmentServiceTest {

    @Test
    void processFileRequestTest() throws IOException, CsvValidationException {
        TradeCsvValidationService validationService = mock(TradeCsvValidationService.class);
        ProductService productService = mock(ProductService.class);
        MultipartFile file = Mockito.mock(MultipartFile.class);
        CachedDateValidationService mock = mock(CachedDateValidationService.class);
        OutputStream os = Mockito.mock(OutputStream.class);
        TradeBulkEnrichmentService tradeBulkEnrichmentService = new TradeBulkEnrichmentService(validationService, productService, mock);
        InputStream is = IOUtils.toInputStream("date,product_id,currency,price\n\r" +
                        "20160101,1,EUR,10.0\n\r",
                "UTF-8");
        when(file.getInputStream()).thenReturn(is);
        tradeBulkEnrichmentService.processFileRequest(file, os);

        verify(validationService).validateCsvFile(file);
        verify(validationService).validateCsvHeader(any());
        verify(os).close();

    }

}