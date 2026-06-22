package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "registrations", indexes = {
    @Index(name = "idx_reg_date", columnList = "registration_date"),
    @Index(name = "idx_reg_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
public class Registration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "non_member_name", length = 100)
    private String nonMemberName;

    @Column(name = "non_member_email", length = 100)
    private String nonMemberEmail;

    @Column(name = "non_member_phone", length = 20)
    private String nonMemberPhone;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "payment_status", nullable = false, length = 30)
    private String paymentStatus = "FREE"; // FREE, PENDING, PAID

    @Column(nullable = false, length = 30)
    private String status = "CONFIRMED"; // CONFIRMED, CANCELLED
}
