package com.spiceswap.spiceswap.dto.request;

import com.spiceswap.spiceswap.validation.Base64Image;
import com.spiceswap.spiceswap.validation.FieldExistence;
import com.spiceswap.spiceswap.common.util.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIngredientRequest {
    @NotBlank
    @FieldExistence(tableName = "ingredients", fieldName = "ingredient_name", shouldExist = false, message = Constants.ValidationMessage.INGREDIENT_EXISTS)
    private String ingredientName;

    @NotBlank
    @Base64Image(message = Constants.ValidationMessage.INVALID_BASE64_IMG)
    private String image;
}
