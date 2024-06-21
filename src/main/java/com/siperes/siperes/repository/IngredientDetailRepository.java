package com.siperes.siperes.repository;

import com.siperes.siperes.model.IngredientDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientDetailRepository extends JpaRepository<IngredientDetail, UUID> {
    Optional<IngredientDetail> findFirstByIngredientDetailSlug(String ingredientDetailSlug);
    @Modifying
    void deleteByRecipeId(UUID recipeId);
}
