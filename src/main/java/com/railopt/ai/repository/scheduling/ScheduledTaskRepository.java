package com.railopt.ai.repository.scheduling;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.scheduling.ScheduledTask;

/** Repository interface for {@link ScheduledTask}. */
@Repository
public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, UUID> {
}
