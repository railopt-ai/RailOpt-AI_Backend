package com.railopt.ai.model.maintenance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.railopt.ai.model.infrastructure.BlockSection;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `maintenance_task_block_requirements`. */
@Entity
@Table(name = "maintenance_task_block_requirements")
@Getter
@Setter
@NoArgsConstructor
public class MaintenanceTaskBlockRequirement {

    @EmbeddedId
    private MaintenanceTaskBlockRequirementId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("maintenanceTaskId")
    @JoinColumn(name = "maintenance_task_id", nullable = false, columnDefinition = "uuid")
    private MaintenanceTask maintenanceTask;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("blockSectionId")
    @JoinColumn(name = "block_section_id", nullable = false, columnDefinition = "uuid")
    private BlockSection blockSection;

    @Column(name = "requirement_type", columnDefinition = "varchar")
    private String requirementType;

    @Column(name = "requires_full_block", columnDefinition = "boolean")
    private Boolean requiresFullBlock;

    @Column(name = "requires_power_block", columnDefinition = "boolean")
    private Boolean requiresPowerBlock;

    @Column(name = "requires_signal_block", columnDefinition = "boolean")
    private Boolean requiresSignalBlock;

    @Column(name = "requires_speed_restriction", columnDefinition = "boolean")
    private Boolean requiresSpeedRestriction;

    @Column(name = "safety_buffer_minutes", columnDefinition = "integer")
    private Integer safetyBufferMinutes;

    @Column(name = "minimum_duration_hours", columnDefinition = "numeric")
    private BigDecimal minimumDurationHours;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

}
