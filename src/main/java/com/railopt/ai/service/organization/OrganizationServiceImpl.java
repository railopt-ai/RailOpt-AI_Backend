package com.railopt.ai.service.organization;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.railopt.ai.exception.BadRequestException;
import com.railopt.ai.exception.ResourceConflictException;
import com.railopt.ai.exception.ResourceNotFoundException;
import com.railopt.ai.model.organization.Department;
import com.railopt.ai.payload.organization.CreateDepartmentRequest;
import com.railopt.ai.payload.organization.DepartmentResponse;
import com.railopt.ai.payload.organization.UpdateDepartmentRequest;
import com.railopt.ai.repository.asset.AssetTypeRepository;
import com.railopt.ai.repository.maintenance.MaintenanceResourceRepository;
import com.railopt.ai.repository.maintenance.MaintenanceTaskRepository;
import com.railopt.ai.repository.organization.DepartmentRepository;
import com.railopt.ai.repository.user.UserDepartmentRepository;

/**
 * Service implementation for the Organization / Department domain.
 * Coordinates persistence, business rules, referential integrity, and mapping for all department operations.
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_INACTIVE = "INACTIVE";

    private final DepartmentRepository departmentRepository;
    private final UserDepartmentRepository userDepartmentRepository;
    private final MaintenanceTaskRepository maintenanceTaskRepository;
    private final MaintenanceResourceRepository maintenanceResourceRepository;
    private final AssetTypeRepository assetTypeRepository;

    public OrganizationServiceImpl(
            DepartmentRepository departmentRepository,
            UserDepartmentRepository userDepartmentRepository,
            MaintenanceTaskRepository maintenanceTaskRepository,
            MaintenanceResourceRepository maintenanceResourceRepository,
            AssetTypeRepository assetTypeRepository
    ) {
        this.departmentRepository = departmentRepository;
        this.userDepartmentRepository = userDepartmentRepository;
        this.maintenanceTaskRepository = maintenanceTaskRepository;
        this.maintenanceResourceRepository = maintenanceResourceRepository;
        this.assetTypeRepository = assetTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> listDepartments(String status, int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be negative: " + page);
        }
        if (size < 1 || size > 100) {
            throw new BadRequestException("Page size must be between 1 and 100: " + size);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departmentPage;

        if (status != null && !status.isBlank()) {
            departmentPage = departmentRepository.findByStatusIgnoreCase(status.trim(), pageable);
        } else {
            departmentPage = departmentRepository.findAll(pageable);
        }

        return departmentPage.getContent().stream()
                .map(this::mapToDepartmentResponse)
                .toList();
    }

    @Override
    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new BadRequestException("Department name is required");
        }

        String trimmedName = request.name().trim();
        if (trimmedName.length() < 2 || trimmedName.length() > 100) {
            throw new BadRequestException("Department name must be between 2 and 100 characters");
        }

        if (departmentRepository.existsByNameIgnoreCase(trimmedName)) {
            throw new ResourceConflictException("Department with name '" + trimmedName + "' already exists");
        }

        String code = generateUniqueDepartmentCode();
        LocalDateTime now = LocalDateTime.now();

        Department department = new Department();
        department.setId(UUID.randomUUID());
        department.setCode(code);
        department.setName(trimmedName);
        department.setStatus(STATUS_ACTIVE);
        department.setCreatedAt(now);
        department.setUpdatedAt(now);

        Department saved = departmentRepository.save(department);
        return mapToDepartmentResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(String departmentId) {
        Department department = findDepartmentByIdOrCode(departmentId);
        return mapToDepartmentResponse(department);
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(String departmentId, UpdateDepartmentRequest request) {
        Department department = findDepartmentByIdOrCode(departmentId);

        if (request != null && request.name() != null) {
            String trimmedName = request.name().trim();
            if (trimmedName.length() < 2 || trimmedName.length() > 100) {
                throw new BadRequestException("Department name must be between 2 and 100 characters");
            }

            Optional<Department> existing = departmentRepository.findByNameIgnoreCase(trimmedName);
            if (existing.isPresent() && !existing.get().getId().equals(department.getId())) {
                throw new ResourceConflictException("Department with name '" + trimmedName + "' already exists");
            }

            department.setName(trimmedName);
        }

        department.setUpdatedAt(LocalDateTime.now());
        Department saved = departmentRepository.save(department);
        return mapToDepartmentResponse(saved);
    }

    @Override
    @Transactional
    public void deactivateDepartment(String departmentId) {
        Department department = findDepartmentByIdOrCode(departmentId);
        UUID deptId = department.getId();

        // 1. Active user department assignments: user_departments.department_id = deptId AND left_at IS NULL
        if (userDepartmentRepository.existsActiveByDepartmentId(deptId)) {
            throw new ResourceConflictException("Department cannot be deactivated because it is referenced by active user assignments");
        }

        // 2. Active maintenance tasks assigned to the department: assigned_department_id = deptId AND status NOT IN ('COMPLETED', 'CANCELLED')
        if (maintenanceTaskRepository.existsActiveTasksByDepartmentId(deptId)) {
            throw new ResourceConflictException("Department cannot be deactivated because it is referenced by active maintenance tasks");
        }

        // 3. Active maintenance resources: department_id = deptId AND status = 'ACTIVE'
        if (maintenanceResourceRepository.existsActiveResourcesByDepartmentId(deptId)) {
            throw new ResourceConflictException("Department cannot be deactivated because it is referenced by active maintenance resources");
        }

        // 4. Active asset types: asset_types.department_id = deptId
        if (assetTypeRepository.existsByDepartmentId(deptId)) {
            throw new ResourceConflictException("Department cannot be deactivated because it is referenced by associated asset types");
        }

        department.setStatus(STATUS_INACTIVE);
        department.setUpdatedAt(LocalDateTime.now());
        departmentRepository.save(department);
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    private Department findDepartmentByIdOrCode(String departmentId) {
        if (departmentId == null || departmentId.isBlank()) {
            throw new BadRequestException("Department ID cannot be blank");
        }

        String trimmed = departmentId.trim();

        // 1. Try parsing as UUID
        try {
            UUID uuid = UUID.fromString(trimmed);
            Optional<Department> dept = departmentRepository.findById(uuid);
            if (dept.isPresent()) {
                return dept.get();
            }
        } catch (IllegalArgumentException ignored) {
            // Not a UUID, proceed to code lookup
        }

        // 2. Search by department code
        return departmentRepository.findByCodeIgnoreCase(trimmed)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with identifier: " + departmentId));
    }

    private String generateUniqueDepartmentCode() {
        for (int i = 0; i < 10; i++) {
            String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            String candidateCode = "DEP-" + suffix;
            if (!departmentRepository.existsByCodeIgnoreCase(candidateCode)) {
                return candidateCode;
            }
        }
        // Fallback to full UUID suffix if collisions occur
        return "DEP-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    private DepartmentResponse mapToDepartmentResponse(Department department) {
        return new DepartmentResponse(
                department.getId().toString(),
                department.getName(),
                department.getStatus()
        );
    }
}
