package com.spiceswap.spiceswap.dto.request;

import com.spiceswap.spiceswap.common.util.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDetailRequest {
    @NotBlank(message = Constants.ValidationMessage.NOT_BLANK)
    @Pattern(regexp = "^\\S+$", message = Constants.ValidationMessage.CANT_CONTAIN_SPACES)
    @Schema(example = "username")
    private String username;

    @NotBlank(message = Constants.ValidationMessage.NOT_BLANK)
    private String name;

    private LocalDate dateOfBirth;
}
