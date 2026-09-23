package com.railopt.ai.repository.scheduling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.scheduling.ScheduledBlockResource;
import com.railopt.ai.entity.scheduling.ScheduledBlockResourceId;

/** Repository interface for {@link ScheduledBlockResource}. */
@Repository
public interface ScheduledBlockResourceRepository extends JpaRepository<ScheduledBlockResource, ScheduledBlockResourceId> {
}
