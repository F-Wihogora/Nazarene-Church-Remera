package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_steps")
@Getter
@Setter
@NoArgsConstructor
public class WorkflowStep extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_request_id", nullable = false)
    private WorkflowRequest workflowRequest;

    @Column(nullable = false, length = 50)
    private String action; // SUBMIT, APPROVE, REJECT, ARCHIVE

    @Column(name = "performed_by", nullable = false, length = 50)
    private String performedBy;

    @Column(length = 500)
    private String comments;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate = LocalDateTime.now();
}
