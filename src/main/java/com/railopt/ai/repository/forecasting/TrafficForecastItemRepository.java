package com.railopt.ai.repository.forecasting;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.forecasting.TrafficForecastItem;

/** Repository interface for {@link TrafficForecastItem}. */
@Repository
public interface TrafficForecastItemRepository extends JpaRepository<TrafficForecastItem, UUID> {
}
