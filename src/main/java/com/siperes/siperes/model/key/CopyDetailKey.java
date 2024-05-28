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
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CopyDetailKey implements Serializable {
    @Column(name = "original_recipe_id")
    private UUID originalRecipeId;

    @Column(name = "copy_recipe_id")
    private UUID copyRecipeId;
}
