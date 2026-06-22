package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_templates", indexes = {
    @Index(name = "idx_templates_name", columnList = "template_name")
})
@Getter
@Setter
@NoArgsConstructor
public class NotificationTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", unique = true, nullable = false, length = 100)
    private String templateName; // e.g. WELCOME, BIRTHDAY_WISH, EXPENSE_APPROVED

    @Column(length = 200)
    private String subject;

    @Column(nullable = false, length = 2000)
    private String body; // Dynamic template body using placeholder tokens like {name}
}
