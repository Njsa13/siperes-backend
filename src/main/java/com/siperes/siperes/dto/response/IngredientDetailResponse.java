package com.siperes.siperes.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDetailResponse {
    private String ingredientDetailSlug;
    private String ingredientName;
    private String dose;
    private IngredientResponse ingredientResponse;
}
