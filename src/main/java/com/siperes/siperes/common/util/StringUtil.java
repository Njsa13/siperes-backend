package com.siperes.siperes.common.util;

import com.siperes.siperes.exception.ServiceBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.FILED_GET_TWO_WORDS;

@Component
@RequiredArgsConstructor
public class StringUtil {

    public String getFirstTwoWords(String input) {
        try {
            if (input == null || input.trim().isEmpty()) {
                return "";
            }
            String[] words = input.split("\\s+");
            if (words.length == 0) {
                return "";
            } else if (words.length == 1) {
                return words[0];
            } else {
                return words[0] + " " + words[1];
            }
        } catch (Exception e) {
            throw new ServiceBusinessException(FILED_GET_TWO_WORDS);
        }
    }
}