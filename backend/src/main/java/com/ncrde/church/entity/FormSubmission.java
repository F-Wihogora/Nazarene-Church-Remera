package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "form_submissions")
@Getter
@Setter
@NoArgsConstructor
public class FormSubmission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private FormTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by_member_id")
    private Member member; // Nullable if submitted anonymously or by guest visitor

    @Column(name = "answers_json", nullable = false, length = 4000)
    private String answersJson; // JSON representation of user submissions: { "first_name": "Alice", "age": 28 }
}
