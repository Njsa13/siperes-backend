package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.service.RecipeService;
import com.spiceswap.spiceswap.common.util.Constants;
import com.spiceswap.spiceswap.dto.response.AdminRecipeResponse;
import com.spiceswap.spiceswap.dto.response.RecipeDetailResponse;
import com.spiceswap.spiceswap.dto.response.RecipeInformation;
import com.spiceswap.spiceswap.dto.response.StatusResponse;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Constants.AdminManageRecipe.ADMIN_MANAGE_RECIPE_PATS , produces = "application/json")
@Tag(name = "Manage Recipe For Admin", description = "Manage Recipe For Admin API")
public class AdminManageRecipeController {
    private final RecipeService recipeService;

    @GetMapping
    @Schema(name = "GetAllRecipeRequest", description = "Get All Recipe request body")
    @Operation(summary = "Endpoint untuk memuat dan mencari resep pada bagian admin")
    public ResponseEntity<APIResultResponse<Page<AdminRecipeResponse>>> getAllRecipe(@RequestParam(value = "keyword", required = false) String keyword,
                                                                                     @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 12);
        Page<AdminRecipeResponse> responses = recipeService.getAllRecipeForAdmin(keyword, pageable);
        APIResultResponse<Page<AdminRecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat daftar resep",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/detail/{recipeSlug}")
    @Schema(name = "GetRecipeDetailRequest", description = "Get Recipe Detail request body")
    @Operation(summary = "Endpoint untuk memuat detail resep untuk admin")
    public ResponseEntity<APIResultResponse<RecipeDetailResponse>> getRecipeDetail(@PathVariable String recipeSlug) {
        RecipeDetailResponse response = recipeService.getRecipeDetailForAdmin(recipeSlug);
        APIResultResponse<RecipeDetailResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memuat detail resep",
                response
        );
        return new ResponseEntity<>(resultResponse,HttpStatus.OK);
    }

    @PutMapping("/status/{recipeSlug}")
    @Schema(name = "ChangeStatusRequest", description = "Change Status request body")
    @Operation(summary = "Endpoint untuk mengubah status resep untuk admin")
    public ResponseEntity<APIResultResponse<StatusResponse>> changeRecipeStatus(@PathVariable String recipeSlug) {
        StatusResponse response = recipeService.changeRecipeStatus(recipeSlug);
        APIResultResponse<StatusResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil mengganti status resep",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/info")
    @Schema(name = "GetRecipesInformationRequest", description = "Get Recipes Information request body")
    @Operation(summary = "Endpoint untuk mengambil informasi statistik tentang resep")
    public ResponseEntity<APIResultResponse<RecipeInformation>> getRecipesInformation() {
        RecipeInformation response = recipeService.getRecipeInformation();
        APIResultResponse<RecipeInformation> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memuat informasi tentang resep",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
