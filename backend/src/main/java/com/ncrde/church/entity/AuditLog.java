package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_action", columnList = "action"),
    @Index(name = "idx_audit_performed_by", columnList = "performed_by"),
    @Index(name = "idx_audit_time", columnList = "action_time")
})
@Getter
@Setter
@NoArgsConstructor
public class AuditLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(name = "performed_by", nullable = false, length = 100)
    private String performedBy;

    @Column(name = "action_time", nullable = false)
    private LocalDateTime actionTime = LocalDateTime.now();

    @Column(name = "target_table", length = 100)
    private String targetTable;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "old_value", length = 4000)
    private String oldValue; // JSON snapshot of previous data

    @Column(name = "new_value", length = 4000)
    private String newValue; // JSON snapshot of updated data

    public AuditLog(String action, String performedBy, String targetTable, Long targetId, String oldValue, String newValue) {
        this.action = action;
        this.performedBy = performedBy;
        this.targetTable = targetTable;
        this.targetId = targetId;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
