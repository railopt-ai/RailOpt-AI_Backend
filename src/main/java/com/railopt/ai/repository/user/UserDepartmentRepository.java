package com.railopt.ai.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.user.UserDepartment;
import com.railopt.ai.entity.user.UserDepartmentId;

/** Repository interface for {@link UserDepartment}. */
@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, UserDepartmentId> {
}
