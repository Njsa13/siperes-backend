package com.spiceswap.spiceswap.repository;

import com.spiceswap.spiceswap.enumeration.EnumRecipeType;
import com.spiceswap.spiceswap.enumeration.EnumStatus;
import com.spiceswap.spiceswap.model.CopyDetail;
import com.spiceswap.spiceswap.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CopyDetailRepository extends JpaRepository<CopyDetail, UUID> {
    @Query("""
            SELECT c FROM CopyDetail c
            LEFT JOIN c.copyRecipe cr
            LEFT JOIN c.originalRecipe or
            WHERE cr.user = :user AND cr.recipeSlug = :recipeSlug AND cr.status = :copyStatus AND or.status = :originalStatus
            """)
    Optional<CopyDetail> findByCopyRecipeUserAndSlug(User user, String recipeSlug, EnumStatus copyStatus, EnumStatus originalStatus);

    @Query("""
            SELECT c FROM CopyDetail c
            LEFT JOIN c.copyRecipe cr
            LEFT JOIN c.originalRecipe or
            WHERE cr.user = :user AND cr.recipeType = :recipeType AND cr.status = :copyStatus AND or.status = :originalStatus
            """)
    Page<CopyDetail> findByCopyRecipeUser(User user, EnumRecipeType recipeType, EnumStatus copyStatus, EnumStatus originalStatus, Pageable pageable);
}
