package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.request.CreateRecipeReviewRequest;
import com.spiceswap.spiceswap.dto.response.RecipeReviewResponse;
import com.spiceswap.spiceswap.dto.response.WriteRecipeReviewResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.service.RecipeReviewService;
import com.spiceswap.spiceswap.common.util.Constants;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Constants.RecipeReview.RECIPE_REVIEW_PATS , produces = "application/json")
@Tag(name = "Recipe Review", description = "Recipe Review API")
public class RecipeReviewController {
    private final RecipeReviewService recipeReviewService;

    @PostMapping
    @Schema(name = "WriteRecipeReviewRequest", description = "Write Recipe Review request body")
    @Operation(summary = "Endpoint for add or update recipe review")
    public ResponseEntity<APIResultResponse<WriteRecipeReviewResponse>> writeRecipeReview(@RequestBody @Valid CreateRecipeReviewRequest request) {
        WriteRecipeReviewResponse response = recipeReviewService.writeRecipeReview(request);
        APIResultResponse<WriteRecipeReviewResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully added recipe review",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping
    @Schema(name = "GetMyRecipeReviewDetailRequest", description = "Get My Recipe Review Detail request body")
    @Operation(summary = "Endpoint to load my recipe review detail")
    public ResponseEntity<APIResultResponse<RecipeReviewResponse>> getMyRecipeReviewDetail(@RequestParam("recipeSlug") String recipeSlug) {
        RecipeReviewResponse response = recipeReviewService.getWriteRecipeReviewDetail(recipeSlug);
        APIResultResponse<RecipeReviewResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded recipe review detail",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @Schema(name = "DeleteRecipeReviewRequest", description = "Delete Recipe Review request body")
    @Operation(summary = "Endpoint to delete my recipe review")
    public ResponseEntity<APIResponse> deleteRecipeReview(@RequestParam("recipeSlug") String recipeSlug) {
        recipeReviewService.deleteRecipeReview(recipeSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Successfully deleted review recipe"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/list")
    @Schema(name = "GetAllRecipeReviewRequest", description = "Get All Recipe Review request body")
    @Operation(summary = "Endpoint to load a list of recipe reviews")
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
