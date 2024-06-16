package com.siperes.siperes.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.siperes.siperes.common.util.Constants.ValidationMessage.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDetailRequest {
    @NotBlank(message = NOT_BLANK)
    @Pattern(regexp = "^\\S+$", message = CANT_CONTAIN_SPACES)
    @Schema(example = "username")
    private String username;

    @NotBlank(message = NOT_BLANK)
    private String name;

    private LocalDate dateOfBirth;
}
