package com.railopt.ai.repository.train;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.train.TrainRun;

/** Repository interface for {@link TrainRun}. */
@Repository
public interface TrainRunRepository extends JpaRepository<TrainRun, UUID> {
}
