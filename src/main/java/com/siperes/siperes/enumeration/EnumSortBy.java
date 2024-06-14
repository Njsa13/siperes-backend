package com.siperes.siperes.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static com.siperes.siperes.common.util.Constants.ValidationMessage.UNKNOWN_ENUM;

public enum EnumSortBy {
    NEWEST("newest"),
    OLDEST("oldest"),
    POPULAR("popular");

    private final String value;

    EnumSortBy(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumSortBy fromValue(String value) {
        for (EnumSortBy sortBy : EnumSortBy.values()) {
            if (sortBy.value.equalsIgnoreCase(value)) {
                return sortBy;
            }
        }
        throw new IllegalArgumentException(UNKNOWN_ENUM + value);
    }
}
