package com.siperes.siperes.controller;

import com.siperes.siperes.dto.request.CreateRecipeReviewRequest;
import com.siperes.siperes.dto.response.RecipeReviewResponse;
import com.siperes.siperes.dto.response.WriteRecipeReviewResponse;
import com.siperes.siperes.dto.response.base.APIResponse;
import com.siperes.siperes.dto.response.base.APIResultResponse;
import com.siperes.siperes.service.RecipeReviewService;
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

import static com.siperes.siperes.common.util.Constants.RecipeReview.RECIPE_REVIEW_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = RECIPE_REVIEW_PATS , produces = "application/json")
@Tag(name = "Recipe Review", description = "Recipe Review API")
public class RecipeReviewController {
    private final RecipeReviewService recipeReviewService;

    @PostMapping
    @Schema(name = "WriteRecipeReviewRequest", description = "Write Recipe Review request body")
    @Operation(summary = "Endpoint untuk memberikan atau mengedit review resep")
    public ResponseEntity<APIResultResponse<WriteRecipeReviewResponse>> writeRecipeReview(@RequestBody @Valid CreateRecipeReviewRequest request) {
        WriteRecipeReviewResponse response = recipeReviewService.writeRecipeReview(request);
        APIResultResponse<WriteRecipeReviewResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil menambahkan ulasan resep",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping
    @Schema(name = "GetMyRecipeReviewDetailRequest", description = "Get My Recipe Review Detail request body")
    @Operation(summary = "Endpoint untuk mengambil detail review resep milik saya")
    public ResponseEntity<APIResultResponse<RecipeReviewResponse>> getMyRecipeReviewDetail(@RequestParam("recipeSlug") String recipeSlug) {
        RecipeReviewResponse response = recipeReviewService.getWriteRecipeReviewDetail(recipeSlug);
        APIResultResponse<RecipeReviewResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memuat detail ulasan resep",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @Schema(name = "DeleteRecipeReviewRequest", description = "Delete Recipe Review request body")
    @Operation(summary = "Endpoint untuk menghapus review resep milik saya")
    public ResponseEntity<APIResponse> deleteRecipeReview(@RequestParam("recipeSlug") String recipeSlug) {
        recipeReviewService.deleteRecipeReview(recipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Berhasil menghapus resep review"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/list")
    @Schema(name = "GetAllRecipeReviewRequest", description = "Get All Recipe Review request body")
    @Operation(summary = "Endpoint untuk mengambil daftar review resep")
    public ResponseEntity<APIResultResponse<Page<RecipeReviewResponse>>> getAllRecipeReview(@RequestParam("recipeSlug") String recipeSlug,
                                                                                            @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<RecipeReviewResponse> responses = recipeReviewService.getAllRecipeReview(recipeSlug, pageable);
        APIResultResponse<Page<RecipeReviewResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memuat daftar ulasan resep",
                responses
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
