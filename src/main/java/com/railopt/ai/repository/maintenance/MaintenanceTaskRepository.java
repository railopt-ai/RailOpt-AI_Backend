package com.railopt.ai.repository.maintenance;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.maintenance.MaintenanceTask;

/** Repository interface for {@link MaintenanceTask}. */
@Repository
public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, UUID> {

    @Query("SELECT COUNT(mt) > 0 FROM MaintenanceTask mt WHERE mt.assignedDepartment.id = :departmentId AND (mt.status IS NULL OR UPPER(mt.status) NOT IN ('COMPLETED', 'CANCELLED'))")
    boolean existsActiveTasksByDepartmentId(@Param("departmentId") UUID departmentId);
}
