package com.siperes.siperes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipe_histories")
public class RecipeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "recipe_history_id")
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ingredient_details", columnDefinition = "jsonb")
    private List<IngredientDetail> ingredientDetails;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "steps", columnDefinition = "jsonb")
    private List<Step> steps;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id", nullable = false)
    private Recipe recipe;
}
