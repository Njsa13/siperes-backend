package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.CreateRecipeReviewRequest;
import com.siperes.siperes.dto.response.RecipeReviewResponse;
import com.siperes.siperes.dto.response.WriteRecipeReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeReviewService {
    WriteRecipeReviewResponse writeRecipeReview(CreateRecipeReviewRequest request);
    RecipeReviewResponse getWriteRecipeReviewDetail(String recipeSlug);
    void deleteRecipeReview(String slugRecipe);
    Page<RecipeReviewResponse> getAllRecipeReview(String recipeSlug, Pageable pageable);

}
