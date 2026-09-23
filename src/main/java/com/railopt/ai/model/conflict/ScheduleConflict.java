package com.railopt.ai.model.conflict;

import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.model.infrastructure.BlockSection;
import com.railopt.ai.model.infrastructure.TrackSection;
import com.railopt.ai.model.maintenance.MaintenanceResource;
import com.railopt.ai.model.scheduling.ScheduledBlock;
import com.railopt.ai.model.scheduling.ScheduledTask;
import com.railopt.ai.model.scheduling.SchedulingRun;
import com.railopt.ai.model.train.TrainRun;

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


/** Persistence mapping for table `schedule_conflicts`. */
@Entity
@Table(name = "schedule_conflicts")
@Getter
@Setter
@NoArgsConstructor
public class ScheduleConflict {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheduling_run_id", nullable = false, columnDefinition = "uuid")
    private SchedulingRun schedulingRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "scheduled_task_id", nullable = true, columnDefinition = "uuid")
    private ScheduledTask scheduledTask;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "scheduled_block_id", nullable = true, columnDefinition = "uuid")
    private ScheduledBlock scheduledBlock;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "train_run_id", nullable = true, columnDefinition = "uuid")
    private TrainRun trainRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "resource_id", nullable = true, columnDefinition = "uuid")
    private MaintenanceResource resource;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "track_section_id", nullable = true, columnDefinition = "uuid")
    private TrackSection trackSection;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "block_section_id", nullable = true, columnDefinition = "uuid")
    private BlockSection blockSection;

    @Column(name = "conflict_type", columnDefinition = "varchar")
    private String conflictType;

    @Column(name = "severity", columnDefinition = "varchar")
    private String severity;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "conflict_duration_minutes", columnDefinition = "integer")
    private Integer conflictDurationMinutes;

    @Column(name = "conflict_start_at", columnDefinition = "timestamp")
    private LocalDateTime conflictStartAt;

    @Column(name = "conflict_end_at", columnDefinition = "timestamp")
    private LocalDateTime conflictEndAt;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "resolved_at", columnDefinition = "timestamp")
    private LocalDateTime resolvedAt;

}
