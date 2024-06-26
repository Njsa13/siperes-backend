package com.siperes.siperes.model;

import com.siperes.siperes.enumeration.EnumRecipeType;
import com.siperes.siperes.enumeration.EnumStatus;
import com.siperes.siperes.enumeration.EnumVisibility;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(of = "recipeSlug")
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

    @Column(name = "thumbnail_image_link", nullable = false)
    private String thumbnailImageLink;

    @Column(nullable = false)
    private Integer portion;

    @Column(name = "total_rating", nullable = false)
    private Double totalRating;

    @Column(name = "total_reviewers", nullable = false)
    private Integer totalReviewers;

    @Column(name = "total_bookmarks", nullable = false)
    private Integer totalBookmarks;

    @Column(name = "popularity_rate", nullable = false)
    private Double popularityRate;

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

    @OneToMany(mappedBy = "recipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<IngredientDetail> ingredientDetails;

    @OneToMany(mappedBy = "recipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Step> steps;

    @OneToMany(mappedBy = "recipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<RecipeHistory> recipeHistories;

    @OneToMany(mappedBy = "recipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<RecipeReview> recipeReviews;

    @OneToMany(mappedBy = "originalRecipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<CopyDetail> originalRecipeCopyDetails;

    @OneToMany(mappedBy = "copyRecipe", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CopyDetail> copyRecipeCopyDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "bookmarks", fetch = FetchType.EAGER)
    private Set<User> bookmarks;

    @PrePersist
    @PreUpdate
    public void convertToLowerCase() {
        this.recipeName = this.recipeName.toLowerCase();
    }
}
