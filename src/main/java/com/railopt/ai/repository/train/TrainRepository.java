package com.railopt.ai.repository.train;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.train.Train;

/** Repository interface for {@link Train}. */
@Repository
public interface TrainRepository extends JpaRepository<Train, UUID> {
}
