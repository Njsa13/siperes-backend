package com.siperes.siperes.repository;

import com.siperes.siperes.enumeration.EnumRecipeType;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.enumeration.EnumVisibility;
import com.siperes.siperes.model.Recipe;
import com.siperes.siperes.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    Optional<Recipe> findFirstByRecipeSlugAndUserAndStatus(String recipeSlug, User user, EnumStatus status);
    Page<Recipe> findByUserAndStatusOrderByCreatedAtDesc(User user, EnumStatus status, Pageable pageable);
    Page<Recipe> findByBookmarksAndStatusAndVisibility(User bookmarks, EnumStatus status, EnumVisibility visibility, Pageable pageable);
    Optional<Recipe> findFirstByRecipeSlugAndStatusAndVisibility(String recipeSlug, EnumStatus status, EnumVisibility visibility);
    List<Recipe> findTop12ByStatusAndVisibilityAndRecipeTypeOrderByTotalRatingDesc(EnumStatus status, EnumVisibility visibility, EnumRecipeType recipeType);
}
