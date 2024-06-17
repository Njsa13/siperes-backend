package com.siperes.siperes.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.siperes.siperes.enumeration.EnumRecipeType;
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
public class RecipeResponse {
    private String recipeSlug;
    private String recipeName;
    private String thumbnailImageLink;
    private Double totalRating;
    private LocalDate createdAt;
    private Boolean canBookmark;
    private Boolean isBookmarked;
    private EnumRecipeType recipeType;
}
