package com.spiceswap.spiceswap.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDetailJson {
    private Integer portion;
    private List<IngredientDetailJson> ingredientDetailJson;
    private List<StepJson> stepJson;
}
