package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "assets", indexes = {
    @Index(name = "idx_assets_name", columnList = "name"),
    @Index(name = "idx_assets_condition", columnList = "asset_condition")
})
@Getter
@Setter
@NoArgsConstructor
public class Asset extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "asset_condition", nullable = false, length = 30)
    private String assetCondition = "EXCELLENT"; // EXCELLENT, GOOD, FAIR, POOR

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_department_id")
    private Department assignedDepartment;
}
