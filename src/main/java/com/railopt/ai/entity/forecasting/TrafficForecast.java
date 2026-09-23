package com.railopt.ai.entity.forecasting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.entity.organization.RailwayDivision;

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


/** Persistence mapping for table `traffic_forecasts`. */
@Entity
@Table(name = "traffic_forecasts")
@Getter
@Setter
@NoArgsConstructor
public class TrafficForecast {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "division_id", nullable = false, columnDefinition = "uuid")
    private RailwayDivision division;

    @Column(name = "forecast_type", columnDefinition = "varchar")
    private String forecastType;

    @Column(name = "forecast_date", columnDefinition = "date")
    private LocalDate forecastDate;

    @Column(name = "horizon_start_at", columnDefinition = "timestamp")
    private LocalDateTime horizonStartAt;

    @Column(name = "horizon_end_at", columnDefinition = "timestamp")
    private LocalDateTime horizonEndAt;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
