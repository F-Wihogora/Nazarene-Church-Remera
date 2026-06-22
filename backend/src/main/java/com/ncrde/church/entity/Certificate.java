package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "certificates", indexes = {
    @Index(name = "idx_cert_number", columnList = "certificate_number")
})
@Getter
@Setter
@NoArgsConstructor
public class Certificate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certificate_type", nullable = false, length = 50)
    private String certificateType; // DISCIPLESHIP_COMPLETION, BAPTISM, MEMBERSHIP, COUNSELING_COMPLETION

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "certificate_number", unique = true, nullable = false, length = 50)
    private String certificateNumber;

    @Column(name = "file_url", length = 255)
    private String fileUrl; // URL of generated PDF document
}
