package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "marriage_records")
@Getter
@Setter
@NoArgsConstructor
public class MarriageRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "husband_member_id")
    private Member husband;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wife_member_id")
    private Member wife;

    @Column(name = "husband_name_non_member", length = 100)
    private String husbandNameNonMember;

    @Column(name = "wife_name_non_member", length = 100)
    private String wifeNameNonMember;

    @Column(name = "wedding_date", nullable = false)
    private LocalDate weddingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officiating_pastor_id", nullable = false)
    private Member officiatingPastor;

    @Column(length = 255)
    private String witnesses;
}
