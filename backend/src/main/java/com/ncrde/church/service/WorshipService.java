package com.ncrde.church.service;

import com.ncrde.church.dto.ServicePlanDto;
import com.ncrde.church.dto.SongDto;
import com.ncrde.church.entity.Event;
import com.ncrde.church.entity.ServicePlan;
import com.ncrde.church.entity.Song;
import com.ncrde.church.repository.EventRepository;
import com.ncrde.church.repository.ServicePlanRepository;
import com.ncrde.church.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorshipService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ServicePlanRepository servicePlanRepository;

    @Autowired
    private EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<SongDto> getAllSongs(String search) {
        List<Song> songs;
        if (search != null && !search.isBlank()) {
            songs = songRepository.findByTitleContainingIgnoreCase(search);
        } else {
            songs = songRepository.findAll();
        }
        return songs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public SongDto createSong(SongDto dto) {
        Song song = new Song(dto.getTitle(), dto.getSongKey(), dto.getBpm(), dto.getLyrics(), dto.getCategory());
        Song saved = songRepository.save(song);
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public ServicePlanDto getPlanByEventId(Long eventId) {
        return servicePlanRepository.findByEventId(eventId)
                .map(this::convertToDto)
                .orElseGet(() -> {
                    Event event = eventRepository.findById(eventId)
                            .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
                    ServicePlanDto dto = new ServicePlanDto();
                    dto.setEventId(event.getId());
                    dto.setEventTitle(event.getTitle());
                    dto.setTitle(event.getTitle() + " - Order of Service");
                    return dto;
                });
    }

    @Transactional
    public ServicePlanDto saveServicePlan(ServicePlanDto dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + dto.getEventId()));

        ServicePlan plan = servicePlanRepository.findByEventId(dto.getEventId())
                .orElseGet(() -> {
                    ServicePlan p = new ServicePlan();
                    p.setEvent(event);
                    return p;
                });

        plan.setTitle(dto.getTitle());
        plan.setOpeningPrayer(dto.getOpeningPrayer());
        plan.setBibleReading(dto.getBibleReading());
        plan.setSermonTitle(dto.getSermonTitle());
        plan.setOfferingDetails(dto.getOfferingDetails());
        plan.setAnnouncements(dto.getAnnouncements());
        plan.setClosingPrayer(dto.getClosingPrayer());

        if (dto.getSongIds() != null) {
            List<Song> songs = songRepository.findAllById(dto.getSongIds());
            plan.setSongs(songs);
        }

        ServicePlan saved = servicePlanRepository.save(plan);
        return convertToDto(saved);
    }

    private SongDto convertToDto(Song song) {
        SongDto dto = new SongDto();
        dto.setId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setSongKey(song.getSongKey());
        dto.setBpm(song.getBpm());
        dto.setLyrics(song.getLyrics());
        dto.setCategory(song.getCategory());
        return dto;
    }

    private ServicePlanDto convertToDto(ServicePlan plan) {
        ServicePlanDto dto = new ServicePlanDto();
        dto.setId(plan.getId());
        dto.setEventId(plan.getEvent().getId());
        dto.setEventTitle(plan.getEvent().getTitle());
        dto.setTitle(plan.getTitle());
        dto.setOpeningPrayer(plan.getOpeningPrayer());
        dto.setBibleReading(plan.getBibleReading());
        dto.setSermonTitle(plan.getSermonTitle());
        dto.setOfferingDetails(plan.getOfferingDetails());
        dto.setAnnouncements(plan.getAnnouncements());
        dto.setClosingPrayer(plan.getClosingPrayer());
        
        dto.setSongs(plan.getSongs().stream().map(this::convertToDto).collect(Collectors.toList()));
        dto.setSongIds(plan.getSongs().stream().map(Song::getId).collect(Collectors.toList()));
        return dto;
    }
}
