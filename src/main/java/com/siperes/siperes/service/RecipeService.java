package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.CreateRecipeRequest;
import com.siperes.siperes.dto.request.SetRecipeRequest;
import com.siperes.siperes.dto.request.UpdateRecipeRequest;
import com.siperes.siperes.dto.response.*;
import com.siperes.siperes.enumeration.EnumSortBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
    RecipeHistoryListResponse getMyRecipeHistories(String recipeSlug, Pageable pageable);
    RecipeHistoryDetailResponse getMyRecipeHistoryDetail(String historySlug);
    void createBookmark(String recipeSlug);
    void deleteBookmark(String recipeSlug);
    List<RecipeResponse> getRecipeList();
    RecipeDetailResponse getRecipeDetail(String recipeSlug);
    Page<RecipeResponse> getCopyRecipeList(String recipeSlug, Pageable pageable);
    RecipeHistoryListResponse getRecipeHistories(String recipeSlug, Pageable pageable);
    RecipeHistoryDetailResponse getRecipeHistoryDetail(String historySlug);
    Page<RecipeResponse> searchAndSortingRecipe(String keyword, EnumSortBy sortBy, Pageable pageable);
    CreateRecipeResponse copyRecipe(String recipeSlug);
}
