package com.siperes.siperes.common.util;

import com.github.slugify.Slugify;
import com.siperes.siperes.exception.ServiceBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.FAILED_CHECK_FIELD_EXISTS;
import static com.siperes.siperes.common.util.Constants.ErrorMessage.INPUT_CANNOT_NULL;

@Component
@RequiredArgsConstructor
public class SlugUtil {
    private final JdbcTemplate jdbcTemplate;

    @Async
    public CompletableFuture<String> toSlug(String tableName, String fieldName, String input) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (input == null || input.isEmpty()) {
                    throw new IllegalArgumentException(INPUT_CANNOT_NULL);
                }
                final Slugify slg = Slugify.builder().build();
                String slug = slg.slugify(input);
                String newSlug = slug + "-" + UUID.randomUUID();
                boolean checkSlug = isSlugExist(tableName, fieldName, newSlug);
                if (checkSlug) {
                    newSlug = slug + "-" + UUID.randomUUID();
                }
                return newSlug;
            } catch (Exception e) {
                throw new ServiceBusinessException(e.getMessage());
            }
        });
    }

    private boolean isSlugExist(String tableName, String fieldName, String value) {
        try {
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + fieldName + "=" + "?";
            int count = jdbcTemplate.queryForObject(sql, Integer.class, value);
            return count > 0;
        } catch (Exception e) {
            throw new ServiceBusinessException(FAILED_CHECK_FIELD_EXISTS);
        }
    }
}
