package com.railopt.ai.repository.scheduling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.scheduling.ScheduledBlockResource;
import com.railopt.ai.model.scheduling.ScheduledBlockResourceId;

/** Repository interface for {@link ScheduledBlockResource}. */
@Repository
public interface ScheduledBlockResourceRepository extends JpaRepository<ScheduledBlockResource, ScheduledBlockResourceId> {
}
