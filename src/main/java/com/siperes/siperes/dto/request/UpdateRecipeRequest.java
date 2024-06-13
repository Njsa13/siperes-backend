package com.siperes.siperes.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.siperes.siperes.common.util.Constants.ValidationMessage.*;
import static com.siperes.siperes.common.util.Constants.ValidationMessage.NOT_BLANK;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRecipeRequest {
    @NotNull(message = NOT_BLANK)
    @Digits(integer = 5, fraction = 0, message = INVALID_VALUE)
    private Integer portion;

    @Valid
    @Size(max = 50, message = MAX_INGREDIENTS)
    @NotEmpty
    private List<UpdateIngredientDetailRequest> ingredientDetailRequests;

    @Valid
    @Size(max = 40, message = MAX_STEPS)
    @NotEmpty
    private List<UpdateStepRequest> stepRequests;
}
