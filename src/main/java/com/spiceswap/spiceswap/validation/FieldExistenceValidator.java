package com.spiceswap.spiceswap.validation;

import com.spiceswap.spiceswap.exception.ServiceBusinessException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.spiceswap.spiceswap.common.util.Constants.ErrorMessage.CHECK_FIELD_EXISTS_FAILED;

@Slf4j
@RequiredArgsConstructor
public class FieldExistenceValidator implements ConstraintValidator<FieldExistence, Object> {
    private final JdbcTemplate jdbcTemplate;
    private String tableName;
    private String fieldName;
    private boolean shouldExist;

    @Override
    public void initialize(FieldExistence constraintAnnotation) {
        this.tableName = constraintAnnotation.tableName();
        this.fieldName = constraintAnnotation.fieldName();
        this.shouldExist = constraintAnnotation.shouldExist();
    }

    @Override
    public boolean isValid(Object val, ConstraintValidatorContext constraintValidatorContext) {
        try {
            String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + fieldName + "=" + "?";
            int count = jdbcTemplate.queryForObject(sql, Integer.class, val);
            return shouldExist ? count > 0 : count == 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceBusinessException(CHECK_FIELD_EXISTS_FAILED);
        }
    }
}