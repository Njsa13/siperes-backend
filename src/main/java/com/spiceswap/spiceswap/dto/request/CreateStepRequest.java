package com.spiceswap.spiceswap.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.spiceswap.spiceswap.common.util.Constants.ValidationMessage.NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStepRequest {
    @NotBlank(message = NOT_BLANK)
    private String stepDescription;
}
