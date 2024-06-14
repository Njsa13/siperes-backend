package com.siperes.siperes.common.converter;

import com.siperes.siperes.enumeration.EnumSortBy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumSortByConverter implements Converter<String, EnumSortBy> {
    @Override
    public EnumSortBy convert(String source) {
        try {
            return EnumSortBy.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid value for EnumSortBy: " + source);
        }
    }
}
