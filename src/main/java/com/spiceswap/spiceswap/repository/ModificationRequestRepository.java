package com.spiceswap.spiceswap.repository;


import com.spiceswap.spiceswap.enumeration.EnumRequestStatus;
import com.spiceswap.spiceswap.enumeration.EnumStatus;
import com.spiceswap.spiceswap.model.CopyDetail;
import com.spiceswap.spiceswap.model.ModificationRequest;
import com.spiceswap.spiceswap.model.Recipe;
import com.spiceswap.spiceswap.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModificationRequestRepository extends JpaRepository<ModificationRequest, UUID> {
    @Query("""
           SELECT COUNT(m) > 0 FROM ModificationRequest m
           LEFT JOIN m.copyDetail c
           WHERE m.requestStatus = :requestStatus AND c.originalRecipe = :originalRecipe AND c.copyRecipe = :copyRecipe
           """)
    Boolean existsByStatus(EnumRequestStatus requestStatus, Recipe originalRecipe, Recipe copyRecipe);

    @Query("""
            SELECT m FROM ModificationRequest m
            LEFT JOIN m.copyDetail c
            LEFT JOIN c.copyRecipe cr
            WHERE cr.user = :user
            ORDER BY m.createdAt DESC
            """)
    Page<ModificationRequest> findByUserOfCopyRecipe(User user, Pageable pageable);

    @Query("""
            SELECT m FROM ModificationRequest m
            LEFT JOIN m.copyDetail c
            LEFT JOIN c.originalRecipe o
            WHERE o.user = :user AND m.requestStatus <> :requestStatus
            ORDER BY m.createdAt DESC
            """)
    Page<ModificationRequest> findByUserOfOriginalRecipeAndRequestStatusIsNot(User user, EnumRequestStatus requestStatus, Pageable pageable);

    @Query("""
            SELECT m FROM ModificationRequest m
            LEFT JOIN m.copyDetail c
            LEFT JOIN c.copyRecipe cr
            LEFT JOIN c.originalRecipe or
            WHERE or.user = :user AND cr.recipeSlug = :recipeSlug AND cr.status = :copyStatus AND or.status = :originalStatus AND m.requestStatus = :requestStatus
            """)
    Optional<ModificationRequest> findByCopyRecipeUserAndSlug(User user, String recipeSlug, EnumStatus copyStatus, EnumStatus originalStatus, EnumRequestStatus requestStatus);
    Optional<ModificationRequest> findFirstByRequestStatusAndCopyDetailOrderByCreatedAtDesc(EnumRequestStatus requestStatus, CopyDetail copyDetail);
}
