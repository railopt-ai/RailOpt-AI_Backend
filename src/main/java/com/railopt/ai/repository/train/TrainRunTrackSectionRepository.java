package com.railopt.ai.repository.train;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.train.TrainRunTrackSection;
import com.railopt.ai.entity.train.TrainRunTrackSectionId;

/** Repository interface for {@link TrainRunTrackSection}. */
@Repository
public interface TrainRunTrackSectionRepository extends JpaRepository<TrainRunTrackSection, TrainRunTrackSectionId> {
}
