package com.verygoodbank.tes.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequestScope
@Slf4j
public class CachedDateValidationService {

    private final Map<String, Boolean> validationCacheMap;
    private final DateTimeFormatter formatter;

    public CachedDateValidationService(@Value("${trades.request.csv.dateformat}") String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
        validationCacheMap = new ConcurrentHashMap<>();
    }

    public boolean isDateStringValid(String date) {
        if (validationCacheMap.computeIfAbsent(date, this::parseAndValidate)) {
            return true;
        } else {
            log.error("date '{}' is not in yyyyMMdd format.", date);
            return false;
        }
    }

    private boolean parseAndValidate(String dateString) {
        try {
            formatter.parse(dateString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
