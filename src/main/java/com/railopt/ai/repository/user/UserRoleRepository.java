package com.railopt.ai.repository.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.user.UserRole;
import com.railopt.ai.model.user.UserRoleId;

/** Repository interface for {@link UserRole}. */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.id.userId = :userId")
    List<UserRole> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.id.userId = :userId ORDER BY ur.assignedAt DESC")
    List<UserRole> findByUserIdOrderByAssignedAtDesc(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.id.userId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}
