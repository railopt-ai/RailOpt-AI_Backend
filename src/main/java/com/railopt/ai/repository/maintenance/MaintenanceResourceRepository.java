package com.railopt.ai.repository.maintenance;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.maintenance.MaintenanceResource;

/** Repository interface for {@link MaintenanceResource}. */
@Repository
public interface MaintenanceResourceRepository extends JpaRepository<MaintenanceResource, UUID> {

    @Query("SELECT COUNT(mr) > 0 FROM MaintenanceResource mr WHERE mr.department.id = :departmentId AND UPPER(mr.status) = 'ACTIVE'")
    boolean existsActiveResourcesByDepartmentId(@Param("departmentId") UUID departmentId);
}
