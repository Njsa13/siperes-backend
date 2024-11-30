package com.spiceswap.spiceswap.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spiceswap.spiceswap.enumeration.EnumRecipeType;
import com.spiceswap.spiceswap.enumeration.EnumStatus;
import com.spiceswap.spiceswap.enumeration.EnumVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminRecipeResponse {
    private String recipeSlug;
    private String recipeName;
    private String thumbnailImageLink;
    private Double totalRating;
    private Integer totalReviewers;
    private Integer totalBookmarks;
    private EnumVisibility visibility;
    private EnumRecipeType recipeType;
    private EnumStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
