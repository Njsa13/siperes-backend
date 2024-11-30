package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.response.RecipeResponse;
import com.spiceswap.spiceswap.dto.response.UserResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.service.RecipeService;
import com.spiceswap.spiceswap.service.UserService;
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

import static com.spiceswap.spiceswap.common.util.Constants.OtherUserProfile.OTHER_USER_PROFILE_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = OTHER_USER_PROFILE_PATS, produces = "application/json")
@Tag(name = "Other User Profile", description = "Other User Profile API")
public class OtherUserController {
    private final UserService userService;
    private final RecipeService recipeService;

    @GetMapping("/{username}")
    @Schema(name = "GetOtherUserProfileRequest", description = "Get Other User Profile request body")
    @Operation(summary = "Endpoint untuk menampilkan data profile user lain")
    public ResponseEntity<APIResultResponse<UserResponse>> getOtherUserProfile(@PathVariable String username) {
        UserResponse response = userService.getOtherUserProfile(username);
        APIResultResponse<UserResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat data user",
                response);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @GetMapping("/{username}/recipe")
    @Schema(name = "GetOtherUserRecipeRequest", description = "Get Other User Recipe request body")
    @Operation(summary = "Endpoint untuk menampilkan data resep milik user lain")
    public ResponseEntity<APIResultResponse<Page<RecipeResponse>>> getOtherUserRecipe(@PathVariable String username,
                                                                                      @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        Page<RecipeResponse> responses = recipeService.getOtherUserRecipe(username, pageable);
        APIResultResponse<Page<RecipeResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat resep",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
