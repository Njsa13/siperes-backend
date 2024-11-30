package com.spiceswap.spiceswap.dto.request;

import com.spiceswap.spiceswap.validation.ValidSlug;
import com.spiceswap.spiceswap.common.util.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRecipeReviewRequest {
    @NotBlank(message = Constants.ValidationMessage.NOT_BLANK)
    @ValidSlug(message = Constants.ErrorMessage.INVALID_SLUG)
    private String recipeSlug;

    @NotNull(message = Constants.ValidationMessage.NOT_BLANK)
    @Min(value = 1, message = Constants.ValidationMessage.MIN_RATING)
    @Max(value = 5, message = Constants.ValidationMessage.MAX_RATING)
    private Integer rating;

    @NotBlank(message = Constants.ValidationMessage.NOT_BLANK)
    private String comment;
}
