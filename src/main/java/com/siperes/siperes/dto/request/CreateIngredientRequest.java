package com.siperes.siperes.dto.request;

import com.siperes.siperes.validation.Base64Image;
import com.siperes.siperes.validation.FieldExistence;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.siperes.siperes.common.util.Constants.ValidationMessage.INGREDIENT_EXISTS;
import static com.siperes.siperes.common.util.Constants.ValidationMessage.INVALID_BASE64_IMG;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIngredientRequest {
    @NotBlank
    @FieldExistence(tableName = "ingredients", fieldName = "ingredient_name", shouldExist = false, message = INGREDIENT_EXISTS)
    private String ingredientName;

    @NotBlank
    @Base64Image(message = INVALID_BASE64_IMG)
    private String image;
}
