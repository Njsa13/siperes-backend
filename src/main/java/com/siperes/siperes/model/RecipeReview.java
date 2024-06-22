package com.siperes.siperes.model;

import com.siperes.siperes.model.key.RecipeReviewKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(of = {"key", "rating", "comment"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipe_review")
public class RecipeReview {
    @EmbeddedId
    private RecipeReviewKey key;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String comment;

    @Column(name = "isEdit", nullable = false)
    private Boolean isEdit;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @MapsId("recipeId")
    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id", nullable = false)
    private Recipe recipe;
}
