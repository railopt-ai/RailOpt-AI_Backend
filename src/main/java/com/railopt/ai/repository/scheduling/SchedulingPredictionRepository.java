package com.railopt.ai.repository.scheduling;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.scheduling.SchedulingPrediction;

/** Repository interface for {@link SchedulingPrediction}. */
@Repository
public interface SchedulingPredictionRepository extends JpaRepository<SchedulingPrediction, UUID> {
}
