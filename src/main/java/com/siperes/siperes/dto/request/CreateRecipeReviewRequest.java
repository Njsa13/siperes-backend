package com.siperes.siperes.dto.request;

import com.siperes.siperes.validation.ValidSlug;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.siperes.siperes.common.util.Constants.ErrorMessage.INVALID_SLUG;
import static com.siperes.siperes.common.util.Constants.ValidationMessage.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecipeReviewRequest {
    @NotBlank(message = NOT_BLANK)
    @ValidSlug(message = INVALID_SLUG)
    private String recipeSlug;

    @NotNull(message = NOT_BLANK)
    @Min(value = 1, message = MIN_RATING)
    @Max(value = 5, message = MAX_RATING)
    private Integer rating;

    @NotBlank(message = NOT_BLANK)
    private String comment;
}
