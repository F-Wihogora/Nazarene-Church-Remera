package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "sermons", indexes = {
    @Index(name = "idx_sermons_date", columnList = "sermon_date"),
    @Index(name = "idx_sermons_preacher", columnList = "preacher")
})
@Getter
@Setter
@NoArgsConstructor
public class Sermon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 100)
    private String preacher;

    @Column(length = 100)
    private String scripture;

    @Column(name = "sermon_date", nullable = false)
    private LocalDate sermonDate;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(length = 1000)
    private String description;

    public Sermon(String title, String preacher, LocalDate sermonDate, String videoUrl, String scripture) {
        this.title = title;
        this.preacher = preacher;
        this.sermonDate = sermonDate;
        this.videoUrl = videoUrl;
        this.scripture = scripture;
    }
}
