package com.ncrde.church.controller;

import com.ncrde.church.dto.ServicePlanDto;
import com.ncrde.church.dto.SongDto;
import com.ncrde.church.service.WorshipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WorshipController {

    @Autowired
    private WorshipService worshipService;

    @GetMapping("/public/worship/songs")
    public ResponseEntity<List<SongDto>> getAllSongs(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(worshipService.getAllSongs(search));
    }

    @PostMapping("/worship/songs")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR', 'MEDIA_TEAM')")
    public ResponseEntity<SongDto> createSong(@Valid @RequestBody SongDto songDto) {
        return ResponseEntity.ok(worshipService.createSong(songDto));
    }

    @GetMapping("/worship/plans/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ServicePlanDto> getPlanByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(worshipService.getPlanByEventId(eventId));
    }

    @PostMapping("/worship/plans")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR', 'MEDIA_TEAM')")
    public ResponseEntity<ServicePlanDto> saveServicePlan(@Valid @RequestBody ServicePlanDto planDto) {
        return ResponseEntity.ok(worshipService.saveServicePlan(planDto));
    }
}
