package com.railopt.ai.repository.conflict;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.conflict.ScheduleConflict;

/** Repository interface for {@link ScheduleConflict}. */
@Repository
public interface ScheduleConflictRepository extends JpaRepository<ScheduleConflict, UUID> {
}
