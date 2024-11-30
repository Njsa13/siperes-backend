package com.spiceswap.spiceswap.repository;

import com.spiceswap.spiceswap.model.Recipe;
import com.spiceswap.spiceswap.model.RecipeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeHistoryRepository extends JpaRepository<RecipeHistory, UUID> {
    @Modifying
    void deleteByRecipeId(UUID recipeId);

    Page<RecipeHistory> findByRecipe(Recipe recipe, Pageable pageable);

    Optional<RecipeHistory> findFirstByHistorySlug(String historySlug);
}
