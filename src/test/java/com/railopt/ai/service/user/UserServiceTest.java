package com.railopt.ai.service.user;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.railopt.ai.payload.user.AdminUpdateUserRequest;
import com.railopt.ai.payload.user.CreateUserRequest;
import com.railopt.ai.payload.user.UpdateUserDepartmentRequest;
import com.railopt.ai.payload.user.UpdateUserProfileRequest;
import com.railopt.ai.payload.user.UpdateUserRoleRequest;
import com.railopt.ai.payload.user.UpdateUserStatusRequest;
import com.railopt.ai.payload.user.UserListResponse;
import com.railopt.ai.payload.user.UserResponse;
import com.railopt.ai.model.organization.Department;
import com.railopt.ai.model.user.Role;
import com.railopt.ai.model.user.User;
import com.railopt.ai.model.user.UserDepartment;
import com.railopt.ai.model.user.UserDepartmentId;
import com.railopt.ai.model.user.UserRole;
import com.railopt.ai.model.user.UserRoleId;
import com.railopt.ai.exception.BadRequestException;
import com.railopt.ai.exception.ResourceConflictException;
import com.railopt.ai.exception.ResourceNotFoundException;
import com.railopt.ai.exception.UnauthorizedException;
import com.railopt.ai.exception.UserAccountInactiveException;
import com.railopt.ai.exception.UserAlreadyExistsException;
import com.railopt.ai.exception.UserNotFoundException;
import com.railopt.ai.repository.organization.DepartmentRepository;
import com.railopt.ai.repository.user.RoleRepository;
import com.railopt.ai.repository.user.UserDepartmentRepository;
import com.railopt.ai.repository.user.UserRepository;
import com.railopt.ai.repository.user.UserRoleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserDepartmentRepository userDepartmentRepository;

    private UserServiceImpl userService;

    private User sampleUser;
    private Role sampleRole;
    private Department sampleDepartment;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository,
                roleRepository,
                departmentRepository,
                userRoleRepository,
                userDepartmentRepository
        );

        sampleUser = new User();
        sampleUser.setId(UUID.randomUUID());
        sampleUser.setGoogleSubjectId("firebase-uid-12345");
        sampleUser.setEmail("planner@railway.gov.in");
        sampleUser.setName("Rahul Sharma");
        sampleUser.setStatus("ACTIVE");
        sampleUser.setCreatedAt(LocalDateTime.now().minusDays(10));
        sampleUser.setUpdatedAt(LocalDateTime.now().minusDays(1));

        sampleRole = new Role();
        sampleRole.setId(UUID.randomUUID());
        sampleRole.setCode("PLANNER");
        sampleRole.setName("Train Planner");

        sampleDepartment = new Department();
        sampleDepartment.setId(UUID.randomUUID());
        sampleDepartment.setCode("DEP-12");
        sampleDepartment.setName("Track Maintenance");
    }

    // =========================================================================
    // 1. Current User (GET /user/me)
    // =========================================================================

    @Test
    @DisplayName("getCurrentUser: should return UserResponse when user exists and is active")
    void getCurrentUser_success() {
        when(userRepository.findByGoogleSubjectId("firebase-uid-12345"))
                .thenReturn(Optional.of(sampleUser));

        UserRole userRole = new UserRole();
        userRole.setUser(sampleUser);
        userRole.setRole(sampleRole);
        when(userRoleRepository.findByUserIdOrderByAssignedAtDesc(sampleUser.getId()))
                .thenReturn(List.of(userRole));

        UserDepartment userDepartment = new UserDepartment();
        userDepartment.setUser(sampleUser);
        userDepartment.setDepartment(sampleDepartment);
        when(userDepartmentRepository.findActiveByUserIdOrderByJoinedAtDesc(sampleUser.getId()))
                .thenReturn(List.of(userDepartment));

        UserResponse response = userService.getCurrentUser("firebase-uid-12345");

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(sampleUser.getId().toString());
        assertThat(response.email()).isEqualTo("planner@railway.gov.in");
        assertThat(response.name()).isEqualTo("Rahul Sharma");
        assertThat(response.role()).isEqualTo("PLANNER");
        assertThat(response.departmentId()).isEqualTo("DEP-12");
        assertThat(response.status()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("getCurrentUser: should throw UnauthorizedException when Firebase UID is blank")
    void getCurrentUser_missingFirebaseUid() {
        assertThatThrownBy(() -> userService.getCurrentUser(""))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Firebase ID Token");
    }

    @Test
    @DisplayName("getCurrentUser: should throw UserNotFoundException when record does not exist")
    void getCurrentUser_notFound() {
        when(userRepository.findByGoogleSubjectId("unknown-uid"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getCurrentUser("unknown-uid"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("RailOpt-AI user record not found");
    }

    @Test
    @DisplayName("getCurrentUser: should throw UserAccountInactiveException when user status is INACTIVE")
    void getCurrentUser_inactiveAccount() {
        sampleUser.setStatus("INACTIVE");
        when(userRepository.findByGoogleSubjectId("firebase-uid-12345"))
                .thenReturn(Optional.of(sampleUser));

        assertThatThrownBy(() -> userService.getCurrentUser("firebase-uid-12345"))
                .isInstanceOf(UserAccountInactiveException.class)
                .hasMessageContaining("User account is inactive or suspended");
    }

    // =========================================================================
    // 2. Update Current User Profile (PATCH /user/me)
    // =========================================================================

    @Test
    @DisplayName("updateCurrentUserProfile: should update name and save user")
    void updateCurrentUserProfile_success() {
        when(userRepository.findByGoogleSubjectId("firebase-uid-12345"))
                .thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateUserProfileRequest request = new UpdateUserProfileRequest("Rahul S. Sharma");
        UserResponse response = userService.updateCurrentUserProfile("firebase-uid-12345", request);

        assertThat(response.name()).isEqualTo("Rahul S. Sharma");
        verify(userRepository).save(sampleUser);
    }

    // =========================================================================
    // 3. Create User (POST /user)
    // =========================================================================

    @Test
    @DisplayName("createUser: should create active user and assign initial role and department")
    void createUser_success() {
        when(userRepository.existsByGoogleSubjectId("new-firebase-uid")).thenReturn(false);
        when(userRepository.existsByEmail("new.planner@railway.gov.in")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(roleRepository.findByCodeIgnoreCase("PLANNER")).thenReturn(Optional.of(sampleRole));
        when(departmentRepository.findByCodeIgnoreCase("DEP-12")).thenReturn(Optional.of(sampleDepartment));

        CreateUserRequest request = new CreateUserRequest(
                "new.planner@railway.gov.in",
                "New Planner",
                "9876543210",
                "PLANNER",
                "DEP-12"
        );

        UserResponse response = userService.createUser("new-firebase-uid", request);

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("new.planner@railway.gov.in");
        assertThat(response.name()).isEqualTo("New Planner");
        assertThat(response.status()).isEqualTo("ACTIVE");

        verify(userRepository).save(any(User.class));
        verify(userRoleRepository).save(any(UserRole.class));
        verify(userDepartmentRepository).save(any(UserDepartment.class));
    }

    @Test
    @DisplayName("createUser: should throw UserAlreadyExistsException when Firebase account already registered")
    void createUser_duplicateFirebaseUid() {
        when(userRepository.existsByGoogleSubjectId("existing-firebase-uid")).thenReturn(true);

        CreateUserRequest request = new CreateUserRequest(
                "test@railway.gov.in",
                "Test User",
                null,
                null,
                null
        );

        assertThatThrownBy(() -> userService.createUser("existing-firebase-uid", request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User already exists for the authenticated Firebase account");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("createUser: should throw ResourceConflictException when email is already in use")
    void createUser_duplicateEmail() {
        when(userRepository.existsByGoogleSubjectId("new-uid")).thenReturn(false);
        when(userRepository.existsByEmail("existing@railway.gov.in")).thenReturn(true);

        CreateUserRequest request = new CreateUserRequest(
                "existing@railway.gov.in",
                "Test User",
                null,
                null,
                null
        );

        assertThatThrownBy(() -> userService.createUser("new-uid", request))
                .isInstanceOf(ResourceConflictException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("createUser: should throw BadRequestException when role is unknown")
    void createUser_invalidRole() {
        when(userRepository.existsByGoogleSubjectId("new-uid")).thenReturn(false);
        when(userRepository.existsByEmail("new@railway.gov.in")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(roleRepository.findByCodeIgnoreCase("UNKNOWN_ROLE")).thenReturn(Optional.empty());

        CreateUserRequest request = new CreateUserRequest(
                "new@railway.gov.in",
                "New User",
                null,
                "UNKNOWN_ROLE",
                null
        );

        assertThatThrownBy(() -> userService.createUser("new-uid", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid or unsupported role");
    }

    // =========================================================================
    // 4. Get User by ID (GET /user/{userId})
    // =========================================================================

    @Test
    @DisplayName("getUserById: should return user when ID is a valid existing UUID")
    void getUserById_success() {
        UUID id = sampleUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(sampleUser));

        UserResponse response = userService.getUserById(id.toString());

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id.toString());
    }

    @Test
    @DisplayName("getUserById: should throw BadRequestException when ID format is not a UUID")
    void getUserById_invalidFormat() {
        assertThatThrownBy(() -> userService.getUserById("NOT-A-UUID"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid user ID format");
    }

    @Test
    @DisplayName("getUserById: should throw UserNotFoundException when UUID does not exist")
    void getUserById_notFound() {
        UUID randomId = UUID.randomUUID();
        when(userRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(randomId.toString()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    // =========================================================================
    // 5. Update User Role (PATCH /user/{userId}/role)
    // =========================================================================

    @Test
    @DisplayName("updateUserRole: should replace existing role and record new UserRole")
    void updateUserRole_success() {
        UUID id = sampleUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(sampleUser));
        when(roleRepository.findByCodeIgnoreCase("ADMIN")).thenReturn(Optional.of(sampleRole));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UpdateUserRoleRequest request = new UpdateUserRoleRequest("ADMIN");
        UserResponse response = userService.updateUserRole(id.toString(), request);

        assertThat(response).isNotNull();
        verify(userRoleRepository).deleteByUserId(id);
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    @DisplayName("updateUserRole: should throw BadRequestException when role is unknown")
    void updateUserRole_invalidRole() {
        UUID id = sampleUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(sampleUser));
        when(roleRepository.findByCodeIgnoreCase("NON_EXISTENT")).thenReturn(Optional.empty());

        UpdateUserRoleRequest request = new UpdateUserRoleRequest("NON_EXISTENT");

        assertThatThrownBy(() -> userService.updateUserRole(id.toString(), request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid or unsupported role");
    }

    // =========================================================================
    // 6. Update User Department (PATCH /user/{userId}/department)
    // =========================================================================

    @Test
    @DisplayName("updateUserDepartment: should close active department and assign new one")
    void updateUserDepartment_success() {
        UUID id = sampleUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(sampleUser));
        when(departmentRepository.findByCodeIgnoreCase("DEP-12")).thenReturn(Optional.of(sampleDepartment));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserDepartment activeDepartment = new UserDepartment();
        activeDepartment.setId(new UserDepartmentId(id, UUID.randomUUID()));
        when(userDepartmentRepository.findActiveByUserId(id)).thenReturn(List.of(activeDepartment));

        UpdateUserDepartmentRequest request = new UpdateUserDepartmentRequest("DEP-12");
        UserResponse response = userService.updateUserDepartment(id.toString(), request);

        assertThat(response).isNotNull();
        assertThat(activeDepartment.getLeftAt()).isNotNull();
        verify(userDepartmentRepository).save(activeDepartment);
        verify(userDepartmentRepository, org.mockito.Mockito.times(2)).save(any(UserDepartment.class));
    }

    @Test
    @DisplayName("updateUserDepartment: should throw ResourceNotFoundException when department is not found")
    void updateUserDepartment_notFound() {
        UUID id = sampleUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(sampleUser));
        when(departmentRepository.findByCodeIgnoreCase("NON-EXISTENT")).thenReturn(Optional.empty());

        UpdateUserDepartmentRequest request = new UpdateUserDepartmentRequest("NON-EXISTENT");

        assertThatThrownBy(() -> userService.updateUserDepartment(id.toString(), request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Department not found");
    }

    // =========================================================================
    // 7. Update User Status (PATCH /user/{userId}/status)
    // =========================================================================

    @Test
    @DisplayName("updateUserStatus: should update status to INACTIVE")
    void updateUserStatus_success() {
        UUID id = sampleUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateUserStatusRequest request = new UpdateUserStatusRequest("INACTIVE");
        UserResponse response = userService.updateUserStatus(id.toString(), request);

        assertThat(sampleUser.getStatus()).isEqualTo("INACTIVE");
        assertThat(response.status()).isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("updateUserStatus: should throw BadRequestException when status is invalid")
    void updateUserStatus_invalidStatus() {
        UUID id = sampleUser.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(sampleUser));

        UpdateUserStatusRequest request = new UpdateUserStatusRequest("INVALID_STATUS");

        assertThatThrownBy(() -> userService.updateUserStatus(id.toString(), request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid user status");
    }

    // =========================================================================
    // 8. List Users (GET /user)
    // =========================================================================

    @Test
    @DisplayName("listUsers: should return paginated users matching criteria")
    void listUsers_success() {
        Page<User> page = new PageImpl<>(List.of(sampleUser), Pageable.ofSize(20), 1);
        when(userRepository.findUsersWithFilters(eq("PLANNER"), eq("DEP-12"), eq(null), eq("ACTIVE"), eq("Rahul"), any(Pageable.class)))
                .thenReturn(page);

        UserListResponse response = userService.listUsers("PLANNER", "DEP-12", "ACTIVE", "Rahul", 0, 20);

        assertThat(response).isNotNull();
        assertThat(response.total()).isEqualTo(1);
        assertThat(response.page()).isEqualTo(0);
        assertThat(response.size()).isEqualTo(20);
        assertThat(response.users()).hasSize(1);
    }

    @Test
    @DisplayName("listUsers: should throw BadRequestException for invalid pagination parameters")
    void listUsers_invalidPagination() {
        assertThatThrownBy(() -> userService.listUsers(null, null, null, null, -1, 20))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Page number must be non-negative");

        assertThatThrownBy(() -> userService.listUsers(null, null, null, null, 0, 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Page size must be between 1 and 100");
    }
}
