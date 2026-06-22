package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feature_flags", indexes = {
    @Index(name = "idx_flags_name", columnList = "flag_name")
})
@Getter
@Setter
@NoArgsConstructor
public class FeatureFlag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flag_name", unique = true, nullable = false, length = 100)
    private String flagName;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(length = 255)
    private String description;

    public FeatureFlag(String flagName, boolean enabled, String description) {
        this.flagName = flagName;
        this.enabled = enabled;
        this.description = description;
    }
}
