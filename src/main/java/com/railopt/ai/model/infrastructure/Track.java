package com.railopt.ai.model.infrastructure;

import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.model.organization.RailwayDivision;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `tracks`. */
@Entity
@Table(
        name = "tracks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Track {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "division_id", nullable = false, columnDefinition = "uuid")
    private RailwayDivision division;

    @Column(name = "code", nullable = false, unique = true, columnDefinition = "varchar")
    private String code;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @Column(name = "track_type", columnDefinition = "varchar")
    private String trackType;

    @Column(name = "gauge", columnDefinition = "varchar")
    private String gauge;

    @Column(name = "electrification_status", columnDefinition = "varchar")
    private String electrificationStatus;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
