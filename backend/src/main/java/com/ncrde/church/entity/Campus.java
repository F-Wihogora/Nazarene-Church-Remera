package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "campuses", indexes = {
    @Index(name = "idx_campuses_code", columnList = "code")
})
@Getter
@Setter
@NoArgsConstructor
public class Campus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String location;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    public Campus(String name, String location, String code) {
        this.name = name;
        this.location = location;
        this.code = code;
    }
}
