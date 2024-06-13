package com.siperes.siperes.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.siperes.siperes.enumeration.EnumRecipeType;
import com.siperes.siperes.enumeration.EnumVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyRecipeDetailResponse {
    private String recipeSlug;
    private String recipeName;
    private String about;
    private String thumbnailImageLink;
    private String owner;
    private Integer portion;
    private Double totalRating;
    private EnumVisibility visibility;
    private EnumRecipeType recipeType;
    private String copyFromSlug;
    private LocalDate createdAt;
    private List<MyIngredientDetailResponse> myIngredientDetailResponses;
    private List<MyStepDetailResponse> myStepDetailResponses;
}
