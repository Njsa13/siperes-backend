package com.siperes.siperes.dto.request;

import com.siperes.siperes.validation.Base64Image;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.siperes.siperes.common.util.Constants.ValidationMessage.INVALID_BASE64_IMG;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIngredientRequest {
    @NotBlank
    private String ingredientName;

    @Base64Image(message = INVALID_BASE64_IMG)
    private String image;
}
