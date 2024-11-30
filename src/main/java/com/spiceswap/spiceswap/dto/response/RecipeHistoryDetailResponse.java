package com.spiceswap.spiceswap.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spiceswap.spiceswap.model.json.RecipeDetailJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeHistoryDetailResponse {
    private String historySlug;
    private String updateBy;
    private LocalDate updatedAt;
    private RecipeDetailJson previousRecipe;
    private RecipeDetailJson currentRecipe;
}
