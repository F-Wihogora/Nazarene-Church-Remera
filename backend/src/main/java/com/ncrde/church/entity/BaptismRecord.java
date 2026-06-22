package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "baptism_records")
@Getter
@Setter
@NoArgsConstructor
public class BaptismRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "baptism_date", nullable = false)
    private LocalDate baptismDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "officiating_pastor_id", nullable = false)
    private Member officiatingPastor;

    @Column(length = 255)
    private String location;

    @Column(length = 150)
    private String godparents;
}
