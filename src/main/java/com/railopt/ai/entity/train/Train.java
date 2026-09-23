package com.railopt.ai.entity.train;

import java.time.LocalDateTime;
import java.util.UUID;

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

/** Persistence mapping for table `trains`. */
@Entity
@Table(
        name = "trains",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "train_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Train {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_type_id", nullable = false, columnDefinition = "uuid")
    private TrainType trainType;

    @Column(name = "train_number", nullable = false, unique = true, columnDefinition = "varchar")
    private String trainNumber;

    @Column(name = "train_name", columnDefinition = "varchar")
    private String trainName;

    @Column(name = "operator", columnDefinition = "varchar")
    private String operator;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
