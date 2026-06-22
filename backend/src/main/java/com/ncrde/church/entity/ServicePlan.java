package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_plans")
@Getter
@Setter
@NoArgsConstructor
public class ServicePlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "opening_prayer", length = 500)
    private String openingPrayer;

    @Column(name = "bible_reading", length = 255)
    private String bibleReading; // e.g. Romans 8:28-39

    @Column(name = "sermon_title", length = 255)
    private String sermonTitle;

    @Column(name = "offering_details", length = 500)
    private String offeringDetails;

    @Column(length = 1000)
    private String announcements;

    @Column(name = "closing_prayer", length = 500)
    private String closingPrayer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "service_plan_songs",
        joinColumns = @JoinColumn(name = "service_plan_id"),
        inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs = new ArrayList<>();
}
