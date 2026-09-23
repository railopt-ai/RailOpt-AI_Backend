package com.railopt.ai.repository.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.infrastructure.CorridorTrackSection;
import com.railopt.ai.entity.infrastructure.CorridorTrackSectionId;

/** Repository interface for {@link CorridorTrackSection}. */
@Repository
public interface CorridorTrackSectionRepository extends JpaRepository<CorridorTrackSection, CorridorTrackSectionId> {
}
