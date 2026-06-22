package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "visitors", indexes = {
    @Index(name = "idx_visitors_visit_date", columnList = "visit_date"),
    @Index(name = "idx_visitors_followup_status", columnList = "followup_status")
})
@Getter
@Setter
@NoArgsConstructor
public class Visitor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_member_id")
    private Member invitedBy;

    @Column(name = "followup_status", nullable = false, length = 50)
    private String followUpStatus = "PENDING"; // PENDING, CONTACTED, VISITED, CONVERTED_TO_MEMBER

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "converted_member_id")
    private Member convertedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;
}
