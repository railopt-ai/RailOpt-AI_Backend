package com.railopt.ai.repository.forecasting;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.forecasting.TrafficForecast;

/** Repository interface for {@link TrafficForecast}. */
@Repository
public interface TrafficForecastRepository extends JpaRepository<TrafficForecast, UUID> {
}
