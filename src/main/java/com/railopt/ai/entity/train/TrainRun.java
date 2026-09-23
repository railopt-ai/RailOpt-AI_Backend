package com.railopt.ai.entity.train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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

/** Persistence mapping for table `train_runs`. */
@Entity
@Table(
        name = "train_runs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "run_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TrainRun {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_id", nullable = false, columnDefinition = "uuid")
    private Train train;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_route_id", nullable = false, columnDefinition = "uuid")
    private TrainRoute trainRoute;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_schedule_id", nullable = false, columnDefinition = "uuid")
    private TrainSchedule trainSchedule;

    @Column(name = "run_code", nullable = false, unique = true, columnDefinition = "varchar")
    private String runCode;

    @Column(name = "operation_date", columnDefinition = "date")
    private LocalDate operationDate;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "planned_start_at", columnDefinition = "timestamp")
    private LocalDateTime plannedStartAt;

    @Column(name = "planned_end_at", columnDefinition = "timestamp")
    private LocalDateTime plannedEndAt;

    @Column(name = "actual_start_at", columnDefinition = "timestamp")
    private LocalDateTime actualStartAt;

    @Column(name = "actual_end_at", columnDefinition = "timestamp")
    private LocalDateTime actualEndAt;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
