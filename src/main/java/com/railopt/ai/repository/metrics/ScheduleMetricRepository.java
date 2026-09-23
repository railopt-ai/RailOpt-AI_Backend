package com.railopt.ai.repository.metrics;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.metrics.ScheduleMetric;

/** Repository interface for {@link ScheduleMetric}. */
@Repository
public interface ScheduleMetricRepository extends JpaRepository<ScheduleMetric, UUID> {
}
