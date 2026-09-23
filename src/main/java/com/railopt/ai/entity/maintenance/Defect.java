package com.railopt.ai.entity.maintenance;

import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.entity.asset.Asset;
import com.railopt.ai.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `defects`. */
@Entity
@Table(
        name = "defects",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "defect_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Defect {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id", nullable = false, columnDefinition = "uuid")
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reported_by", nullable = false, columnDefinition = "uuid")
    private User reportedBy;

    @Column(name = "defect_code", nullable = false, unique = true, columnDefinition = "varchar")
    private String defectCode;

    @Column(name = "defect_type", columnDefinition = "varchar")
    private String defectType;

    @Column(name = "severity", columnDefinition = "varchar")
    private String severity;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "detected_at", columnDefinition = "timestamp")
    private LocalDateTime detectedAt;

    @Column(name = "due_at", columnDefinition = "timestamp")
    private LocalDateTime dueAt;

    @Column(name = "resolved_at", columnDefinition = "timestamp")
    private LocalDateTime resolvedAt;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
