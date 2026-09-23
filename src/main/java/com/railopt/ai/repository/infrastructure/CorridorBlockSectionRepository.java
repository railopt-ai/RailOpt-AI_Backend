package com.railopt.ai.repository.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.infrastructure.CorridorBlockSection;
import com.railopt.ai.entity.infrastructure.CorridorBlockSectionId;

/** Repository interface for {@link CorridorBlockSection}. */
@Repository
public interface CorridorBlockSectionRepository extends JpaRepository<CorridorBlockSection, CorridorBlockSectionId> {
}
