package com.spiceswap.spiceswap.service;

import com.spiceswap.spiceswap.dto.request.CreateRecipeRequest;
import com.spiceswap.spiceswap.dto.request.SetRecipeRequest;
import com.spiceswap.spiceswap.dto.request.UpdateRecipeRequest;
import com.spiceswap.spiceswap.enumeration.EnumSortBy;
import com.spiceswap.spiceswap.dto.response.*;
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
    Page<RecipeResponse> getOtherUserRecipe(String username, Pageable pageable);
    Page<AdminRecipeResponse> getAllRecipeForAdmin(String keyword, Pageable pageable);
    RecipeDetailResponse getRecipeDetailForAdmin(String recipeSlug);
    StatusResponse changeRecipeStatus(String recipeSlug);
    RecipeInformation getRecipeInformation();
}
