package com.railopt.ai.repository.scheduling;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.scheduling.SchedulingRun;

/** Repository interface for {@link SchedulingRun}. */
@Repository
public interface SchedulingRunRepository extends JpaRepository<SchedulingRun, UUID> {
}
