package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stream_sessions", indexes = {
    @Index(name = "idx_stream_live", columnList = "live")
})
@Getter
@Setter
@NoArgsConstructor
public class StreamSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 30)
    private String platform; // YOUTUBE, FACEBOOK

    @Column(name = "stream_url", nullable = false, length = 500)
    private String streamUrl;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(nullable = false)
    private boolean live = false;
}
