package com.railopt.ai.service.user;

import com.railopt.ai.payload.user.AdminUpdateUserRequest;
import com.railopt.ai.payload.user.CreateUserRequest;
import com.railopt.ai.payload.user.UpdateUserDepartmentRequest;
import com.railopt.ai.payload.user.UpdateUserProfileRequest;
import com.railopt.ai.payload.user.UpdateUserRoleRequest;
import com.railopt.ai.payload.user.UpdateUserStatusRequest;
import com.railopt.ai.payload.user.UserListResponse;
import com.railopt.ai.payload.user.UserResponse;

/**
 * Service interface for User domain operations.
 * Fulfills business logic for the User module as defined in docs/api.yaml.
 */
public interface UserService {

    /**
     * Retrieves the RailOpt-AI user associated with the verified Firebase account UID.
     * Corresponds to GET /user/me.
     *
     * @param firebaseUid Verified Firebase UID
     * @return User details
     */
    UserResponse getCurrentUser(String firebaseUid);

    /**
     * Updates editable profile information (name, phone) for the current user.
     * System-controlled fields (role, department, status, etc.) cannot be modified here.
     * Corresponds to PATCH /user/me.
     *
     * @param firebaseUid Verified Firebase UID
     * @param request Profile update payload
     * @return Updated user details
     */
    UserResponse updateCurrentUserProfile(String firebaseUid, UpdateUserProfileRequest request);

    /**
     * Creates the RailOpt-AI application user corresponding to the currently authenticated Firebase account.
     * Corresponds to POST /user.
     *
     * @param firebaseUid Verified Firebase UID
     * @param request User creation payload
     * @return Created user details
     */
    UserResponse createUser(String firebaseUid, CreateUserRequest request);

    /**
     * Returns a paginated list of RailOpt-AI users matching optional filter criteria.
     * Corresponds to GET /user.
     *
     * @param role Optional role filter
     * @param departmentId Optional department filter (code or UUID)
     * @param status Optional account status filter
     * @param search Optional search term for name or email
     * @param page Zero-based page number
     * @param size Number of users per page
     * @return Paginated user list
     */
    UserListResponse listUsers(String role, String departmentId, String status, String search, int page, int size);

    /**
     * Returns information about a specific RailOpt-AI user by ID.
     * Corresponds to GET /user/{userId}.
     *
     * @param userId User identifier (UUID or identifier string)
     * @return User details
     */
    UserResponse getUserById(String userId);

    /**
     * Updates manageable information (name, phone, role, department) of a specific user.
     * Restricted to authorized administrators.
     * Corresponds to PATCH /user/{userId}.
     *
     * @param userId User identifier
     * @param request Manageable fields update payload
     * @return Updated user details
     */
    UserResponse updateUser(String userId, AdminUpdateUserRequest request);

    /**
     * Assigns or changes the application role of a user.
     * Restricted to authorized administrators.
     * Corresponds to PATCH /user/{userId}/role.
     *
     * @param userId User identifier
     * @param request Role update payload
     * @return Updated user details
     */
    UserResponse updateUserRole(String userId, UpdateUserRoleRequest request);

    /**
     * Assigns a user to a department.
     * Restricted to authorized administrators.
     * Corresponds to PATCH /user/{userId}/department.
     *
     * @param userId User identifier
     * @param request Department assignment payload
     * @return Updated user details
     */
    UserResponse updateUserDepartment(String userId, UpdateUserDepartmentRequest request);

    /**
     * Changes the application status of a user (ACTIVE, INACTIVE, SUSPENDED).
     * Corresponds to PATCH /user/{userId}/status.
     *
     * @param userId User identifier
     * @param request Status update payload
     * @return Updated user details
     */
    UserResponse updateUserStatus(String userId, UpdateUserStatusRequest request);

    /**
     * Deactivates a user account (sets status to INACTIVE).
     *
     * @param userId User identifier
     * @return Updated user details
     */
    UserResponse deactivateUser(String userId);
}
