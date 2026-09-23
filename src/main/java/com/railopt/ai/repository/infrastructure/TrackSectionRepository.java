package com.railopt.ai.repository.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.infrastructure.TrackSection;

/** Repository interface for {@link TrackSection}. */
@Repository
public interface TrackSectionRepository extends JpaRepository<TrackSection, UUID> {
}
