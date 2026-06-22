package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cell_groups", indexes = {
    @Index(name = "idx_cell_groups_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
public class CellGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private Member leader;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cell_group_members",
        joinColumns = @JoinColumn(name = "cell_group_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<Member> members = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

    public CellGroup(String name, String location, Member leader, Campus campus) {
        this.name = name;
        this.location = location;
        this.leader = leader;
        this.campus = campus;
    }
}
