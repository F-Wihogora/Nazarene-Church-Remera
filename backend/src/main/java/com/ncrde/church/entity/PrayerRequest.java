package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prayer_requests", indexes = {
    @Index(name = "idx_prayer_status", columnList = "status"),
    @Index(name = "idx_prayer_anonymous", columnList = "anonymous")
})
@Getter
@Setter
@NoArgsConstructor
public class PrayerRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private boolean anonymous = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PrayerRequestStatus status = PrayerRequestStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
