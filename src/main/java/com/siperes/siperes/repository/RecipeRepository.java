package com.siperes.siperes.repository;

import com.siperes.siperes.enumeration.EnumRecipeType;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.enumeration.EnumVisibility;
import com.siperes.siperes.model.Recipe;
import com.siperes.siperes.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    Optional<Recipe> findFirstByRecipeSlugAndUserAndStatus(String recipeSlug, User user, EnumStatus status);
    Page<Recipe> findByUserAndStatusOrderByCreatedAtDesc(User user, EnumStatus status, Pageable pageable);
    Page<Recipe> findByBookmarksAndStatusAndVisibility(User bookmarks, EnumStatus status, EnumVisibility visibility, Pageable pageable);
    Optional<Recipe> findFirstByRecipeSlugAndStatus(String recipeSlug, EnumStatus status);
    List<Recipe> findTop12ByStatusAndVisibilityAndRecipeTypeOrderByTotalRatingDesc(EnumStatus status, EnumVisibility visibility, EnumRecipeType recipeType);
    Page<Recipe> findAll(Specification<Recipe> specification, Pageable pageable);
    @Query("""
            SELECT COUNT(r) FROM Recipe r
            LEFT JOIN r.copyRecipeCopyDetails c
            WHERE r.user = :user AND c.originalRecipe = :originalRecipe AND r.status = :status
            """)
    Long countCopyRecipe(User user, Recipe originalRecipe, EnumStatus status);

    @Query("""
            SELECT r FROM Recipe r
            LEFT JOIN r.copyRecipeCopyDetails c
            LEFT JOIN c.originalRecipe o
            WHERE r.status = :status AND r.visibility = :visibility AND r.recipeType = :recipeType And o.recipeSlug = :originalRecipeSlug
            """)
    Page<Recipe> findByStatusAndVisibilityAndRecipeTypeAndOriginalRecipeSlug(EnumStatus status, EnumVisibility visibility, EnumRecipeType recipeType, String originalRecipeSlug, Pageable pageable);
}
