package com.railopt.ai.repository.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.user.Role;

/** Repository interface for {@link Role}. */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
}
