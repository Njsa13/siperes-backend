package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.request.CreateRecipeRequest;
import com.spiceswap.spiceswap.dto.request.SetRecipeRequest;
import com.spiceswap.spiceswap.dto.request.UpdateRecipeRequest;
import com.spiceswap.spiceswap.dto.response.base.APIResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.service.RecipeService;
import com.spiceswap.spiceswap.dto.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.spiceswap.spiceswap.common.util.Constants.ManageRecipe.MANAGE_RECIPE_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = MANAGE_RECIPE_PATS , produces = "application/json")
@Tag(name = "Manage Recipe", description = "Manage Recipe API")
public class ManageRecipeController {
    private final RecipeService recipeService;

    @PostMapping("/add-recipe")
    @Schema(name = "AddRecipeRequest", description = "Add Recipe request body")
    @Operation(summary = "Endpoint for add new recipe")
    public ResponseEntity<APIResultResponse<CreateRecipeResponse>> addRecipe(@RequestBody @Valid CreateRecipeRequest recipeRequest) {
        CreateRecipeResponse response = recipeService.addRecipe(recipeRequest);
        APIResultResponse<CreateRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Successfully created the recipe",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update-recipe/{recipeSlug}")
    @Schema(name = "UpdateRecipeRequest", description = "Update Recipe request body")
    @Operation(summary = "Endpoint for updating recipe")
    public ResponseEntity<APIResultResponse<UpdateRecipeResponse>> updateRecipe(@PathVariable String recipeSlug,
                                                                                @RequestBody @Valid UpdateRecipeRequest recipeRequest) {
        UpdateRecipeResponse response = recipeService.updateRecipe(recipeSlug, recipeRequest);
        APIResultResponse<UpdateRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully updated recipe",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/update-recipe/{recipeSlug}")
    @Schema(name = "UpdateRecipeRequest", description = "Update Recipe request body")
    @Operation(summary = "Endpoint to retrieve recipe data before updates")
    public ResponseEntity<APIResultResponse<UpdateRecipeResponse>> getUpdateRecipeDetail(@PathVariable String recipeSlug) {
        UpdateRecipeResponse response = recipeService.getUpdateRecipeDetail(recipeSlug);
        APIResultResponse<UpdateRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully retrieved data",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/set-recipe/{recipeSlug}")
    @Schema(name = "SetRecipeRequest", description = "Set Recipe request body")
    @Operation(summary = "Endpoint for changing recipe setting")
    public ResponseEntity<APIResultResponse<SetRecipeResponse>> setRecipe(@PathVariable String recipeSlug,
                                                                          @RequestBody @Valid SetRecipeRequest recipeRequest) {
        SetRecipeResponse response = recipeService.setRecipe(recipeSlug, recipeRequest);
        APIResultResponse<SetRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully updated recipe setting",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/set-recipe/{recipeSlug}")
    @Schema(name = "SetRecipeRequest", description = "Set Recipe request body")
    @Operation(summary = "Endpoint to retrieve recipe data before setting changes")
    public ResponseEntity<APIResultResponse<SetRecipeResponse>> getSettingRecipeDetail(@PathVariable String recipeSlug) {
        SetRecipeResponse response = recipeService.getSettingRecipeDetail(recipeSlug);
        APIResultResponse<SetRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully retrieved data",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete-recipe/{recipeSlug}")
    @Schema(name = "DeleteRecipeRequest", description = "Delete Recipe request body")
    @Operation(summary = "Endpoint to delete a recipe")
    public ResponseEntity<APIResponse> deleteRecipe(@PathVariable String recipeSlug) {
        recipeService.deleteRecipe(recipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Successfully deleted the recipe"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/get-all-my-recipes")
    @Schema(name = "GetAllMyRecipesRequest", description = "Get All My Recipes request body")
    @Operation(summary = "Endpoint to retrieve my list of recipes")
    public ResponseEntity<APIResultResponse<Page<MyRecipeResponse>>> getAllMyRecipes(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<MyRecipeResponse> responses = recipeService.getAllMyRecipes(pageable);
        APIResultResponse<Page<MyRecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded my recipes list",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-my-recipe-detail/{recipeSlug}")
    @Schema(name = "GetMyRecipeDetailRequest", description = "Get My Recipe Detail request body")
    @Operation(summary = "Endpoint to retrieve my recipe detail")
    public ResponseEntity<APIResultResponse<MyRecipeDetailResponse>> getMyRecipeDetail(@PathVariable String recipeSlug) {
        MyRecipeDetailResponse responses = recipeService.getMyRecipeDetail(recipeSlug);
        APIResultResponse<MyRecipeDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded my recipe detail",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-all-bookmarked-recipes")
    @Schema(name = "GetAllBookmarkedRecipesRequest", description = "Get All Bookmarked Recipes request body")
    @Operation(summary = "Endpoint to retrieve a list of bookmarked recipes")
    public ResponseEntity<APIResultResponse<Page<RecipeResponse>>> getAllBookmarkedRecipes(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<RecipeResponse> responses = recipeService.getAllBookmarkedRecipes(pageable);
        APIResultResponse<Page<RecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded bookmarked recipe list",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-all-my-recipe-history/{recipeSlug}")
    @Schema(name = "GetAllMyRecipeHistoryRequest", description = "Get ALl My Recipe History request body")
    @Operation(summary = "Endpoint to retrieve your own recipe history list")
    public ResponseEntity<APIResultResponse<RecipeHistoryListResponse>> getAllMyRecipeHistory(@PathVariable String recipeSlug,
                                                                                              @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        RecipeHistoryListResponse responses = recipeService.getMyRecipeHistories(recipeSlug, pageable);
        APIResultResponse<RecipeHistoryListResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded my recipe history",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-my-recipe-history-detail/{historySlug}")
    @Schema(name = "GetMyRecipeHistoryDetailRequest", description = "Get My Recipe History Detail request body")
    @Operation(summary = "Endpoint to retrieve detail of your own recipe history")
    public ResponseEntity<APIResultResponse<RecipeHistoryDetailResponse>> getMyRecipeHistoryDetail(@PathVariable String historySlug) {
        RecipeHistoryDetailResponse responses = recipeService.getMyRecipeHistoryDetail(historySlug);
        APIResultResponse<RecipeHistoryDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded the detail of my recipe history",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PostMapping("/bookmark/{recipeSlug}")
    @Schema(name = "CreateBookmarkRequest", description = "Create Bookmark request body")
    @Operation(summary = "Endpoint for creating or adding bookmarks")
    public ResponseEntity<APIResponse> createBookmark(@PathVariable String recipeSlug) {
        recipeService.createBookmark(recipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.CREATED,
                "Successfully added bookmark"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/bookmark/{recipeSlug}")
    @Schema(name = "DeleteBookmarkRequest", description = "Delete Bookmark request body")
    @Operation(summary = "Endpoint for deleting bookmark")
    public ResponseEntity<APIResponse> deleteBookmark(@PathVariable String recipeSlug) {
        recipeService.deleteBookmark(recipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Successfully deleted bookmark"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/copy-recipe/{recipeSlug}")
    @Schema(name = "CopyRecipeRequest", description = "Copy Recipe request body")
    @Operation(summary = "Endpoint to create a recipe copy")
    public ResponseEntity<APIResultResponse<CreateRecipeResponse>> copyRecipe(@PathVariable String recipeSlug) {
        CreateRecipeResponse response = recipeService.copyRecipe(recipeSlug);
        APIResultResponse<CreateRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Successfully copied the recipe",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }
}
