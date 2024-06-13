package com.siperes.siperes.dto.request;

import com.siperes.siperes.enumeration.EnumVisibility;
import com.siperes.siperes.validation.Base64Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.siperes.siperes.common.util.Constants.ValidationMessage.INVALID_BASE64_IMG;
import static com.siperes.siperes.common.util.Constants.ValidationMessage.NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetRecipeRequest {
    @Base64Image(message = INVALID_BASE64_IMG)
    private String thumbnailImage;

    @NotBlank(message = NOT_BLANK)
    private String recipeName;

    @NotBlank(message = NOT_BLANK)
    private String about;

    @NotNull(message = NOT_BLANK)
    private EnumVisibility visibility;
}
