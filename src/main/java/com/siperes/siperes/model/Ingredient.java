package com.siperes.siperes.model;

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
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ingredient_id")
    private UUID id;

    @Column(name = "ingredient_name", nullable = false, unique = true)
    private String ingredientName;

    @Column(name = "image_link", nullable = false)
    private String imageLink;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "ingredient", cascade =  {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<IngredientDetail> ingredientDetails;

    @PrePersist
    @PreUpdate
    public void convertToLowerCase() {
        this.ingredientName = this.ingredientName.toLowerCase();
    }
}
