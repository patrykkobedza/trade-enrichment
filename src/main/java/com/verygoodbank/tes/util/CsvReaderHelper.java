package com.verygoodbank.tes.util;

import java.util.HashMap;
import java.util.Map;

public class CsvReaderHelper {

    private Map<String, Integer> headersMap;

    public CsvReaderHelper(String[] headers) {

        headersMap = new HashMap<>();

        int count = 0;
        for (String header : headers) {
            headersMap.put(header, count++);
        }
    }

    public String getValueByName(String[] row, String name) {
        return row[headersMap.get(name)];
    }

    public Integer getFieldId(String name) {
        return headersMap.get(name);
    }
}
