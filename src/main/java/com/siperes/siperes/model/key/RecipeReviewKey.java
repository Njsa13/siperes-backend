package com.siperes.siperes.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class RecipeReviewKey implements Serializable {
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "recipe_id")
    private UUID recipeId;
}
