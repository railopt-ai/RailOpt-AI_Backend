package com.railopt.ai.repository.train;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.train.TrainRoute;

/** Repository interface for {@link TrainRoute}. */
@Repository
public interface TrainRouteRepository extends JpaRepository<TrainRoute, UUID> {
}
