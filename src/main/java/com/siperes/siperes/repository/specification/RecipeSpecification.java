package com.siperes.siperes.repository.specification;

import com.siperes.siperes.enumeration.EnumRecipeType;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.enumeration.EnumVisibility;
import com.siperes.siperes.model.IngredientDetail;
import com.siperes.siperes.model.Recipe;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecification {
    public static Specification<Recipe> hasRecipeNameOrIngredientNameWithEnum(String keyword) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate recipeNamePredicate = criteriaBuilder.conjunction();
            Predicate ingredientNamePredicate = criteriaBuilder.conjunction();

            if (keyword != null && !keyword.isEmpty()) {
                Join<Recipe, IngredientDetail> ingredientDetailJoin = root.join("ingredientDetails", JoinType.LEFT);
                recipeNamePredicate = criteriaBuilder.like(root.get("recipeName"), "%" + keyword.toLowerCase() + "%");
                ingredientNamePredicate = criteriaBuilder.like(ingredientDetailJoin.get("ingredientName"), "%" + keyword.toLowerCase() + "%");
            }

            Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), EnumStatus.ACTIVE);
            Predicate visibilityPredicate = criteriaBuilder.equal(root.get("visibility"), EnumVisibility.PUBLIC);
            Predicate typePredicate = criteriaBuilder.equal(root.get("recipeType"), EnumRecipeType.ORIGINAL);

            return criteriaBuilder.and(
                    criteriaBuilder.or(recipeNamePredicate, ingredientNamePredicate),
                    statusPredicate,
                    visibilityPredicate,
                    typePredicate
            );
        };
    }

    public static Specification<Recipe> orderByCreatedAt(boolean asc) {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(asc ? criteriaBuilder.asc(root.get("createdAt")) : criteriaBuilder.desc(root.get("createdAt")));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Recipe> orderByPopularity() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("popularityRate")));
            return criteriaBuilder.conjunction();
        };
    }

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
}
