package com.verygoodbank.tes.validator;

import com.verygoodbank.tes.exception.InvalidFileException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CsvValidationServiceTest {

    private static final String VALID_HEADER = "a,b,c,d";
    CsvValidationService validationService = new CsvValidationService(VALID_HEADER);

    @Test
    void validateCsvFileValidTest() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("valid.csv");
        validationService.validateCsvFile(file);
    }

    @Test
    void validateCsvFileInvalidFilenameTest() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test123");
        assertThrows(InvalidFileException.class, () -> validationService.validateCsvFile(file));
    }

    @Test
    void validateCsvFileEmptyTest() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("valid.csv");
        when(file.isEmpty()).thenReturn(true);
        assertThrows(InvalidFileException.class, () -> validationService.validateCsvFile(file));
    }

    @Test
    void validateCsvHeaderValidTest() {
        validationService.validateCsvHeader(VALID_HEADER);
    }

    @Test
    void validateCsvHeaderInvalidTest() {
        assertThrows(InvalidFileException.class, () -> validationService.validateCsvHeader("test1"));
    }

}