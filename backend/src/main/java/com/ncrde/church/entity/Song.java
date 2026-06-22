package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "songs", indexes = {
    @Index(name = "idx_songs_title", columnList = "title"),
    @Index(name = "idx_songs_key", columnList = "song_key")
})
@Getter
@Setter
@NoArgsConstructor
public class Song extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "song_key", length = 10)
    private String songKey;

    @Column(name = "bpm")
    private Integer bpm;

    @Column(length = 3000)
    private String lyrics;

    @Column(length = 50)
    private String category; // PRAISE, WORSHIP, HYMN, OTHER

    public Song(String title, String songKey, Integer bpm, String lyrics, String category) {
        this.title = title;
        this.songKey = songKey;
        this.bpm = bpm;
        this.lyrics = lyrics;
        this.category = category;
    }
}
