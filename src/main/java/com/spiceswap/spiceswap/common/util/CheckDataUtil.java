package com.spiceswap.spiceswap.common.util;


import com.spiceswap.spiceswap.exception.DataConflictException;
import com.spiceswap.spiceswap.exception.ServiceBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckDataUtil {
    private final JdbcTemplate jdbcTemplate;
    public void checkDataField(String tableName, String fieldName, String fieldValue, String fieldId, UUID fieldValueId) {
        try {
            String sqlCheckUniqueValue = "SELECT COUNT(*) FROM " + tableName + " WHERE " + fieldName + " = ?";
            Integer countSqlCheckUniqueValue = jdbcTemplate.queryForObject(sqlCheckUniqueValue, Integer.class, fieldValue);
            if (countSqlCheckUniqueValue != null && countSqlCheckUniqueValue > 0) {
                String sqlExceptCurrentId = sqlCheckUniqueValue + " AND " + fieldId + " != ?";
                Integer countExceptCurrentId = jdbcTemplate.queryForObject(sqlExceptCurrentId, Integer.class, fieldValue, fieldValueId);

                if (countExceptCurrentId != null && countExceptCurrentId > 0) {
                    throw new DataConflictException("Data "+ fieldValue +"already exists in field " + fieldName);
                }
            }
        } catch (DataConflictException e) {
            throw e;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException("Failed to check field exists");
        }
    }
}
