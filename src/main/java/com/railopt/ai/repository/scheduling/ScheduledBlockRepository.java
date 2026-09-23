package com.railopt.ai.repository.scheduling;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.scheduling.ScheduledBlock;

/** Repository interface for {@link ScheduledBlock}. */
@Repository
public interface ScheduledBlockRepository extends JpaRepository<ScheduledBlock, UUID> {
}
