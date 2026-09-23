package com.railopt.ai.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.user.UserRole;
import com.railopt.ai.entity.user.UserRoleId;

/** Repository interface for {@link UserRole}. */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
