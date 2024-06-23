package com.siperes.siperes.dto.response;

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
public class RecipeInformation {
    private Integer totalRecipes;
    private Integer totalActiveRecipes;
    private Integer totalInactiveRecipes;
    private Integer totalOriginalRecipes;
    private Integer totalCopyRecipes;
    private NewRecipes newRecipes;
    private List<PopularRecipe> popularRecipes;
}
