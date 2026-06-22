package com.ncrde.church.controller;

import com.ncrde.church.dto.MessageResponse;
import com.ncrde.church.entity.*;
import com.ncrde.church.repository.*;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private SermonRepository sermonRepository;

    @Autowired
    private NotificationCenterRepository notificationCenterRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private CampusRepository campusRepository;

    @GetMapping("/sermons")
    public ResponseEntity<List<Sermon>> getPublicSermons() {
        return ResponseEntity.ok(sermonRepository.findAll());
    }

    @GetMapping("/announcements")
    public ResponseEntity<List<NotificationCenter>> getPublicAnnouncements() {
        // Fetch global announcements (where recipient is null)
        List<NotificationCenter> announcements = notificationCenterRepository.findAll().stream()
                .filter(n -> n.getRecipient() == null)
                .filter(n -> !n.isArchived())
                .toList();
        return ResponseEntity.ok(announcements);
    }

    @PostMapping("/join")
    public ResponseEntity<MessageResponse> joinChurch(@Valid @RequestBody GuestJoinRequest request) {
        Campus campus = campusRepository.findByCode(request.getCampusCode().toUpperCase())
                .orElseGet(() -> campusRepository.findAll().get(0));

        Visitor visitor = new Visitor();
        visitor.setFirstName(request.getFirstName());
        visitor.setLastName(request.getLastName());
        visitor.setEmail(request.getEmail());
        visitor.setPhone(request.getPhone());
        visitor.setVisitDate(LocalDate.now());
        visitor.setFollowUpStatus("PENDING");
        visitor.setCampus(campus);

        visitorRepository.save(visitor);

        return ResponseEntity.ok(new MessageResponse("Thank you for joining Nazarene Church Remera! We will contact you soon."));
    }
}

@Getter
@Setter
class GuestJoinRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String campusCode = "REMERA";
}
