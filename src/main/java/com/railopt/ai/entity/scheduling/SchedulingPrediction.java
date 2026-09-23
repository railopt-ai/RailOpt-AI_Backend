package com.railopt.ai.entity.scheduling;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.railopt.ai.entity.maintenance.MaintenanceTask;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `scheduling_predictions`. */
@Entity
@Table(name = "scheduling_predictions")
@Getter
@Setter
@NoArgsConstructor
public class SchedulingPrediction {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheduling_run_id", nullable = false, columnDefinition = "uuid")
    private SchedulingRun schedulingRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "maintenance_task_id", nullable = false, columnDefinition = "uuid")
    private MaintenanceTask maintenanceTask;

    @Column(name = "prediction_type", columnDefinition = "varchar")
    private String predictionType;

    @Column(name = "predicted_value", columnDefinition = "numeric")
    private BigDecimal predictedValue;

    @Column(name = "model_name", columnDefinition = "varchar")
    private String modelName;

    @Column(name = "model_version", columnDefinition = "varchar")
    private String modelVersion;

    @Column(name = "confidence_score", columnDefinition = "numeric")
    private BigDecimal confidenceScore;

    /** PostgreSQL JSONB; Hibernate JSON JDBC type. */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "input_features", columnDefinition = "jsonb")
    private Map<String, Object> inputFeatures;

    @Column(name = "predicted_at", columnDefinition = "timestamp")
    private LocalDateTime predictedAt;

}
