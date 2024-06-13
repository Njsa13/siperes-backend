package com.siperes.siperes.controller;

import com.siperes.siperes.dto.request.CreateRecipeRequest;
import com.siperes.siperes.dto.request.SetRecipeRequest;
import com.siperes.siperes.dto.request.UpdateRecipeRequest;
import com.siperes.siperes.dto.response.*;
import com.siperes.siperes.dto.response.base.APIResponse;
import com.siperes.siperes.dto.response.base.APIResultResponse;
import com.siperes.siperes.service.RecipeService;
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

import static com.siperes.siperes.common.util.Constants.ManageRecipe.MANAGE_RECIPE_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = MANAGE_RECIPE_PATS , produces = "application/json")
@Tag(name = "Manage Recipe", description = "Manage Recipe API")
public class ManageRecipeController {
    private final RecipeService recipeService;

    @PostMapping("/add-recipe")
    @Schema(name = "AddRecipeRequest", description = "Add Recipe request body")
    @Operation(summary = "Endpoint untuk membuat resep baru")
    public ResponseEntity<APIResultResponse<CreateRecipeResponse>> addRecipe(@RequestBody @Valid CreateRecipeRequest recipeRequest) {
        CreateRecipeResponse response = recipeService.addRecipe(recipeRequest);
        APIResultResponse<CreateRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Berhasil membuat resep",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update-recipe/{recipeSlug}")
    @Schema(name = "UpdateRecipeRequest", description = "Update Recipe request body")
    @Operation(summary = "Endpoint untuk memperbarui resep")
    public ResponseEntity<APIResultResponse<UpdateRecipeResponse>> updateRecipe(@PathVariable String recipeSlug,
                                                                                @RequestBody @Valid UpdateRecipeRequest recipeRequest) {
        UpdateRecipeResponse response = recipeService.updateRecipe(recipeSlug, recipeRequest);
        APIResultResponse<UpdateRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memperbarui resep",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/update-recipe/{recipeSlug}")
    @Schema(name = "UpdateRecipeRequest", description = "Update Recipe request body")
    @Operation(summary = "Endpoint untuk mengambil data resep sebelum update dilakukan")
    public ResponseEntity<APIResultResponse<UpdateRecipeResponse>> getUpdateRecipeDetail(@PathVariable String recipeSlug) {
        UpdateRecipeResponse response = recipeService.getUpdateRecipeDetail(recipeSlug);
        APIResultResponse<UpdateRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil mengambil data",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PutMapping("/set-recipe/{recipeSlug}")
    @Schema(name = "SetRecipeRequest", description = "Set Recipe request body")
    @Operation(summary = "Endpoint untuk mengubah pengaturan resep")
    public ResponseEntity<APIResultResponse<SetRecipeResponse>> setRecipe(@PathVariable String recipeSlug,
                                                                          @RequestBody @Valid SetRecipeRequest recipeRequest) {
        SetRecipeResponse response = recipeService.setRecipe(recipeSlug, recipeRequest);
        APIResultResponse<SetRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memperbarui pengaturan resep",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/set-recipe/{recipeSlug}")
    @Schema(name = "SetRecipeRequest", description = "Set Recipe request body")
    @Operation(summary = "Endpoint untuk mengambil data resep sebelum perubahan pengaturan dilakukan")
    public ResponseEntity<APIResultResponse<SetRecipeResponse>> getSettingRecipeDetail(@PathVariable String recipeSlug) {
        SetRecipeResponse response = recipeService.getSettingRecipeDetail(recipeSlug);
        APIResultResponse<SetRecipeResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil mengambil data",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete-recipe/{recipeSlug}")
    @Schema(name = "DeleteRecipeRequest", description = "Delete Recipe request body")
    @Operation(summary = "Endpoint untuk menghapus resep")
    public ResponseEntity<APIResponse> deleteRecipe(@PathVariable String recipeSlug) {
        recipeService.deleteRecipe(recipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Berhasil menghapus resep"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/get-all-my-recipes")
    @Schema(name = "GetAllMyRecipesRequest", description = "Get All My Recipes request body")
    @Operation(summary = "Endpoint untuk mengambil daftar resep milik saya")
    public ResponseEntity<APIResultResponse<Page<MyRecipeResponse>>> getAllMyRecipes(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<MyRecipeResponse> responses = recipeService.getAllMyRecipes(pageable);
        APIResultResponse<Page<MyRecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat resep saya",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-my-recipe-detail/{recipeSlug}")
    @Schema(name = "GetMyRecipeDetailRequest", description = "Get My Recipe Detail request body")
    @Operation(summary = "Endpoint untuk mengambil detail resep milik saya")
    public ResponseEntity<APIResultResponse<MyRecipeDetailResponse>> getMyRecipeDetail(@PathVariable String recipeSlug) {
        MyRecipeDetailResponse responses = recipeService.getMyRecipeDetail(recipeSlug);
        APIResultResponse<MyRecipeDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat detail resep saya",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-all-bookmarked-recipes")
    @Schema(name = "GetAllBookmarkedRecipesRequest", description = "Get All Bookmarked Recipes request body")
    @Operation(summary = "Endpoint untuk mengambil daftar resep yang telah di bookmark")
    public ResponseEntity<APIResultResponse<Page<RecipeResponse>>> getAllBookmarkedRecipes(@RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<RecipeResponse> responses = recipeService.getAllBookmarkedRecipes(pageable);
        APIResultResponse<Page<RecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat resep yang telah di bookmark",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-all-my-recipe-history/{recipeSlug}")
    @Schema(name = "GetAllMyRecipeHistoryRequest", description = "Get ALl My Recipe History request body")
    @Operation(summary = "Endpoint untuk mengambil daftar history resep milik sendiri")
    public ResponseEntity<APIResultResponse<MyRecipeHistoryListResponse>> getAllMyRecipeHistory(@PathVariable String recipeSlug,
                                                                                                  @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        MyRecipeHistoryListResponse responses = recipeService.getMyRecipeHistories(recipeSlug, pageable);
        APIResultResponse<MyRecipeHistoryListResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat riwayat resep saya",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/get-my-recipe-history-detail/{historySlug}")
    @Schema(name = "GetMyRecipeHistoryDetailRequest", description = "Get My Recipe History Detail request body")
    @Operation(summary = "Endpoint untuk mengambil detail history resep milik sendiri")
    public ResponseEntity<APIResultResponse<RecipeHistoryDetailResponse>> getMyRecipeHistoryDetail(@PathVariable String historySlug) {
        RecipeHistoryDetailResponse responses = recipeService.getMyRecipeHistoryDetail(historySlug);
        APIResultResponse<RecipeHistoryDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat detail riwayat resep saya",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @PostMapping("/bookmark/{recipeSlug}")
    @Schema(name = "CreateBookmarkRequest", description = "Create Bookmark request body")
    @Operation(summary = "Endpoint untuk membuat atau menambahkan bookmark")
    public ResponseEntity<APIResponse> createBookmark(@PathVariable String recipeSlug) {
        recipeService.createBookmark(recipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.CREATED,
                "Berhasil menambahkan bookmark"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/bookmark/{recipeSlug}")
    @Schema(name = "DeleteBookmarkRequest", description = "Delete Bookmark request body")
    @Operation(summary = "Endpoint untuk menghapus bookmark")
    public ResponseEntity<APIResponse> deleteBookmark(@PathVariable String recipeSlug) {
        recipeService.deleteBookmark(recipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Berhasil menghapus bookmark"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
