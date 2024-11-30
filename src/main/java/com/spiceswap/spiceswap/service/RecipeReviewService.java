package com.spiceswap.spiceswap.service;

import com.spiceswap.spiceswap.dto.request.CreateRecipeReviewRequest;
import com.spiceswap.spiceswap.dto.response.RecipeReviewResponse;
import com.spiceswap.spiceswap.dto.response.WriteRecipeReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeReviewService {
    WriteRecipeReviewResponse writeRecipeReview(CreateRecipeReviewRequest request);
    RecipeReviewResponse getWriteRecipeReviewDetail(String recipeSlug);
    void deleteRecipeReview(String slugRecipe);
    Page<RecipeReviewResponse> getAllRecipeReview(String recipeSlug, Pageable pageable);

}
