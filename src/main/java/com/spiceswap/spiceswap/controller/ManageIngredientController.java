package com.spiceswap.spiceswap.controller;

import com.spiceswap.spiceswap.dto.request.CreateIngredientRequest;
import com.spiceswap.spiceswap.dto.request.UpdateIngredientRequest;
import com.spiceswap.spiceswap.dto.response.AdminIngredientResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResponse;
import com.spiceswap.spiceswap.dto.response.base.APIResultResponse;
import com.spiceswap.spiceswap.service.IngredientService;
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
@RequestMapping(value = Constants.ManageIngredient.MANAGE_INGREDIENT_PATS , produces = "application/json")
@Tag(name = "Manage Ingredient for Admin", description = "Manage Ingredient API for Admin")
public class ManageIngredientController {
    private final IngredientService ingredientService;

    @PostMapping("/add-ingredient")
    @Schema(name = "AddIngredientRequest", description = "Add Ingredient request body")
    @Operation(summary = "Endpoint for adding new ingredient")
    public ResponseEntity<APIResultResponse<AdminIngredientResponse>> addNewIngredient(@RequestBody @Valid CreateIngredientRequest request) {
        AdminIngredientResponse response = ingredientService.addNewIngredient(request);
        APIResultResponse<AdminIngredientResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Successfully added ingredient",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update-ingredient/{ingredientSlug}")
    @Schema(name = "UpdateIngredientRequest", description = "Update Ingredient request body")
    @Operation(summary = "Endpoint for updating ingredient")
    public ResponseEntity<APIResultResponse<AdminIngredientResponse>> updateIngredient(@PathVariable String ingredientSlug,
                                                                                       @RequestBody @Valid UpdateIngredientRequest request) {
        AdminIngredientResponse response = ingredientService.updateIngredient(ingredientSlug, request);
        APIResultResponse<AdminIngredientResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully updated ingredient",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete-ingredient/{ingredientSlug}")
    @Schema(name = "DeleteIngredientRequest", description = "Delete Ingredient request body")
    @Operation(summary = "Endpoint to delete ingredient")
    public ResponseEntity<APIResponse> deleteIngredient(@PathVariable String ingredientSlug) {
        ingredientService.deleteIngredient(ingredientSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Successfully deleted ingredient"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/get-ingredient")
    @Schema(name = "GetAllIngredientRequest", description = "Get All Ingredient request body")
    @Operation(summary = "Endpoint for loading and searching for ingredients")
    public ResponseEntity<APIResultResponse<Page<AdminIngredientResponse>>> getAllIngredient(@RequestParam(value = "keyword", required = false) String keyword,
                                                                                             @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 24);
        Page<AdminIngredientResponse> responses = ingredientService.getAllIngredientForAdmin(keyword, pageable);
        APIResultResponse<Page<AdminIngredientResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Successfully loaded the ingredients list",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
