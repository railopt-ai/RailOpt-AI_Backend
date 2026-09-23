package com.railopt.ai.repository.maintenance;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.maintenance.MaintenanceTask;

/** Repository interface for {@link MaintenanceTask}. */
@Repository
public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, UUID> {
}
