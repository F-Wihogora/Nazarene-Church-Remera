package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "inventory_transactions", indexes = {
    @Index(name = "idx_inv_status", columnList = "status"),
    @Index(name = "idx_inv_borrow_date", columnList = "borrow_date")
})
@Getter
@Setter
@NoArgsConstructor
public class InventoryTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowed_by_member_id", nullable = false)
    private Member borrowedBy;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "expected_return_date")
    private LocalDate expectedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    @Column(name = "damage_report", length = 1000)
    private String damageReport;

    @Column(nullable = false, length = 30)
    private String status = "BORROWED"; // BORROWED, RETURNED, DAMAGED
}
