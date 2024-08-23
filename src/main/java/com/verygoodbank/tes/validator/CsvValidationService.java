package com.verygoodbank.tes.validator;

import com.verygoodbank.tes.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;

import static com.verygoodbank.tes.util.Const.CSV_SEPARATOR;

@Service
public final class CsvValidationService {

    private final String tradesRequestCsvHeader;

    public CsvValidationService(@Value("${trades.request.csv.header}") String tradesRequestCsvHeader) {
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

    public void validateCsvHeader(String firstLine) {
        if (!firstLine.equals(tradesRequestCsvHeader)){
            throw new InvalidFileException(String.format("Invalid input header: %s", firstLine));
        }
    }
}
