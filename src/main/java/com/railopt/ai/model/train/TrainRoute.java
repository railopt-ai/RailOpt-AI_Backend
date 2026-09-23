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
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `train_routes`. */
@Entity
@Table(
        name = "train_routes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "route_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TrainRoute {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_id", nullable = false, columnDefinition = "uuid")
    private Train train;

    @Column(name = "route_code", nullable = false, unique = true, columnDefinition = "varchar")
    private String routeCode;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origin_station_id", nullable = false, columnDefinition = "uuid")
    private Station originStation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_station_id", nullable = false, columnDefinition = "uuid")
    private Station destinationStation;

    @Column(name = "total_stations", columnDefinition = "integer")
    private Integer totalStations;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
