package com.ncrde.church.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SongDto {
    private Long id;
    private String title;
    private String songKey;
    private Integer bpm;
    private String lyrics;
    private String category;
}
