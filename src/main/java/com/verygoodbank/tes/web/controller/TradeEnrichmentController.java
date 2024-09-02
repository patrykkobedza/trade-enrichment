package com.verygoodbank.tes.web.controller;


import com.verygoodbank.tes.exception.InvalidFileException;
import com.verygoodbank.tes.service.BulkEnrichmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
    public StreamingResponseBody enrichTrades(@RequestParam("file") MultipartFile file) {
        return os -> bulkEnrichmentService.processFileRequest(file, os);
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(InvalidFileException.class)
    public String invalidFile(InvalidFileException invalidFileException) {
        return invalidFileException.getMessage();
    }
}


