package com.siperes.siperes.repository.specification;

import com.siperes.siperes.model.Ingredient;
import org.springframework.data.jpa.domain.Specification;

public class IngredientSpecification {
    public static Specification<Ingredient> ingredientNameContains(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("ingredientName")), likePattern);
        };
    }
}
