package com.siperes.siperes.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(exclude = {"recipe", "ingredient"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredient_details")
public class IngredientDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ingredient_detail_id")
    private UUID id;

    @Column(name = "ingredient_detail_slug", nullable = false, unique = true)
    private String ingredientDetailSlug;

    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;

    @Column(nullable = false)
    private String dose;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id")
    private Ingredient ingredient;

    @PrePersist
    @PreUpdate
    public void convertToLowerCase() {
        this.ingredientName = this.ingredientName.toLowerCase();
    }
}
