package com.verygoodbank.tes.service;

import com.verygoodbank.tes.validator.CsvValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class BulkEnrichmentServiceTest {

    @Test
    void processFileRequestTest() throws IOException {
        CsvValidationService validationService = mock(CsvValidationService.class);
        ProductService productService = mock(ProductService.class);
        MultipartFile file = Mockito.mock(MultipartFile.class);
        OutputStream os = Mockito.mock(OutputStream.class);
        BulkEnrichmentService bulkEnrichmentService = new BulkEnrichmentService(validationService, productService, "a,b,c", "yyyyMMdd");
        InputStream is = mock(InputStream.class);
        when(file.getInputStream()).thenReturn(is);
        when(is.read(any(), anyInt(), anyInt())).thenReturn(-1);
        bulkEnrichmentService.processFileRequest(file, os);

        verify(validationService).validateCsvFile(file);
        verify(validationService).validateCsvHeader(any());
        verify(os).close();

    }

}