package com.siperes.siperes.repository;

import com.siperes.siperes.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    Optional<Ingredient> findFirstByIngredientName(String ingredientName);
    Optional<Ingredient> findFirstByIngredientSlug(String ingredientSlug);
    Page<Ingredient> findAll(Specification<Ingredient> specification, Pageable pageable);
}
