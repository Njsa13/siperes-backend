package com.siperes.siperes.model;

import com.siperes.siperes.model.key.CopyDetailKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "copy_details")
public class CopyDetail {
    @EmbeddedId
    private CopyDetailKey id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "copyDetail", cascade =  {CascadeType.REMOVE, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private ModificationRequest modificationRequests;

    @ManyToOne
    @MapsId("originalRecipeId")
    @JoinColumn(name = "original_recipe_id", referencedColumnName = "recipe_id", nullable = false)
    private Recipe originalRecipe;

    @ManyToOne
    @MapsId("copyRecipeId")
    @JoinColumn(name = "copy_recipe_id", referencedColumnName = "recipe_id", nullable = false)
    private Recipe copyRecipe;
}
