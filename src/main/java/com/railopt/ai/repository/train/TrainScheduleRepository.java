package com.railopt.ai.repository.train;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.train.TrainSchedule;

/** Repository interface for {@link TrainSchedule}. */
@Repository
public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, UUID> {
}
