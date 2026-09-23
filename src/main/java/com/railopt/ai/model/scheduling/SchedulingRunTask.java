package com.railopt.ai.model.scheduling;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.railopt.ai.model.maintenance.MaintenanceTask;

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


/** Persistence mapping for table `scheduling_run_tasks`. */
@Entity
@Table(name = "scheduling_run_tasks")
@Getter
@Setter
@NoArgsConstructor
public class SchedulingRunTask {

    @EmbeddedId
    private SchedulingRunTaskId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("schedulingRunId")
    @JoinColumn(name = "scheduling_run_id", nullable = false, columnDefinition = "uuid")
    private SchedulingRun schedulingRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("maintenanceTaskId")
    @JoinColumn(name = "maintenance_task_id", nullable = false, columnDefinition = "uuid")
    private MaintenanceTask maintenanceTask;

    @Column(name = "consideration_status", columnDefinition = "varchar")
    private String considerationStatus;

    @Column(name = "unscheduled_reason", columnDefinition = "varchar")
    private String unscheduledReason;

    @Column(name = "predicted_duration_hours", columnDefinition = "numeric")
    private BigDecimal predictedDurationHours;

    @Column(name = "effective_duration_hours", columnDefinition = "numeric")
    private BigDecimal effectiveDurationHours;

    @Column(name = "priority_score", columnDefinition = "numeric")
    private BigDecimal priorityScore;

    @Column(name = "risk_score", columnDefinition = "numeric")
    private BigDecimal riskScore;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

}
