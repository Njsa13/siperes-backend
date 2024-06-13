package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.CreateRecipeRequest;
import com.siperes.siperes.dto.request.SetRecipeRequest;
import com.siperes.siperes.dto.request.UpdateRecipeRequest;
import com.siperes.siperes.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeService {
    CreateRecipeResponse addRecipe(CreateRecipeRequest recipeRequest);
    UpdateRecipeResponse updateRecipe(String recipeSlug, UpdateRecipeRequest recipeRequest);
    SetRecipeResponse setRecipe(String recipeSlug, SetRecipeRequest recipeRequest);
    UpdateRecipeResponse getUpdateRecipeDetail(String recipeSlug);
    SetRecipeResponse getSettingRecipeDetail(String recipeSlug);
    void deleteRecipe(String recipeSlug);
    Page<MyRecipeResponse> getAllMyRecipes(Pageable pageable);
    MyRecipeDetailResponse getMyRecipeDetail(String recipeSlug);
    Page<RecipeResponse> getAllBookmarkedRecipes(Pageable pageable);
    MyRecipeHistoryListResponse getMyRecipeHistories(String recipeSlug, Pageable pageable);
    RecipeHistoryDetailResponse getMyRecipeHistoryDetail(String historySlug);
    void createBookmark(String recipeSlug);
    void deleteBookmark(String recipeSlug);
}
