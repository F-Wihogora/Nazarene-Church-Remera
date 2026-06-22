package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "form_fields")
@Getter
@Setter
@NoArgsConstructor
public class FormField extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private FormTemplate template;

    @Column(nullable = false, length = 150)
    private String label;

    @Column(name = "field_type", nullable = false, length = 30)
    private String fieldType; // TEXT, NUMBER, EMAIL, SELECT, CHECKBOX

    @Column(nullable = false)
    private boolean required = false;

    @Column(length = 150)
    private String placeholder;

    @Column(name = "options_json", length = 1000)
    private String optionsJson; // Dropdown choices, e.g. ["Male", "Female"]
}
