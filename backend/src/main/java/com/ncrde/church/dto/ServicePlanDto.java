package com.ncrde.church.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ServicePlanDto {
    private Long id;
    private Long eventId;
    private String eventTitle;
    private String title;
    
    private String openingPrayer;
    private String bibleReading;
    private String sermonTitle;
    private String offeringDetails;
    private String announcements;
    private String closingPrayer;
    
    private List<SongDto> songs = new ArrayList<>();
    private List<Long> songIds = new ArrayList<>();
}
