package com.railopt.ai.repository.organization;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.organization.Department;

/** Repository interface for {@link Department}. */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
}
