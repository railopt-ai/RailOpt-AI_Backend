package com.railopt.ai.model.forecasting;

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


/** Persistence mapping for table `traffic_forecast_items`. */
@Entity
@Table(name = "traffic_forecast_items")
@Getter
@Setter
@NoArgsConstructor
public class TrafficForecastItem {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "forecast_id", nullable = false, columnDefinition = "uuid")
    private TrafficForecast forecast;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "block_section_id", nullable = false, columnDefinition = "uuid")
    private BlockSection blockSection;

    @Column(name = "time_window_start", columnDefinition = "timestamp")
    private LocalDateTime timeWindowStart;

    @Column(name = "time_window_end", columnDefinition = "timestamp")
    private LocalDateTime timeWindowEnd;

    @Column(name = "train_category", columnDefinition = "varchar")
    private String trainCategory;

    @Column(name = "expected_train_count", columnDefinition = "integer")
    private Integer expectedTrainCount;

    @Column(name = "probability", columnDefinition = "numeric")
    private BigDecimal probability;

    @Column(name = "expected_passenger_trains", columnDefinition = "integer")
    private Integer expectedPassengerTrains;

    @Column(name = "expected_goods_trains", columnDefinition = "integer")
    private Integer expectedGoodsTrains;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

}
