package com.siperes.siperes.repository.specification;

import com.siperes.siperes.model.IngredientDetail;
import com.siperes.siperes.model.Recipe;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecification {
    public static Specification<Recipe> hasRecipeNameOrIngredientName(String keyword) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Recipe, IngredientDetail> ingredientDetailJoin = root.join("ingredientDetails", JoinType.LEFT);
            Predicate recipeNamePredicate = criteriaBuilder.like(root.get("recipeName"), "%" + keyword + "%");
            Predicate ingredientNamePredicate = criteriaBuilder.like(ingredientDetailJoin.get("ingredientName"), "%" + keyword + "%");
            return criteriaBuilder.or(recipeNamePredicate, ingredientNamePredicate);
        };
    }

    public static Specification<Recipe> orderByCreatedAt(boolean asc) {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(asc ? criteriaBuilder.asc(root.get("createdAt")) : criteriaBuilder.desc(root.get("createdAt")));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Recipe> orderByRating() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("totalRating")));
            return criteriaBuilder.conjunction();
        };
    }
}
