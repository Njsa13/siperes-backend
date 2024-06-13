package com.siperes.siperes.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static com.siperes.siperes.common.util.Constants.ValidationMessage.UNKNOWN_ENUM;

public enum EnumVisibility {
    PRIVATE("private"),
    PUBLIC("public");

    private final String value;

    EnumVisibility(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumVisibility fromValue(String value) {
        for (EnumVisibility visibility : EnumVisibility.values()) {
            if (visibility.value.equalsIgnoreCase(value)) {
                return visibility;
            }
        }
        throw new IllegalArgumentException(UNKNOWN_ENUM + value);
    }
}
