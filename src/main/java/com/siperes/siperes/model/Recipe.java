package com.siperes.siperes.model;

import com.siperes.siperes.enumeration.EnumRecipeType;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.enumeration.EnumVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "recipe_id")
    private UUID id;

    @Column(name = "recipe_slug", nullable = false, unique = true)
    private String recipeSlug;

    @Column(name = "recipe_name", nullable = false)
    private String recipeName;

    @Column(nullable = false, columnDefinition = "text")
    private String about;

    @Column(name = "thumbnail_image_link")
    private String thumbnailImageLink;

    @Column(name = "total_rating", nullable = false)
    private Double totalRating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumVisibility visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipe_type", nullable = false)
    private EnumRecipeType recipeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "recipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<IngredientDetail> ingredientDetails;

    @OneToMany(mappedBy = "recipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Step> steps;

    @OneToMany(mappedBy = "recipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<RecipeHistory> recipeHistories;

    @OneToMany(mappedBy = "recipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<RecipeReview> recipeReviews;

    @OneToMany(mappedBy = "originalRecipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<CopyDetail> originalRecipeCopyDetails;

    @OneToMany(mappedBy = "copyRecipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<CopyDetail> copyRecipeCopyDetails;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "bookmarks")
    private Set<User> bookmarks;
}
