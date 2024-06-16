package com.siperes.siperes.controller;

import com.siperes.siperes.dto.request.CreateIngredientRequest;
import com.siperes.siperes.dto.request.UpdateIngredientRequest;
import com.siperes.siperes.dto.response.AdminIngredientResponse;
import com.siperes.siperes.dto.response.base.APIResponse;
import com.siperes.siperes.dto.response.base.APIResultResponse;
import com.siperes.siperes.service.IngredientService;
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

import static com.siperes.siperes.common.util.Constants.ManageIngredient.MANAGE_INGREDIENT_PATS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = MANAGE_INGREDIENT_PATS , produces = "application/json")
@Tag(name = "Manage Ingredient for Admin", description = "Manage Ingredient API for Admin")
public class ManageIngredientController {
    private final IngredientService ingredientService;

    @PostMapping("/add-ingredient")
    @Schema(name = "AddIngredientRequest", description = "Add Ingredient request body")
    @Operation(summary = "Endpoint untuk menambahkan ingredient baru")
    public ResponseEntity<APIResultResponse<AdminIngredientResponse>> addNewIngredient(@RequestBody @Valid CreateIngredientRequest request) {
        AdminIngredientResponse response = ingredientService.addNewIngredient(request);
        APIResultResponse<AdminIngredientResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.CREATED,
                "Berhasil menambahkan ingredient",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update-ingredient/{ingredientSlug}")
    @Schema(name = "UpdateIngredientRequest", description = "Update Ingredient request body")
    @Operation(summary = "Endpoint untuk memperbarui ingredient")
    public ResponseEntity<APIResultResponse<AdminIngredientResponse>> updateIngredient(@PathVariable String ingredientSlug,
                                                                                       @RequestBody @Valid UpdateIngredientRequest request) {
        AdminIngredientResponse response = ingredientService.updateIngredient(ingredientSlug, request);
        APIResultResponse<AdminIngredientResponse> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Berhasil memperbarui ingredient",
                response
        );
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete-ingredient/{ingredientSlug}")
    @Schema(name = "DeleteIngredientRequest", description = "Delete Ingredient request body")
    @Operation(summary = "Endpoint untuk menghapus ingredient")
    public ResponseEntity<APIResponse> deleteIngredient(@PathVariable String ingredientSlug) {
        ingredientService.deleteIngredient(ingredientSlug);
        APIResponse responseDTO = new APIResponse(
                HttpStatus.OK,
                "Berhasil menghapus ingredient"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/get-ingredient")
    @Schema(name = "GetAllIngredientRequest", description = "Get All Ingredient request body")
    @Operation(summary = "Endpoint untuk menampilkan dan mencari bahan-bahan")
    public ResponseEntity<APIResultResponse<Page<AdminIngredientResponse>>> getAllIngredient(@RequestParam(value = "keyword", required = false) String keyword,
                                                                                             @RequestParam("page") Integer page) {
        Pageable pageable = PageRequest.of(page, 24);
        Page<AdminIngredientResponse> responses = ingredientService.getAllIngredientForAdmin(keyword, pageable);
        APIResultResponse<Page<AdminIngredientResponse>> resultResponse = new APIResultResponse<>(
                HttpStatus.OK,
                "Behasil memuat daftar bahan",
                responses);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}
