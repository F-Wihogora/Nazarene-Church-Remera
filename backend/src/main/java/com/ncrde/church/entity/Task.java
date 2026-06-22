package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tasks", indexes = {
    @Index(name = "idx_tasks_due_date", columnList = "due_date"),
    @Index(name = "idx_tasks_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id", nullable = false)
    private Member assignee;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, IN_PROGRESS, COMPLETED
}
