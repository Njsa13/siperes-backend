package com.siperes.siperes.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateRecipeResponse {
    private String recipeSlug;
    private String recipeName;
    private Integer portion;
    private LocalDateTime updatedAt;
    private List<UpdateIngredientDetailResponse> updateIngredientDetailResponses;
    private List<UpdateStepResponse> updateStepResponses;
}
