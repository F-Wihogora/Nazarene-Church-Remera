package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflow_requests", indexes = {
    @Index(name = "idx_workflow_status", columnList = "status"),
    @Index(name = "idx_workflow_type", columnList = "request_type")
})
@Getter
@Setter
@NoArgsConstructor
public class WorkflowRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_type", nullable = false, length = 50)
    private String requestType; // EXPENSE_APPROVAL, RESERVATION_APPROVAL, DOCUMENT_APPROVAL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkflowStatus status = WorkflowStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_pending_role", length = 50)
    private UserRoleType currentPendingRole;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "reference_id")
    private Long referenceId; // The ID of the target entity under approval

    @OneToMany(mappedBy = "workflowRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkflowStep> steps = new ArrayList<>();
}
