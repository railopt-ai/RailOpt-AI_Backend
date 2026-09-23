package com.railopt.ai.model.scheduling;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.model.infrastructure.BlockSection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `scheduled_blocks`. */
@Entity
@Table(name = "scheduled_blocks")
@Getter
@Setter
@NoArgsConstructor
public class ScheduledBlock {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheduling_run_id", nullable = false, columnDefinition = "uuid")
    private SchedulingRun schedulingRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheduled_task_id", nullable = false, columnDefinition = "uuid")
    private ScheduledTask scheduledTask;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "block_section_id", nullable = false, columnDefinition = "uuid")
    private BlockSection blockSection;

    @Column(name = "block_code", columnDefinition = "varchar")
    private String blockCode;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "planned_start_at", columnDefinition = "timestamp")
    private LocalDateTime plannedStartAt;

    @Column(name = "planned_end_at", columnDefinition = "timestamp")
    private LocalDateTime plannedEndAt;

    @Column(name = "duration_minutes", columnDefinition = "integer")
    private Integer durationMinutes;

    @Column(name = "full_block", columnDefinition = "boolean")
    private Boolean fullBlock;

    @Column(name = "power_disconnection", columnDefinition = "boolean")
    private Boolean powerDisconnection;

    @Column(name = "train_impact_score", columnDefinition = "numeric")
    private BigDecimal trainImpactScore;

    @Column(name = "asset_impact_score", columnDefinition = "numeric")
    private BigDecimal assetImpactScore;

    @Column(name = "scheduling_reason", columnDefinition = "text")
    private String schedulingReason;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
