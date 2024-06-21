package com.siperes.siperes.model;

import com.siperes.siperes.enumeration.EnumRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(exclude = "copyDetail")
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

    @ManyToOne
    @JoinColumn(name="copy_detail_id", referencedColumnName="copy_detail_id")
    private CopyDetail copyDetail;
}
