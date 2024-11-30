package com.spiceswap.spiceswap.dto.request;

import com.spiceswap.spiceswap.validation.ValidSlug;
import com.spiceswap.spiceswap.common.util.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateModificationReqRequest {
    @NotBlank
    @ValidSlug(message = Constants.ErrorMessage.INVALID_SLUG)
    private String fromRecipeSlug;

    @NotBlank
    private String message;
}
