package com.railopt.ai.service.organization;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserDepartmentRepository userDepartmentRepository;

    @Mock
    private MaintenanceTaskRepository maintenanceTaskRepository;

    @Mock
    private MaintenanceResourceRepository maintenanceResourceRepository;

    @Mock
    private AssetTypeRepository assetTypeRepository;

    private OrganizationServiceImpl organizationService;

    private Department sampleDepartment;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        organizationService = new OrganizationServiceImpl(
                departmentRepository,
                userDepartmentRepository,
                maintenanceTaskRepository,
                maintenanceResourceRepository,
                assetTypeRepository
        );

        sampleId = UUID.randomUUID();
        sampleDepartment = new Department();
        sampleDepartment.setId(sampleId);
        sampleDepartment.setCode("DEP-TEST01");
        sampleDepartment.setName("Track Maintenance");
        sampleDepartment.setStatus("ACTIVE");
        sampleDepartment.setCreatedAt(LocalDateTime.now().minusDays(5));
        sampleDepartment.setUpdatedAt(LocalDateTime.now().minusDays(1));
    }

    // =========================================================================
    // 1. Listing (GET /department)
    // =========================================================================

    @Test
    @DisplayName("listDepartments: should return all departments paginated when status is omitted")
    void listDepartments_success_noFilter() {
        Page<Department> pageResult = new PageImpl<>(List.of(sampleDepartment));
        when(departmentRepository.findAll(any(Pageable.class))).thenReturn(pageResult);

        List<DepartmentResponse> responses = organizationService.listDepartments(null, 0, 20);

        assertThat(responses).hasSize(1);
        DepartmentResponse response = responses.get(0);
        assertThat(response.id()).isEqualTo(sampleId.toString());
        assertThat(response.name()).isEqualTo("Track Maintenance");
        assertThat(response.status()).isEqualTo("ACTIVE");
        verify(departmentRepository).findAll(PageRequest.of(0, 20));
        verify(departmentRepository, never()).findByStatusIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("listDepartments: should filter by status when provided")
    void listDepartments_success_withStatusFilter() {
        Page<Department> pageResult = new PageImpl<>(List.of(sampleDepartment));
        when(departmentRepository.findByStatusIgnoreCase(eq("ACTIVE"), any(Pageable.class))).thenReturn(pageResult);

        List<DepartmentResponse> responses = organizationService.listDepartments("ACTIVE", 0, 10);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).status()).isEqualTo("ACTIVE");
        verify(departmentRepository).findByStatusIgnoreCase("ACTIVE", PageRequest.of(0, 10));
        verify(departmentRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("listDepartments: should throw BadRequestException when page is negative")
    void listDepartments_negativePage_throwsBadRequest() {
        assertThatThrownBy(() -> organizationService.listDepartments(null, -1, 20))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Page number cannot be negative");
    }

    @Test
    @DisplayName("listDepartments: should throw BadRequestException when size is less than 1")
    void listDepartments_sizeLessThanOne_throwsBadRequest() {
        assertThatThrownBy(() -> organizationService.listDepartments(null, 0, 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Page size must be between 1 and 100");
    }

    @Test
    @DisplayName("listDepartments: should throw BadRequestException when size exceeds 100")
    void listDepartments_sizeExceeds100_throwsBadRequest() {
        assertThatThrownBy(() -> organizationService.listDepartments(null, 0, 101))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Page size must be between 1 and 100");
    }

    // =========================================================================
    // 2. Retrieval (GET /department/{departmentId})
    // =========================================================================

    @Test
    @DisplayName("getDepartmentById: should resolve by valid UUID string")
    void getDepartmentById_uuid_success() {
        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));

        DepartmentResponse response = organizationService.getDepartmentById(sampleId.toString());

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(sampleId.toString());
        assertThat(response.name()).isEqualTo("Track Maintenance");
        assertThat(response.status()).isEqualTo("ACTIVE");
        verify(departmentRepository).findById(sampleId);
        verify(departmentRepository, never()).findByCodeIgnoreCase(anyString());
    }

    @Test
    @DisplayName("getDepartmentById: should resolve by department code when not a UUID")
    void getDepartmentById_code_success() {
        when(departmentRepository.findByCodeIgnoreCase("DEP-TEST01")).thenReturn(Optional.of(sampleDepartment));

        DepartmentResponse response = organizationService.getDepartmentById("DEP-TEST01");

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(sampleId.toString());
        assertThat(response.name()).isEqualTo("Track Maintenance");
        verify(departmentRepository).findByCodeIgnoreCase("DEP-TEST01");
    }

    @Test
    @DisplayName("getDepartmentById: should throw ResourceNotFoundException when neither UUID nor code resolves")
    void getDepartmentById_notFound_throwsException() {
        when(departmentRepository.findByCodeIgnoreCase("NONEXISTENT")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> organizationService.getDepartmentById("NONEXISTENT"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found with identifier: NONEXISTENT");
    }

    @Test
    @DisplayName("getDepartmentById: should throw BadRequestException when departmentId is null or blank")
    void getDepartmentById_blankId_throwsBadRequest() {
        assertThatThrownBy(() -> organizationService.getDepartmentById(""))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Department ID cannot be blank");

        assertThatThrownBy(() -> organizationService.getDepartmentById("   "))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Department ID cannot be blank");

        assertThatThrownBy(() -> organizationService.getDepartmentById(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Department ID cannot be blank");
    }

    // =========================================================================
    // 3. Creation (POST /department)
    // =========================================================================

    @Test
    @DisplayName("createDepartment: should create department with trimmed name, unique generated code, ACTIVE status, and timestamps")
    void createDepartment_success() {
        when(departmentRepository.existsByNameIgnoreCase("Signal & Telecom")).thenReturn(false);
        when(departmentRepository.existsByCodeIgnoreCase(anyString())).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

        CreateDepartmentRequest request = new CreateDepartmentRequest("  Signal & Telecom  ");
        DepartmentResponse response = organizationService.createDepartment(request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Signal & Telecom");
        assertThat(response.status()).isEqualTo("ACTIVE");
        assertThat(response.id()).isNotBlank();

        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    @DisplayName("createDepartment: should retry and generate unique code if initial code collides")
    void createDepartment_codeCollision_retriesUntilUnique() {
        when(departmentRepository.existsByNameIgnoreCase("Electrical OHE")).thenReturn(false);
        // First code check reports collision, second check reports available
        when(departmentRepository.existsByCodeIgnoreCase(anyString()))
                .thenReturn(true)
                .thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

        CreateDepartmentRequest request = new CreateDepartmentRequest("Electrical OHE");
        DepartmentResponse response = organizationService.createDepartment(request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Electrical OHE");
        verify(departmentRepository, times(2)).existsByCodeIgnoreCase(anyString());
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    @DisplayName("createDepartment: should throw ResourceConflictException when name already exists (case-insensitive)")
    void createDepartment_duplicateName_throwsConflict() {
        when(departmentRepository.existsByNameIgnoreCase("track maintenance")).thenReturn(true);

        CreateDepartmentRequest request = new CreateDepartmentRequest("track maintenance");

        assertThatThrownBy(() -> organizationService.createDepartment(request))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("already exists");

        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    @DisplayName("createDepartment: should throw BadRequestException when request or name is null or blank")
    void createDepartment_blankName_throwsBadRequest() {
        assertThatThrownBy(() -> organizationService.createDepartment(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Department name is required");

        assertThatThrownBy(() -> organizationService.createDepartment(new CreateDepartmentRequest(null)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Department name is required");

        assertThatThrownBy(() -> organizationService.createDepartment(new CreateDepartmentRequest("   ")))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Department name is required");
    }

    @Test
    @DisplayName("createDepartment: should throw BadRequestException when name is shorter than 2 characters")
    void createDepartment_nameTooShort_throwsBadRequest() {
        assertThatThrownBy(() -> organizationService.createDepartment(new CreateDepartmentRequest("A")))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("between 2 and 100 characters");
    }

    @Test
    @DisplayName("createDepartment: should throw BadRequestException when name exceeds 100 characters")
    void createDepartment_nameTooLong_throwsBadRequest() {
        String longName = "A".repeat(101);
        assertThatThrownBy(() -> organizationService.createDepartment(new CreateDepartmentRequest(longName)))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("between 2 and 100 characters");
    }

    // =========================================================================
    // 4. Update (PATCH /department/{departmentId})
    // =========================================================================

    @Test
    @DisplayName("updateDepartment: should successfully update department name by UUID lookup")
    void updateDepartment_uuid_success() {
        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));
        when(departmentRepository.findByNameIgnoreCase("Permanent Way Maintenance")).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateDepartmentRequest request = new UpdateDepartmentRequest("Permanent Way Maintenance");
        DepartmentResponse response = organizationService.updateDepartment(sampleId.toString(), request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Permanent Way Maintenance");
        assertThat(sampleDepartment.getName()).isEqualTo("Permanent Way Maintenance");
        verify(departmentRepository).save(sampleDepartment);
    }

    @Test
    @DisplayName("updateDepartment: should successfully update department name by code lookup")
    void updateDepartment_code_success() {
        when(departmentRepository.findByCodeIgnoreCase("DEP-TEST01")).thenReturn(Optional.of(sampleDepartment));
        when(departmentRepository.findByNameIgnoreCase("Civil Engineering")).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateDepartmentRequest request = new UpdateDepartmentRequest("Civil Engineering");
        DepartmentResponse response = organizationService.updateDepartment("DEP-TEST01", request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Civil Engineering");
        verify(departmentRepository).save(sampleDepartment);
    }

    @Test
    @DisplayName("updateDepartment: should allow updating to the same current name without throwing conflict")
    void updateDepartment_sameName_success() {
        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));
        when(departmentRepository.findByNameIgnoreCase("Track Maintenance")).thenReturn(Optional.of(sampleDepartment));
        when(departmentRepository.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateDepartmentRequest request = new UpdateDepartmentRequest("Track Maintenance");
        DepartmentResponse response = organizationService.updateDepartment(sampleId.toString(), request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Track Maintenance");
        verify(departmentRepository).save(sampleDepartment);
    }

    @Test
    @DisplayName("updateDepartment: should throw ResourceConflictException when name is taken by another department")
    void updateDepartment_duplicateNameOtherDept_throwsConflict() {
        Department otherDept = new Department();
        otherDept.setId(UUID.randomUUID());
        otherDept.setName("Mechanical Operations");

        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));
        when(departmentRepository.findByNameIgnoreCase("Mechanical Operations")).thenReturn(Optional.of(otherDept));

        UpdateDepartmentRequest request = new UpdateDepartmentRequest("Mechanical Operations");

        assertThatThrownBy(() -> organizationService.updateDepartment(sampleId.toString(), request))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("already exists");

        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    @DisplayName("updateDepartment: should throw ResourceNotFoundException when department does not exist")
    void updateDepartment_notFound_throwsException() {
        when(departmentRepository.findByCodeIgnoreCase("DEP-MISSING")).thenReturn(Optional.empty());

        UpdateDepartmentRequest request = new UpdateDepartmentRequest("New Name");

        assertThatThrownBy(() -> organizationService.updateDepartment("DEP-MISSING", request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found with identifier: DEP-MISSING");
    }

    @Test
    @DisplayName("updateDepartment: should throw BadRequestException when updated name length is invalid")
    void updateDepartment_invalidNameLength_throwsBadRequest() {
        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));

        UpdateDepartmentRequest shortReq = new UpdateDepartmentRequest("X");
        assertThatThrownBy(() -> organizationService.updateDepartment(sampleId.toString(), shortReq))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("between 2 and 100 characters");

        UpdateDepartmentRequest longReq = new UpdateDepartmentRequest("X".repeat(101));
        assertThatThrownBy(() -> organizationService.updateDepartment(sampleId.toString(), longReq))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("between 2 and 100 characters");
    }

    // =========================================================================
    // 5. Deactivation (DELETE /department/{departmentId})
    // =========================================================================

    @Test
    @DisplayName("deactivateDepartment: should transition status to INACTIVE when no active records reference department")
    void deactivateDepartment_success() {
        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));
        when(userDepartmentRepository.existsActiveByDepartmentId(sampleId)).thenReturn(false);
        when(maintenanceTaskRepository.existsActiveTasksByDepartmentId(sampleId)).thenReturn(false);
        when(maintenanceResourceRepository.existsActiveResourcesByDepartmentId(sampleId)).thenReturn(false);
        when(assetTypeRepository.existsByDepartmentId(sampleId)).thenReturn(false);

        organizationService.deactivateDepartment(sampleId.toString());

        assertThat(sampleDepartment.getStatus()).isEqualTo("INACTIVE");
        verify(departmentRepository).save(sampleDepartment);
    }

    @Test
    @DisplayName("deactivateDepartment: should throw ResourceNotFoundException when department not found")
    void deactivateDepartment_notFound_throwsException() {
        when(departmentRepository.findByCodeIgnoreCase("DEP-UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> organizationService.deactivateDepartment("DEP-UNKNOWN"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found with identifier: DEP-UNKNOWN");

        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    @DisplayName("deactivateDepartment: should throw ResourceConflictException when active users are assigned to department")
    void deactivateDepartment_conflictActiveUsers_throwsConflict() {
        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));
        when(userDepartmentRepository.existsActiveByDepartmentId(sampleId)).thenReturn(true);

        assertThatThrownBy(() -> organizationService.deactivateDepartment(sampleId.toString()))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("active user assignments");

        assertThat(sampleDepartment.getStatus()).isEqualTo("ACTIVE");
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    @DisplayName("deactivateDepartment: should throw ResourceConflictException when active maintenance tasks exist")
    void deactivateDepartment_conflictActiveTasks_throwsConflict() {
        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));
        when(userDepartmentRepository.existsActiveByDepartmentId(sampleId)).thenReturn(false);
        when(maintenanceTaskRepository.existsActiveTasksByDepartmentId(sampleId)).thenReturn(true);

        assertThatThrownBy(() -> organizationService.deactivateDepartment(sampleId.toString()))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("active maintenance tasks");

        assertThat(sampleDepartment.getStatus()).isEqualTo("ACTIVE");
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    @DisplayName("deactivateDepartment: should throw ResourceConflictException when active maintenance resources exist")
    void deactivateDepartment_conflictActiveResources_throwsConflict() {
        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));
        when(userDepartmentRepository.existsActiveByDepartmentId(sampleId)).thenReturn(false);
        when(maintenanceTaskRepository.existsActiveTasksByDepartmentId(sampleId)).thenReturn(false);
        when(maintenanceResourceRepository.existsActiveResourcesByDepartmentId(sampleId)).thenReturn(true);

        assertThatThrownBy(() -> organizationService.deactivateDepartment(sampleId.toString()))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("active maintenance resources");

        assertThat(sampleDepartment.getStatus()).isEqualTo("ACTIVE");
        verify(departmentRepository, never()).save(any(Department.class));
    }

    @Test
    @DisplayName("deactivateDepartment: should throw ResourceConflictException when associated asset types exist")
    void deactivateDepartment_conflictActiveAssetTypes_throwsConflict() {
        when(departmentRepository.findById(sampleId)).thenReturn(Optional.of(sampleDepartment));
        when(userDepartmentRepository.existsActiveByDepartmentId(sampleId)).thenReturn(false);
        when(maintenanceTaskRepository.existsActiveTasksByDepartmentId(sampleId)).thenReturn(false);
        when(maintenanceResourceRepository.existsActiveResourcesByDepartmentId(sampleId)).thenReturn(false);
        when(assetTypeRepository.existsByDepartmentId(sampleId)).thenReturn(true);

        assertThatThrownBy(() -> organizationService.deactivateDepartment(sampleId.toString()))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("associated asset types");

        assertThat(sampleDepartment.getStatus()).isEqualTo("ACTIVE");
        verify(departmentRepository, never()).save(any(Department.class));
    }
}
