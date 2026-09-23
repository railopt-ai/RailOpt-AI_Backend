package com.railopt.ai.model.user;

import java.time.LocalDateTime;

import com.railopt.ai.model.organization.Department;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Persistence mapping for table `user_departments`. */
@Entity
@Table(name = "user_departments")
@Getter
@Setter
@NoArgsConstructor
public class UserDepartment {

    @EmbeddedId
    private UserDepartmentId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("departmentId")
    @JoinColumn(name = "department_id", nullable = false, columnDefinition = "uuid")
    private Department department;

    @Column(name = "joined_at", columnDefinition = "timestamp")
    private LocalDateTime joinedAt;

    @Column(name = "left_at", columnDefinition = "timestamp")
    private LocalDateTime leftAt;

}
