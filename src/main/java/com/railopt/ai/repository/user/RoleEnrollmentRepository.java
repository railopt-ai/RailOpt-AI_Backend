package com.railopt.ai.repository.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.user.RoleEnrollment;

/** Repository interface for {@link RoleEnrollment}. */
@Repository
public interface RoleEnrollmentRepository extends JpaRepository<RoleEnrollment, UUID> {
}
