package com.railopt.ai.repository.train;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.train.TrainRunStop;

/** Repository interface for {@link TrainRunStop}. */
@Repository
public interface TrainRunStopRepository extends JpaRepository<TrainRunStop, UUID> {
}
