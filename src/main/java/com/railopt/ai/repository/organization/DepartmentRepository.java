package com.railopt.ai.repository.organization;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.organization.Department;

/** Repository interface for {@link Department}. */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    Optional<Department> findByCode(String code);

    Optional<Department> findByCodeIgnoreCase(String code);
}
