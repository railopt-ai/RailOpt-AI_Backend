package com.railopt.ai.repository.metrics;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.metrics.ScheduleBaselineMetric;

/** Repository interface for {@link ScheduleBaselineMetric}. */
@Repository
public interface ScheduleBaselineMetricRepository extends JpaRepository<ScheduleBaselineMetric, UUID> {
}
