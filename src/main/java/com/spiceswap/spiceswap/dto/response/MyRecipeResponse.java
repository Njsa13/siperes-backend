package com.spiceswap.spiceswap.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spiceswap.spiceswap.enumeration.EnumRecipeType;
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
public class MyRecipeResponse {
    private String recipeSlug;

    private String recipeName;

    private String thumbnailImageLink;

    private Double totalRating;

    private Integer totalReviewers;

    private EnumRecipeType recipeType;

    private LocalDate createdAt;
}
