package com.verygoodbank.tes.validator;

import com.verygoodbank.tes.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public final class CsvValidationService {

    private final String[] tradesRequestCsvHeader;

    public CsvValidationService(@Value("${trades.request.csv.header}") String[] tradesRequestCsvHeader) {
        this.tradesRequestCsvHeader = tradesRequestCsvHeader;
    }

    public void validateCsvFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (Objects.isNull(fileName) || !fileName.endsWith(".csv")) {
            throw new InvalidFileException(String.format("%s is not valid csv file.", fileName));
        }
        if (file.isEmpty()) {
            throw new InvalidFileException(String.format("%s is empty file", fileName));
        }
    }

    public void validateCsvHeader(String[] values) {
        List<String> missingColumnNames = Arrays.stream(tradesRequestCsvHeader)
                .filter(e -> !List.of(values).contains(e))
                .toList();

        if (!missingColumnNames.isEmpty()) {
            throw new InvalidFileException(String.format("Missing columns: %s", missingColumnNames));
        }
    }
}
