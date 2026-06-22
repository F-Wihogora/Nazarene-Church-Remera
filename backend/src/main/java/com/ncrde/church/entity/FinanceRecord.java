package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "finance_records", indexes = {
    @Index(name = "idx_finance_type", columnList = "record_type"),
    @Index(name = "idx_finance_category", columnList = "category"),
    @Index(name = "idx_finance_date", columnList = "record_date")
})
@Getter
@Setter
@NoArgsConstructor
public class FinanceRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_type", nullable = false, length = 20)
    private String recordType; // INCOME, EXPENSE

    @Column(nullable = false, length = 30)
    private String category; // TITHE, OFFERING, DONATION, SALARY, UTILITY, REPAIRS, OTHER

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // Nullable for general offerings or non-member donations

    @Column(length = 500)
    private String description;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod; // CASH, BANK_TRANSFER, MOBILE_MONEY, CHECK

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "workflow_request_id")
    private WorkflowRequest workflowRequest; // Optional: Only for expense requests requiring approvals
}
