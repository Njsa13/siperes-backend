package com.siperes.siperes.dto.request;

import com.siperes.siperes.enumeration.EnumVisibility;
import com.siperes.siperes.validation.Base64Image;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.siperes.siperes.common.util.Constants.ValidationMessage.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecipeRequest {
    @NotBlank(message = NOT_BLANK)
    private String recipeName;

    @NotBlank(message = NOT_BLANK)
    private String about;

    @NotBlank(message = NOT_BLANK)
    @Base64Image(message = INVALID_BASE64_IMG)
    private String thumbnailImage;

    @NotNull(message = NOT_BLANK)
    @Digits(integer = 5, fraction = 0, message = INVALID_VALUE)
    private Integer portion;

    @NotNull(message = NOT_BLANK)
    private EnumVisibility visibility;

    @Valid
    @Size(max = 50, message = MAX_INGREDIENTS)
    @NotEmpty
    private List<CreateIngredientDetailRequest> ingredientDetailRequests;

    @Valid
    @Size(max = 40, message = MAX_STEPS)
    @NotEmpty
    private List<CreateStepRequest> stepRequests;
}
