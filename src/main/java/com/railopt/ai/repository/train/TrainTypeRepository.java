package com.railopt.ai.repository.train;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.train.TrainType;

/** Repository interface for {@link TrainType}. */
@Repository
public interface TrainTypeRepository extends JpaRepository<TrainType, UUID> {
}
