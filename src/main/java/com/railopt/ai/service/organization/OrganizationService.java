package com.railopt.ai.service.organization;

import java.util.List;

import com.railopt.ai.payload.organization.CreateDepartmentRequest;
import com.railopt.ai.payload.organization.DepartmentResponse;
import com.railopt.ai.payload.organization.UpdateDepartmentRequest;

/**
 * Service interface for Organization / Department domain operations.
 * Fulfills business logic for the Organization module as defined in docs/api.yaml.
 */
public interface OrganizationService {

    /**
     * Lists departments with optional status filtering and pagination.
     * Corresponds to GET /department.
     *
     * @param status Optional status filter (e.g. ACTIVE, INACTIVE)
     * @param page Zero-based page number
     * @param size Number of departments per page (1 to 100)
     * @return List of department responses
     */
    List<DepartmentResponse> listDepartments(String status, int page, int size);

    /**
     * Creates a new department with an auto-generated unique DEP-<suffix> code.
     * Corresponds to POST /department.
     *
     * @param request Department creation payload
     * @return Created department details
     */
    DepartmentResponse createDepartment(CreateDepartmentRequest request);

    /**
     * Retrieves department details by UUID or department code.
     * Corresponds to GET /department/{departmentId}.
     *
     * @param departmentId Department identifier (UUID or department code)
     * @return Department details
     */
    DepartmentResponse getDepartmentById(String departmentId);

    /**
     * Updates editable department information.
     * Corresponds to PATCH /department/{departmentId}.
     *
     * @param departmentId Department identifier (UUID or department code)
     * @param request Department update payload
     * @return Updated department details
     */
    DepartmentResponse updateDepartment(String departmentId, UpdateDepartmentRequest request);

    /**
     * Deactivates a department (sets status to INACTIVE) if no active records reference it.
     * Corresponds to DELETE /department/{departmentId}.
     *
     * @param departmentId Department identifier (UUID or department code)
     */
    void deactivateDepartment(String departmentId);
}
