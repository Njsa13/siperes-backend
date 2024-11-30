package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.response.*;
import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.enumeration.EnumSortBy;
import com.spiceswap.spiceswap.service.IngredientService;
import com.spiceswap.spiceswap.service.RecipeReviewService;
import com.spiceswap.spiceswap.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.spiceswap.spiceswap.common.util.Constants.BrowseRecipe.BROWSE_RECIPE_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = BROWSE_RECIPE_PATS , produces = "application/json")
@Tag(name = "Browse Recipe", description = "Browse Recipe API")
public class BrowseRecipeController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final RecipeReviewService recipeReviewService;

    @GetMapping
    @Schema(name = "GetRecipeListRequest", description = "Get Recipe List request body")
    @Operation(summary = "Endpoint to load a list of recipes that will be displayed on the homepage")
    public ResponseEntity<APIResultResponse<List<RecipeResponse>>> getRecipeList() {
        List<RecipeResponse> responses = recipeService.getRecipeList();
        APIResultResponse<List<RecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded the recipe list",
                responses
        );
        return new ResponseEntity<>(resultResponse,HttpStatus.OK);
    }

    @GetMapping("/recipe-detail/{recipeSlug}")
    @Schema(name = "GetRecipeDetailRequest", description = "Get Recipe Detail request body")
    @Operation(summary = "Endpoint to load recipe detail")
    public ResponseEntity<APIResultResponse<RecipeDetailResponse>> getRecipeDetail(@PathVariable String recipeSlug) {
        RecipeDetailResponse response = recipeService.getRecipeDetail(recipeSlug);
        APIResultResponse<RecipeDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded recipe detail",
                response
        );
        return new ResponseEntity<>(resultResponse,HttpStatus.OK);
    }

    @GetMapping("/copied/{recipeSlug}")
    @Schema(name = "GetCopyRecipeListRequest", description = "Get Copy Recipe List request body")
    @Operation(summary = "Endpoint to load a list of copied recipes from an original recipe")
    public ResponseEntity<APIResultResponse<Page<RecipeResponse>>> getCopyRecipeList(@PathVariable String recipeSlug,
                                                                                     @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<RecipeResponse> responses = recipeService.getCopyRecipeList(recipeSlug, pageable);
        APIResultResponse<Page<RecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded copy recipe list",
                responses
        );
        return new ResponseEntity<>(resultResponse,HttpStatus.OK);
    }

    @GetMapping("/get-all-recipe-history/{recipeSlug}")
    @Schema(name = "GetAllRecipeHistoryRequest", description = "Get ALl Recipe History request body")
    @Operation(summary = "Endpoint to retrieve a list of recipe history")
    public ResponseEntity<APIResultResponse<RecipeHistoryListResponse>> getAllRecipeHistory(@PathVariable String recipeSlug,
                                                                                            @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        RecipeHistoryListResponse responses = recipeService.getRecipeHistories(recipeSlug, pageable);
        APIResultResponse<RecipeHistoryListResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded recipe history",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-recipe-history-detail/{historySlug}")
    @Schema(name = "GetRecipeHistoryDetailRequest", description = "Get Recipe History Detail request body")
    @Operation(summary = "Endpoint to retrieve recipe history detail")
    public ResponseEntity<APIResultResponse<RecipeHistoryDetailResponse>> getRecipeHistoryDetail(@PathVariable String historySlug) {
        RecipeHistoryDetailResponse responses = recipeService.getRecipeHistoryDetail(historySlug);
        APIResultResponse<RecipeHistoryDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded recipe history detail",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Schema(name = "SearchRecipesRequest", description = "Search Recipes request body")
    @Operation(summary = "Endpoint to search for recipes by entering the name of the recipe or ingredients and sorting them according to criteria")
    public ResponseEntity<APIResultResponse<Page<RecipeResponse>>> searchOrSortRecipes(@RequestParam(value = "keyword", required = false) String keyword,
                                                                                       @RequestParam(value = "sortBy", required = false) EnumSortBy sortBy,
                                                                                       @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<RecipeResponse> responses = recipeService.searchAndSortingRecipe(keyword, sortBy, pageable);
        APIResultResponse<Page<RecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded the recipe list",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/ingredient")
    @Schema(name = "GetIngredientListRequest", description = "Get Ingredient List request body")
    @Operation(summary = "Endpoint to load the list of ingredients on the homepage")
    public ResponseEntity<APIResultResponse<List<IngredientResponse>>> getIngredientList() {
        List<IngredientResponse> responses = ingredientService.getIngredientList();
        APIResultResponse<List<IngredientResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded the ingredient list",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/search-ingredient")
    @Schema(name = "GetAllIngredientRequest", description = "Get All Ingredient request body")
    @Operation(summary = "Endpoint for loading and searching for ingredients")
    public ResponseEntity<APIResultResponse<Page<IngredientResponse>>> getAllIngredient(@RequestParam(value = "keyword", required = false) String keyword,
                                                                                        @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<IngredientResponse> responses = ingredientService.getAllIngredient(keyword, pageable);
        APIResultResponse<Page<IngredientResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded the ingredient list",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/review-list")
    @Schema(name = "GetAllRecipeReviewRequest", description = "Get All Recipe Review request body")
    @Operation(summary = "Endpoint to retrieve a list of recipe reviews")
    public ResponseEntity<APIResultResponse<Page<RecipeReviewResponse>>> getAllRecipeReview(@RequestParam("recipeSlug") String recipeSlug,
                                                                                            @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<RecipeReviewResponse> responses = recipeReviewService.getAllRecipeReview(recipeSlug, pageable);
        APIResultResponse<Page<RecipeReviewResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded recipe review list",
                responses
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
