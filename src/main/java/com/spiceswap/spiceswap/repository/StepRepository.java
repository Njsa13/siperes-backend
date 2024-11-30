package com.spiceswap.spiceswap.repository;

import com.spiceswap.spiceswap.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StepRepository extends JpaRepository<Step, UUID> {
    Optional<Step> findFirstByStepSlug(String stepSlug);
    @Modifying
    void deleteByRecipeId(UUID recipeId);
}
