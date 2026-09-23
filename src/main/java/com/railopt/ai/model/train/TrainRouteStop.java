package com.railopt.ai.model.train;

import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.model.infrastructure.Station;

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


/** Persistence mapping for table `train_route_stops`. */
@Entity
@Table(name = "train_route_stops")
@Getter
@Setter
@NoArgsConstructor
public class TrainRouteStop {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_route_id", nullable = false, columnDefinition = "uuid")
    private TrainRoute trainRoute;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false, columnDefinition = "uuid")
    private Station station;

    @Column(name = "sequence_no", columnDefinition = "integer")
    private Integer sequenceNo;

    @Column(name = "arrival_offset_minutes", columnDefinition = "integer")
    private Integer arrivalOffsetMinutes;

    @Column(name = "departure_offset_minutes", columnDefinition = "integer")
    private Integer departureOffsetMinutes;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

}
