package com.railopt.ai.entity.train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

/** Persistence mapping for table `train_schedules`. */
@Entity
@Table(
        name = "train_schedules",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "schedule_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TrainSchedule {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_id", nullable = false, columnDefinition = "uuid")
    private Train train;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_route_id", nullable = false, columnDefinition = "uuid")
    private TrainRoute trainRoute;

    @Column(name = "schedule_code", nullable = false, unique = true, columnDefinition = "varchar")
    private String scheduleCode;

    @Column(name = "valid_from", columnDefinition = "date")
    private LocalDate validFrom;

    @Column(name = "valid_until", columnDefinition = "date")
    private LocalDate validUntil;

    @Column(name = "operating_days", columnDefinition = "varchar")
    private String operatingDays;

    @Column(name = "departure_time", columnDefinition = "time")
    private LocalTime departureTime;

    @Column(name = "arrival_time", columnDefinition = "time")
    private LocalTime arrivalTime;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
