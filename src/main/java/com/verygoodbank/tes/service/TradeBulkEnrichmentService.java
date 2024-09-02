package com.verygoodbank.tes.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.verygoodbank.tes.util.CsvReaderHelper;
import com.verygoodbank.tes.validator.CachedDateValidationService;
import com.verygoodbank.tes.validator.TradeCsvValidationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Objects;

import static com.opencsv.ICSVParser.DEFAULT_ESCAPE_CHARACTER;
import static com.opencsv.ICSVParser.DEFAULT_SEPARATOR;
import static com.opencsv.ICSVWriter.DEFAULT_LINE_END;
import static com.opencsv.ICSVWriter.NO_QUOTE_CHARACTER;
import static com.verygoodbank.tes.util.Const.*;

@Service
@Slf4j
public class TradeBulkEnrichmentService {

    private final TradeCsvValidationService validationService;
    private final ProductService productService;

    private final CachedDateValidationService cachedDateValidationService;


    public TradeBulkEnrichmentService(TradeCsvValidationService validationService, ProductService productService, CachedDateValidationService cachedDateValidationService) {
        this.validationService = validationService;
        this.productService = productService;
        this.cachedDateValidationService = cachedDateValidationService;
    }

    @SneakyThrows
    public void processFileRequest(MultipartFile file, OutputStream outputStream) {
        validationService.validateCsvFile(file);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(reader);
            String[] headerValues = csvReader.readNext();
            validationService.validateCsvHeader(headerValues);
            CsvReaderHelper csvReaderHelper = new CsvReaderHelper(headerValues);
            String[] nextLineValues = csvReader.readNext();
            CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream), DEFAULT_SEPARATOR, NO_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER, DEFAULT_LINE_END);

            csvWriter.writeNext(replaceProductHeader(headerValues));
            while (nextLineValues != null) {
                processLine(csvReaderHelper, nextLineValues, csvWriter);
                nextLineValues = csvReader.readNext();
            }
            csvReader.close();
            csvWriter.close();
        }
    }

    private void processLine(CsvReaderHelper csvReaderHelper, String[] nextLineValues, CSVWriter csvWriter) {
        String date = csvReaderHelper.getValueByName(nextLineValues, DATE_CSV_HEADER);
        boolean isDateValid = cachedDateValidationService.isDateStringValid(date);
        if (isDateValid) {
            String productName = productService.resolveProductNameById(csvReaderHelper.getValueByName(nextLineValues, PRODUCT_ID_CSV_HEADER));
            nextLineValues[csvReaderHelper.getFieldId(PRODUCT_ID_CSV_HEADER)] = productName;
            csvWriter.writeNext(nextLineValues);
        }
    }

    private String[] replaceProductHeader(String[] originalHeaders) {
        return Arrays.stream(originalHeaders).map(e -> {
            if (Objects.equals(e, PRODUCT_ID_CSV_HEADER)) {
                return PRODUCT_NAME_CSV_HEADER;
            }
            return e;
        }).toArray(String[]::new);
    }


}
