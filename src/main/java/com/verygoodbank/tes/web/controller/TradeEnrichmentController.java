package com.verygoodbank.tes.web.controller;


import com.verygoodbank.tes.exception.InvalidFileException;
import com.verygoodbank.tes.service.BulkEnrichmentService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class TradeEnrichmentController {

    private final BulkEnrichmentService bulkEnrichmentService;

    @Autowired
    public TradeEnrichmentController(BulkEnrichmentService bulkEnrichmentService) {
        this.bulkEnrichmentService = bulkEnrichmentService;
    }


    @PostMapping("/enrich")
    public void enrichTrades(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        bulkEnrichmentService.processFileRequest(file, os);
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(InvalidFileException.class)
    public String invalidFile(InvalidFileException invalidFileException) {
        return invalidFileException.getMessage();
    }
}


