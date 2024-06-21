package com.siperes.siperes.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(exclude = {"modificationRequests", "originalRecipe", "copyRecipe"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "copy_details")
public class CopyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "copy_detail_id")
    private UUID id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "copyDetail", cascade =  CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<ModificationRequest> modificationRequests;

    @ManyToOne
    @JoinColumn(name = "original_recipe_id", referencedColumnName = "recipe_id", nullable = false)
    private Recipe originalRecipe;

    @ManyToOne
    @JoinColumn(name = "copy_recipe_id", referencedColumnName = "recipe_id", nullable = false)
    private Recipe copyRecipe;
}
