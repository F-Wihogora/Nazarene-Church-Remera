package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets", indexes = {
    @Index(name = "idx_tickets_code", columnList = "ticket_code")
})
@Getter
@Setter
@NoArgsConstructor
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false)
    private Registration registration;

    @Column(name = "ticket_code", unique = true, nullable = false, length = 50)
    private String ticketCode;

    @Column(name = "checked_in", nullable = false)
    private boolean checkedIn = false;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;
}
