package com.siperes.siperes.model;

import com.siperes.siperes.model.json.RecipeDetailJson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
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

    @Column(name = "slug_history", unique = true, nullable = false)
    private String historySlug;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "previous_recipe", columnDefinition = "jsonb")
    private RecipeDetailJson previousRecipe;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "current_recipe", columnDefinition = "jsonb")
    private RecipeDetailJson currentRecipe;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id", nullable = false)
    private Recipe recipe;
}
