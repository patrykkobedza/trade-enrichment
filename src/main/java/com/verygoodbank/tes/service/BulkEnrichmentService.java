package com.verygoodbank.tes.service;

import com.verygoodbank.tes.validator.CsvValidationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.verygoodbank.tes.util.Const.CSV_SEPARATOR;
import static com.verygoodbank.tes.util.Const.EMPTY_STRING;

@Service
@Slf4j
public class BulkEnrichmentService {

    private final CsvValidationService validationService;
    private final ProductService productService;
    private final DateTimeFormatter formatter;

    private final String tradesResponseCsvHeader;

    public BulkEnrichmentService(CsvValidationService validationService, ProductService productService,
                                 @Value("${trades.response.csv.header}") String tradesResponseCsvHeader,
                                 @Value("${trades.request.csv.dateformat}") String dateFormat) {
        this.validationService = validationService;
        this.productService = productService;
        this.tradesResponseCsvHeader = tradesResponseCsvHeader;
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    public void processFileRequest(MultipartFile file, OutputStream outputStream) throws IOException {
        validationService.validateCsvFile(file);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            validationService.validateCsvHeader(reader.readLine());
            while (reader.ready()) {
                writeCsvHeader(outputStream);
                reader.lines().forEach(line -> processLine(line, outputStream));
            }
            outputStream.close();
        }
    }

    private void writeCsvHeader(OutputStream outputStream) throws IOException {
        outputStream.write(tradesResponseCsvHeader.getBytes());
        outputStream.write(System.lineSeparator().getBytes());
    }

    @SneakyThrows
    private void processLine(String line, OutputStream outputStream) {
        String[] values = line.split(CSV_SEPARATOR);
        if (isDateStringValid(values[0])) {
            values[1] = productService.resolveProductNameById(values[1]);
            String result = Arrays.stream(values).collect(Collectors.joining(CSV_SEPARATOR, EMPTY_STRING, System.lineSeparator()));
            outputStream.write(result.getBytes());
        }
    }


    private boolean isDateStringValid(String dateString) {
        try {
            formatter.parse(dateString);
            return true;
        } catch (DateTimeParseException e) {
            log.error("date '{}' is not in yyyyMMdd format.", dateString);
            return false;
        }
    }
}
