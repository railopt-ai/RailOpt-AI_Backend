package com.railopt.ai.model.asset;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.model.infrastructure.Station;
import com.railopt.ai.model.infrastructure.TrackSection;

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

/** Persistence mapping for table `assets`. */
@Entity
@Table(
        name = "assets",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Asset {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_type_id", nullable = false, columnDefinition = "uuid")
    private AssetType assetType;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "track_section_id", nullable = true, columnDefinition = "uuid")
    private TrackSection trackSection;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "station_id", nullable = true, columnDefinition = "uuid")
    private Station station;

    @Column(name = "code", nullable = false, unique = true, columnDefinition = "varchar")
    private String code;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @Column(name = "location_km", columnDefinition = "numeric")
    private BigDecimal locationKm;

    @Column(name = "installation_date", columnDefinition = "date")
    private LocalDate installationDate;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "criticality", columnDefinition = "varchar")
    private String criticality;

    @Column(name = "condition_status", columnDefinition = "varchar")
    private String conditionStatus;

    @Column(name = "availability_percentage", columnDefinition = "numeric")
    private BigDecimal availabilityPercentage;

    @Column(name = "last_maintenance_at", columnDefinition = "timestamp")
    private LocalDateTime lastMaintenanceAt;

    @Column(name = "next_maintenance_due_at", columnDefinition = "timestamp")
    private LocalDateTime nextMaintenanceDueAt;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
