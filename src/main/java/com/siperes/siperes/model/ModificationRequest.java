package com.siperes.siperes.model;

import com.siperes.siperes.enumeration.EnumRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "modification_requests")
public class ModificationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "modification_request_id")
    private UUID id;

    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    private EnumRequestStatus requestStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="original_recipe_id", referencedColumnName="original_recipe_id", nullable = false),
            @JoinColumn(name="copy_recipe_id", referencedColumnName="copy_recipe_id", nullable = false)
    })
    private CopyDetail copyDetail;
}
