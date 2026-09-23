package com.railopt.ai.repository.train;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.train.TrainRunBlockSection;
import com.railopt.ai.entity.train.TrainRunBlockSectionId;

/** Repository interface for {@link TrainRunBlockSection}. */
@Repository
public interface TrainRunBlockSectionRepository extends JpaRepository<TrainRunBlockSection, TrainRunBlockSectionId> {
}
