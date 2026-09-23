package com.railopt.ai.repository.train;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.train.TrainRouteStop;

/** Repository interface for {@link TrainRouteStop}. */
@Repository
public interface TrainRouteStopRepository extends JpaRepository<TrainRouteStop, UUID> {
}
