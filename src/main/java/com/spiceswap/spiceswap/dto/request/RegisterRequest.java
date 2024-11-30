package com.spiceswap.spiceswap.dto.request;

import com.spiceswap.spiceswap.validation.FieldExistence;
import com.spiceswap.spiceswap.common.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = Constants.ValidationMessage.NOT_BLANK)
    @FieldExistence(tableName = "users", fieldName = "username", shouldExist = false, message = Constants.ValidationMessage.USER_EXISTS)
    @Pattern(regexp = "^\\S+$", message = Constants.ValidationMessage.CANT_CONTAIN_SPACES)
    @Schema(example = "username")
    private String username;

    @NotBlank(message = Constants.ValidationMessage.NOT_BLANK)
    private String name;

    @NotBlank(message = Constants.ValidationMessage.NOT_BLANK)
    @Email(message = Constants.ValidationMessage.INVALID_EMAIL)
    @FieldExistence(tableName = "users", fieldName = "email", shouldExist = false, message = Constants.ValidationMessage.EMAIL_EXISTS)
    private String email;

    @NotBlank(message = Constants.ValidationMessage.NOT_BLANK)
    @Size(min = 6, message = Constants.ValidationMessage.PASSWORD_MIN_CHAR)
    private String password;
}
