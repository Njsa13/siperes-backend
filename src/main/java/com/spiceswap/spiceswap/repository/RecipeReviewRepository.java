package com.spiceswap.spiceswap.repository;

import com.spiceswap.spiceswap.model.RecipeReview;
import com.spiceswap.spiceswap.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeReviewRepository extends JpaRepository<RecipeReview, UUID> {
    @Query("""
            SELECT COUNT(rr) FROM RecipeReview rr
            LEFT JOIN rr.recipe r
            WHERE r.id = :recipeId
            """)
    long countByRecipeId(UUID recipeId);

    @Query("""
            SELECT AVG(rr.rating) FROM RecipeReview rr
            LEFT JOIN rr.recipe r
            WHERE r.id = :recipeId
            """)
    Double averageByRecipeId(UUID recipeId);

    @Modifying
    void deleteByUserIdAndRecipeId(UUID userId, UUID recipeId);

    @Query("""
            SELECT rr FROM RecipeReview rr
            LEFT JOIN rr.recipe r
            WHERE r.recipeSlug = :recipeSlug AND rr.user = :user
            """)
    Optional<RecipeReview> findFirstByUserIdAndRecipeSlug(User user, String recipeSlug);

    Optional<RecipeReview> findFirstByUserIdAndRecipeId(UUID userId, UUID recipeId);

    @Query("""
            SELECT rr FROM RecipeReview rr
            LEFT JOIN rr.recipe r
            WHERE r.recipeSlug = :recipeSlug
            """)
    Page<RecipeReview> findByRecipeSlug(String recipeSlug, Pageable pageable);
}
