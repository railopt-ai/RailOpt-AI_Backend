package com.railopt.ai.repository.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.user.User;

/** Repository interface for {@link User}. */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByGoogleSubjectId(String googleSubjectId);

    boolean existsByGoogleSubjectId(String googleSubjectId);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(
        value = """
            SELECT DISTINCT u FROM User u
            LEFT JOIN UserRole ur ON ur.user.id = u.id
            LEFT JOIN ur.role r
            LEFT JOIN UserDepartment ud ON ud.user.id = u.id AND ud.leftAt IS NULL
            LEFT JOIN ud.department d
            WHERE (:role IS NULL OR LOWER(r.code) = LOWER(:role))
              AND (:departmentCode IS NULL OR d.code = :departmentCode)
              AND (:departmentUuid IS NULL OR d.id = :departmentUuid)
              AND (:status IS NULL OR LOWER(u.status) = LOWER(:status))
              AND (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
        """,
        countQuery = """
            SELECT COUNT(DISTINCT u) FROM User u
            LEFT JOIN UserRole ur ON ur.user.id = u.id
            LEFT JOIN ur.role r
            LEFT JOIN UserDepartment ud ON ud.user.id = u.id AND ud.leftAt IS NULL
            LEFT JOIN ud.department d
            WHERE (:role IS NULL OR LOWER(r.code) = LOWER(:role))
              AND (:departmentCode IS NULL OR d.code = :departmentCode)
              AND (:departmentUuid IS NULL OR d.id = :departmentUuid)
              AND (:status IS NULL OR LOWER(u.status) = LOWER(:status))
              AND (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
        """
    )
    Page<User> findUsersWithFilters(
        @Param("role") String role,
        @Param("departmentCode") String departmentCode,
        @Param("departmentUuid") UUID departmentUuid,
        @Param("status") String status,
        @Param("search") String search,
        Pageable pageable
    );
}
