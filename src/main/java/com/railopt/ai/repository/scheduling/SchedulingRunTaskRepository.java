package com.railopt.ai.repository.scheduling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.scheduling.SchedulingRunTask;
import com.railopt.ai.entity.scheduling.SchedulingRunTaskId;

/** Repository interface for {@link SchedulingRunTask}. */
@Repository
public interface SchedulingRunTaskRepository extends JpaRepository<SchedulingRunTask, SchedulingRunTaskId> {
}
