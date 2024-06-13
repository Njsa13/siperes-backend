package com.siperes.siperes.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(exclude = "recipe")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "steps")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "step_id")
    private UUID id;

    @Column(name = "step_slug", nullable = false, unique = true)
    private String stepSlug;

    @Column(name = "number_step", nullable = false)
    private Integer numberStep;

    @Column(name = "step_description", nullable = false, columnDefinition = "text")
    private String stepDescription;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id", nullable = false)
    private Recipe recipe;
}
