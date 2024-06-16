package com.siperes.siperes.service;

import com.siperes.siperes.dto.request.CreateIngredientRequest;
import com.siperes.siperes.dto.request.UpdateIngredientRequest;
import com.siperes.siperes.dto.response.AdminIngredientResponse;
import com.siperes.siperes.dto.response.IngredientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IngredientService {
    AdminIngredientResponse addNewIngredient(CreateIngredientRequest request);
    AdminIngredientResponse updateIngredient(String ingredientSlug, UpdateIngredientRequest request);
    void deleteIngredient(String ingredientSlug);
    Page<IngredientResponse> getAllIngredient(String keyword, Pageable pageable);
    List<IngredientResponse> getIngredientList();
    Page<AdminIngredientResponse> getAllIngredientForAdmin(String keyword, Pageable pageable);
}
