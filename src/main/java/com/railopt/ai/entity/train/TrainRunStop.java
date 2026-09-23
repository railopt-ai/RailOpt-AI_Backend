package com.railopt.ai.entity.train;

import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.entity.infrastructure.Station;

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


/** Persistence mapping for table `train_run_stops`. */
@Entity
@Table(name = "train_run_stops")
@Getter
@Setter
@NoArgsConstructor
public class TrainRunStop {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_run_id", nullable = false, columnDefinition = "uuid")
    private TrainRun trainRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false, columnDefinition = "uuid")
    private Station station;

    @Column(name = "sequence_no", columnDefinition = "integer")
    private Integer sequenceNo;

    @Column(name = "planned_arrival_at", columnDefinition = "timestamp")
    private LocalDateTime plannedArrivalAt;

    @Column(name = "planned_departure_at", columnDefinition = "timestamp")
    private LocalDateTime plannedDepartureAt;

    @Column(name = "actual_arrival_at", columnDefinition = "timestamp")
    private LocalDateTime actualArrivalAt;

    @Column(name = "actual_departure_at", columnDefinition = "timestamp")
    private LocalDateTime actualDepartureAt;

}
