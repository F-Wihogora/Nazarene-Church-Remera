package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments", indexes = {
    @Index(name = "idx_appointments_date", columnList = "appointment_date"),
    @Index(name = "idx_appointments_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "appointment_type", nullable = false, length = 50)
    private String appointmentType; // MARRIAGE_COUNSELING, PRAYER_SESSION, BAPTISM_CLASS

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pastor_id", nullable = false)
    private Member pastor; // The pastor or elder officiating the session

    @Column(nullable = false, length = 30)
    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, CANCELLED
}
