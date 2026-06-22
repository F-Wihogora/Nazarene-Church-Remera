package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_center", indexes = {
    @Index(name = "idx_notif_center_read", columnList = "read_status"),
    @Index(name = "idx_notif_center_priority", columnList = "priority")
})
@Getter
@Setter
@NoArgsConstructor
public class NotificationCenter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_member_id")
    private Member recipient; // null means global broadcast

    @Column(name = "read_status", nullable = false)
    private boolean readStatus = false;

    @Column(nullable = false)
    private boolean archived = false;

    @Column(nullable = false, length = 30)
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, EMERGENCY

    @Column(name = "delivery_status", nullable = false, length = 30)
    private String deliveryStatus = "PENDING"; // PENDING, SENT, FAILED
}
