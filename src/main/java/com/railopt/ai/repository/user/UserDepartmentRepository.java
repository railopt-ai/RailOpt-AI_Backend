package com.railopt.ai.repository.user;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.user.UserDepartment;
import com.railopt.ai.model.user.UserDepartmentId;

/** Repository interface for {@link UserDepartment}. */
@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, UserDepartmentId> {

    @Query("SELECT ud FROM UserDepartment ud WHERE ud.id.userId = :userId")
    List<UserDepartment> findByUserId(@Param("userId") UUID userId);

    @Query("SELECT ud FROM UserDepartment ud WHERE ud.id.userId = :userId AND ud.leftAt IS NULL")
    List<UserDepartment> findActiveByUserId(@Param("userId") UUID userId);

    @Query("SELECT ud FROM UserDepartment ud WHERE ud.id.userId = :userId AND ud.leftAt IS NULL ORDER BY ud.joinedAt DESC")
    List<UserDepartment> findActiveByUserIdOrderByJoinedAtDesc(@Param("userId") UUID userId);

    @Query("SELECT COUNT(ud) > 0 FROM UserDepartment ud WHERE ud.id.departmentId = :departmentId AND ud.leftAt IS NULL")
    boolean existsActiveByDepartmentId(@Param("departmentId") UUID departmentId);
}
