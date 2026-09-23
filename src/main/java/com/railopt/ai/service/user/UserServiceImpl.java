package com.railopt.ai.service.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

/**
 * Service implementation for the User domain.
 * Coordinates persistence, business rules, and mapping for all user operations.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_INACTIVE = "INACTIVE";
    private static final String STATUS_SUSPENDED = "SUSPENDED";
    private static final Set<String> ALLOWED_STATUSES = Set.of(STATUS_ACTIVE, STATUS_INACTIVE, STATUS_SUSPENDED);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserDepartmentRepository userDepartmentRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            DepartmentRepository departmentRepository,
            UserRoleRepository userRoleRepository,
            UserDepartmentRepository userDepartmentRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.userRoleRepository = userRoleRepository;
        this.userDepartmentRepository = userDepartmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String firebaseUid) {
        validateFirebaseUid(firebaseUid);

        User user = userRepository.findByGoogleSubjectId(firebaseUid)
                .orElseThrow(() -> new UserNotFoundException("RailOpt-AI user record not found for authenticated account"));

        validateUserIsActive(user);

        return mapToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateCurrentUserProfile(String firebaseUid, UpdateUserProfileRequest request) {
        validateFirebaseUid(firebaseUid);

        User user = userRepository.findByGoogleSubjectId(firebaseUid)
                .orElseThrow(() -> new UserNotFoundException("RailOpt-AI user record not found for authenticated account"));

        validateUserIsActive(user);

        if (request != null && request.name() != null && !request.name().isBlank()) {
            user.setName(request.name().trim());
        }

        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse createUser(String firebaseUid, CreateUserRequest request) {
        validateFirebaseUid(firebaseUid);

        if (request == null) {
            throw new BadRequestException("User creation request cannot be null");
        }

        if (userRepository.existsByGoogleSubjectId(firebaseUid)) {
            throw new UserAlreadyExistsException("User already exists for the authenticated Firebase account");
        }

        String normalizedEmail = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResourceConflictException("User with email '" + request.email() + "' already exists");
        }

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setGoogleSubjectId(firebaseUid);
        user.setEmail(normalizedEmail);
        user.setName(request.name() != null ? request.name().trim() : null);
        user.setStatus(STATUS_ACTIVE);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setLastLoginAt(now);

        User savedUser = userRepository.save(user);

        // Optional initial role assignment
        if (request.role() != null && !request.role().isBlank()) {
            Role role = findRoleByIdOrCode(request.role().trim());
            if (role == null) {
                throw new BadRequestException("Invalid or unsupported role: " + request.role());
            }
            UserRole userRole = new UserRole();
            userRole.setId(new UserRoleId(savedUser.getId(), role.getId()));
            userRole.setUser(savedUser);
            userRole.setRole(role);
            userRole.setAssignedAt(now);
            userRoleRepository.save(userRole);
        }

        // Optional initial department assignment
        if (request.departmentId() != null && !request.departmentId().isBlank()) {
            Department department = findDepartmentByIdOrCode(request.departmentId().trim());
            if (department == null) {
                throw new BadRequestException("Invalid department: " + request.departmentId());
            }
            UserDepartment userDepartment = new UserDepartment();
            userDepartment.setId(new UserDepartmentId(savedUser.getId(), department.getId()));
            userDepartment.setUser(savedUser);
            userDepartment.setDepartment(department);
            userDepartment.setJoinedAt(now);
            userDepartmentRepository.save(userDepartment);
        }

        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserListResponse listUsers(String role, String departmentId, String status, String search, int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number must be non-negative: " + page);
        }
        if (size < 1 || size > 100) {
            throw new BadRequestException("Page size must be between 1 and 100: " + size);
        }

        String normalizedRole = (role != null && !role.isBlank()) ? role.trim() : null;
        String normalizedStatus = (status != null && !status.isBlank()) ? status.trim().toUpperCase() : null;
        String normalizedSearch = (search != null && !search.isBlank()) ? search.trim() : null;

        String departmentCode = null;
        UUID departmentUuid = null;
        if (departmentId != null && !departmentId.isBlank()) {
            String trimmedDept = departmentId.trim();
            try {
                departmentUuid = UUID.fromString(trimmedDept);
            } catch (IllegalArgumentException e) {
                departmentCode = trimmedDept;
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findUsersWithFilters(
                normalizedRole,
                departmentCode,
                departmentUuid,
                normalizedStatus,
                normalizedSearch,
                pageable
        );

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(this::mapToUserResponse)
                .toList();

        return new UserListResponse(
                userResponses,
                (int) userPage.getTotalElements(),
                page,
                size
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(String userId) {
        UUID userUuid = parseUserId(userId);

        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        return mapToUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(String userId, AdminUpdateUserRequest request) {
        UUID userUuid = parseUserId(userId);

        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        LocalDateTime now = LocalDateTime.now();

        if (request != null) {
            if (request.name() != null && !request.name().isBlank()) {
                user.setName(request.name().trim());
            }

            // Update role if requested
            if (request.role() != null && !request.role().isBlank()) {
                Role role = findRoleByIdOrCode(request.role().trim());
                if (role == null) {
                    throw new BadRequestException("Invalid or unsupported role: " + request.role());
                }
                userRoleRepository.deleteByUserId(user.getId());
                UserRole userRole = new UserRole();
                userRole.setId(new UserRoleId(user.getId(), role.getId()));
                userRole.setUser(user);
                userRole.setRole(role);
                userRole.setAssignedAt(now);
                userRoleRepository.save(userRole);
            }

            // Update department if requested
            if (request.departmentId() != null && !request.departmentId().isBlank()) {
                Department department = findDepartmentByIdOrCode(request.departmentId().trim());
                if (department == null) {
                    throw new ResourceNotFoundException("Department not found: " + request.departmentId());
                }
                closeActiveDepartments(user.getId(), now);
                UserDepartment userDepartment = new UserDepartment();
                userDepartment.setId(new UserDepartmentId(user.getId(), department.getId()));
                userDepartment.setUser(user);
                userDepartment.setDepartment(department);
                userDepartment.setJoinedAt(now);
                userDepartmentRepository.save(userDepartment);
            }
        }

        user.setUpdatedAt(now);
        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUserRole(String userId, UpdateUserRoleRequest request) {
        UUID userUuid = parseUserId(userId);

        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (request == null || request.role() == null || request.role().isBlank()) {
            throw new BadRequestException("Role cannot be blank");
        }

        Role role = findRoleByIdOrCode(request.role().trim());
        if (role == null) {
            throw new BadRequestException("Invalid or unsupported role: " + request.role());
        }

        LocalDateTime now = LocalDateTime.now();

        userRoleRepository.deleteByUserId(user.getId());

        UserRole userRole = new UserRole();
        userRole.setId(new UserRoleId(user.getId(), role.getId()));
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setAssignedAt(now);
        userRoleRepository.save(userRole);

        user.setUpdatedAt(now);
        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUserDepartment(String userId, UpdateUserDepartmentRequest request) {
        UUID userUuid = parseUserId(userId);

        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (request == null || request.departmentId() == null || request.departmentId().isBlank()) {
            throw new BadRequestException("Department ID cannot be blank");
        }

        Department department = findDepartmentByIdOrCode(request.departmentId().trim());
        if (department == null) {
            throw new ResourceNotFoundException("Department not found: " + request.departmentId());
        }

        LocalDateTime now = LocalDateTime.now();

        closeActiveDepartments(user.getId(), now);

        UserDepartment userDepartment = new UserDepartment();
        userDepartment.setId(new UserDepartmentId(user.getId(), department.getId()));
        userDepartment.setUser(user);
        userDepartment.setDepartment(department);
        userDepartment.setJoinedAt(now);
        userDepartmentRepository.save(userDepartment);

        user.setUpdatedAt(now);
        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUserStatus(String userId, UpdateUserStatusRequest request) {
        UUID userUuid = parseUserId(userId);

        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (request == null || request.status() == null || request.status().isBlank()) {
            throw new BadRequestException("Status cannot be blank");
        }

        String normalizedStatus = request.status().trim().toUpperCase();
        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
            throw new BadRequestException("Invalid user status: " + request.status() + ". Allowed: " + ALLOWED_STATUSES);
        }

        user.setStatus(normalizedStatus);
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse deactivateUser(String userId) {
        return updateUserStatus(userId, new UpdateUserStatusRequest(STATUS_INACTIVE));
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    private void validateFirebaseUid(String firebaseUid) {
        if (firebaseUid == null || firebaseUid.isBlank()) {
            throw new UnauthorizedException("Missing, invalid, or expired Firebase ID Token");
        }
    }

    private void validateUserIsActive(User user) {
        if (!STATUS_ACTIVE.equalsIgnoreCase(user.getStatus())) {
            throw new UserAccountInactiveException("User account is inactive or suspended: " + user.getStatus());
        }
    }

    private UUID parseUserId(String userIdStr) {
        if (userIdStr == null || userIdStr.isBlank()) {
            throw new BadRequestException("User ID cannot be blank");
        }
        try {
            return UUID.fromString(userIdStr.trim());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid user ID format: " + userIdStr + ". Expected a valid UUID.");
        }
    }

    private Role findRoleByIdOrCode(String roleIdentifier) {
        if (roleIdentifier == null || roleIdentifier.isBlank()) {
            return null;
        }
        String trimmed = roleIdentifier.trim();
        try {
            UUID uuid = UUID.fromString(trimmed);
            Optional<Role> role = roleRepository.findById(uuid);
            if (role.isPresent()) {
                return role.get();
            }
        } catch (IllegalArgumentException ignored) {
            // Not a UUID, search by code
        }
        return roleRepository.findByCodeIgnoreCase(trimmed).orElse(null);
    }

    private Department findDepartmentByIdOrCode(String departmentIdentifier) {
        if (departmentIdentifier == null || departmentIdentifier.isBlank()) {
            return null;
        }
        String trimmed = departmentIdentifier.trim();
        try {
            UUID uuid = UUID.fromString(trimmed);
            Optional<Department> dept = departmentRepository.findById(uuid);
            if (dept.isPresent()) {
                return dept.get();
            }
        } catch (IllegalArgumentException ignored) {
            // Not a UUID, search by code
        }
        return departmentRepository.findByCodeIgnoreCase(trimmed).orElse(null);
    }

    private void closeActiveDepartments(UUID userId, LocalDateTime leftAt) {
        List<UserDepartment> activeDepts = userDepartmentRepository.findActiveByUserId(userId);
        for (UserDepartment ud : activeDepts) {
            ud.setLeftAt(leftAt);
            userDepartmentRepository.save(ud);
        }
    }

    private UserResponse mapToUserResponse(User user) {
        String roleCode = userRoleRepository.findByUserIdOrderByAssignedAtDesc(user.getId())
                .stream()
                .findFirst()
                .map(ur -> ur.getRole() != null ? ur.getRole().getCode() : null)
                .orElse(null);

        String departmentCode = userDepartmentRepository.findActiveByUserIdOrderByJoinedAtDesc(user.getId())
                .stream()
                .findFirst()
                .map(ud -> ud.getDepartment() != null ? ud.getDepartment().getCode() : null)
                .orElse(null);

        return new UserResponse(
                user.getId().toString(),
                user.getEmail(),
                user.getName(),
                null, // phone is not stored in the users table per docs/database-schema.mmd
                roleCode,
                departmentCode,
                user.getStatus()
        );
    }
}
