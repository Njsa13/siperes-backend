package com.siperes.siperes.dto.request;

import com.siperes.siperes.validation.FieldExistence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.siperes.siperes.common.util.Constants.ValidationMessage.INVALID_EMAIL;
import static com.siperes.siperes.common.util.Constants.ValidationMessage.NOT_BLANK;
import static com.siperes.siperes.common.util.Constants.ValidationMessage.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = NOT_BLANK)
    @FieldExistence(tableName = "users", fieldName = "username", shouldExist = false, message = USER_EXISTS)
    @Pattern(regexp = "^\\S+$", message = CANT_CONTAIN_SPACES)
    private String username;

    @NotBlank(message = NOT_BLANK)
    private String name;

    @NotBlank(message = NOT_BLANK)
    @Email(message = INVALID_EMAIL)
    @FieldExistence(tableName = "users", fieldName = "email", shouldExist = false, message = EMAIL_EXISTS)
    private String email;

    @NotBlank(message = NOT_BLANK)
    @Size(min = 6, message = PASSWORD_MIN_CHAR)
    private String password;
}
