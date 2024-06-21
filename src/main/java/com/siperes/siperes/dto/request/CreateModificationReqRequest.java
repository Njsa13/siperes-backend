package com.siperes.siperes.dto.request;

import com.siperes.siperes.validation.ValidSlug;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.INVALID_SLUG;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateModificationReqRequest {
    @NotBlank
    @ValidSlug(message = INVALID_SLUG)
    private String fromRecipeSlug;

    @NotBlank
    private String message;
}
