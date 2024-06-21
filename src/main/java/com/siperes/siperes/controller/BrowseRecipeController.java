package com.siperes.siperes.controller;

import com.siperes.siperes.dto.response.*;
import com.siperes.siperes.dto.response.base.APIResultResponse;
import com.siperes.siperes.enumeration.EnumSortBy;
import com.siperes.siperes.service.IngredientService;
import com.siperes.siperes.service.RecipeService;
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

import static com.siperes.siperes.common.util.Constants.BrowseRecipe.BROWSE_RECIPE_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = BROWSE_RECIPE_PATS , produces = "application/json")
@Tag(name = "Browse Recipe", description = "Browse Recipe API")
public class BrowseRecipeController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @GetMapping
    @Schema(name = "GetRecipeListRequest", description = "Get Recipe List request body")
    @Operation(summary = "Endpoint untuk memuat daftar resep yang akan ditampilkan di homepage")
    public ResponseEntity<APIResultResponse<List<RecipeResponse>>> getRecipeList() {
        List<RecipeResponse> responses = recipeService.getRecipeList();
        APIResultResponse<List<RecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memuat daftar resep",
                responses
        );
        return new ResponseEntity<>(resultResponse,HttpStatus.OK);
    }

    @GetMapping("/recipe-detail/{recipeSlug}")
    @Schema(name = "GetRecipeDetailRequest", description = "Get Recipe Detail request body")
    @Operation(summary = "Endpoint untuk memuat detail resep")
    public ResponseEntity<APIResultResponse<RecipeDetailResponse>> getRecipeDetail(@PathVariable String recipeSlug) {
        RecipeDetailResponse response = recipeService.getRecipeDetail(recipeSlug);
        APIResultResponse<RecipeDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memuat detail resep",
                response
        );
        return new ResponseEntity<>(resultResponse,HttpStatus.OK);
    }

    @GetMapping("/copied/{recipeSlug}")
    @Schema(name = "GetCopyRecipeListRequest", description = "Get Copy Recipe List request body")
    @Operation(summary = "Endpoint untuk memuat daftar resep salinan dari sebuah resep original")
    public ResponseEntity<APIResultResponse<Page<RecipeResponse>>> getCopyRecipeList(@PathVariable String recipeSlug,
                                                                                     @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<RecipeResponse> responses = recipeService.getCopyRecipeList(recipeSlug, pageable);
        APIResultResponse<Page<RecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memuat daftar resep salinan",
                responses
        );
        return new ResponseEntity<>(resultResponse,HttpStatus.OK);
    }

    @GetMapping("/get-all-recipe-history/{recipeSlug}")
    @Schema(name = "GetAllRecipeHistoryRequest", description = "Get ALl Recipe History request body")
    @Operation(summary = "Endpoint untuk mengambil daftar history resep")
    public ResponseEntity<APIResultResponse<RecipeHistoryListResponse>> getAllRecipeHistory(@PathVariable String recipeSlug,
                                                                                            @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        RecipeHistoryListResponse responses = recipeService.getRecipeHistories(recipeSlug, pageable);
        APIResultResponse<RecipeHistoryListResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat riwayat resep",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-recipe-history-detail/{historySlug}")
    @Schema(name = "GetRecipeHistoryDetailRequest", description = "Get Recipe History Detail request body")
    @Operation(summary = "Endpoint untuk mengambil detail history resep")
    public ResponseEntity<APIResultResponse<RecipeHistoryDetailResponse>> getRecipeHistoryDetail(@PathVariable String historySlug) {
        RecipeHistoryDetailResponse responses = recipeService.getRecipeHistoryDetail(historySlug);
        APIResultResponse<RecipeHistoryDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat detail riwayat resep",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Schema(name = "SearchRecipesRequest", description = "Search Recipes request body")
    @Operation(summary = "Endpoint untuk mencari resep dengan memasukan nama resep atau bahan dan mengurutkannya sesuai kriteria")
    public ResponseEntity<APIResultResponse<Page<RecipeResponse>>> searchOrSortRecipes(@RequestParam(value = "keyword", required = false) String keyword,
                                                                                       @RequestParam(value = "sortBy", required = false) EnumSortBy sortBy,
                                                                                       @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<RecipeResponse> responses = recipeService.searchAndSortingRecipe(keyword, sortBy, pageable);
        APIResultResponse<Page<RecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat daftar resep",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/ingredient")
    @Schema(name = "GetIngredientListRequest", description = "Get Ingredient List request body")
    @Operation(summary = "Endpoint untuk menampilkan daftar bahan pada homepage")
    public ResponseEntity<APIResultResponse<List<IngredientResponse>>> getIngredientList() {
        List<IngredientResponse> responses = ingredientService.getIngredientList();
        APIResultResponse<List<IngredientResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat daftar bahan",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/search-ingredient")
    @Schema(name = "GetAllIngredientRequest", description = "Get All Ingredient request body")
    @Operation(summary = "Endpoint untuk menampilkan dan mencari bahan-bahan")
    public ResponseEntity<APIResultResponse<Page<IngredientResponse>>> getAllIngredient(@RequestParam(value = "keyword", required = false) String keyword,
                                                                                        @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<IngredientResponse> responses = ingredientService.getAllIngredient(keyword, pageable);
        APIResultResponse<Page<IngredientResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat daftar bahan",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
