package com.reg.time_series;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter
public class IntegerArrayConverter implements AttributeConverter<Integer[], String> {

    @Override
    public String convertToDatabaseColumn(Integer[] attribute) {
        if (attribute == null || attribute.length == 0) {
            return null;
        }
        return Arrays.toString(attribute);
    }

    @Override
    public Integer[] convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        String[] values = dbData.substring(1, dbData.length() - 1).split(", ");
        Integer[] result = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = Integer.parseInt(values[i]);
        }
        return result;
    }
}
