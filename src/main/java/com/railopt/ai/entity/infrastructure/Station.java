package com.railopt.ai.entity.infrastructure;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.entity.organization.RailwayDivision;

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


/** Persistence mapping for table `stations`. */
@Entity
@Table(
        name = "stations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Station {

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

    @Column(name = "latitude", columnDefinition = "numeric")
    private BigDecimal latitude;

    @Column(name = "longitude", columnDefinition = "numeric")
    private BigDecimal longitude;

    @Column(name = "station_type", columnDefinition = "varchar")
    private String stationType;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
