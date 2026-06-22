package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "members", indexes = {
    @Index(name = "idx_members_email", columnList = "email"),
    @Index(name = "idx_members_first_name", columnList = "first_name"),
    @Index(name = "idx_members_last_name", columnList = "last_name"),
    @Index(name = "idx_members_status", columnList = "status"),
    @Index(name = "idx_members_category", columnList = "category")
})
@Getter
@Setter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 10)
    private String gender;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status = MemberStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberCategory category = MemberCategory.ADULT;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @Enumerated(EnumType.STRING)
    @Column(name = "family_role", length = 30)
    private FamilyRole familyRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

    public Member(String firstName, String lastName, String email, String phone, Campus campus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.campus = campus;
        this.status = MemberStatus.ACTIVE;
        this.category = MemberCategory.ADULT;
        this.joinedDate = LocalDate.now();
    }
}
