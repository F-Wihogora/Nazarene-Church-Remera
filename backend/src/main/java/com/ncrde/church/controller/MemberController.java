package com.ncrde.church.controller;

import com.ncrde.church.dto.MemberCreateDto;
import com.ncrde.church.dto.MemberDto;
import com.ncrde.church.dto.MessageResponse;
import com.ncrde.church.entity.FamilyRole;
import com.ncrde.church.entity.MemberCategory;
import com.ncrde.church.entity.MemberStatus;
import com.ncrde.church.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR', 'SECRETARY', 'TREASURER')")
    public ResponseEntity<List<MemberDto>> getAllMembers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) MemberStatus status,
            @RequestParam(required = false) MemberCategory category,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String campusCode) {
        
        List<MemberDto> members = memberService.getAllMembers(search, status, category, gender, campusCode);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR', 'SECRETARY', 'TREASURER')")
    public ResponseEntity<MemberDto> getMemberById(@PathVariable Long id) {
        MemberDto member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY')")
    public ResponseEntity<MemberDto> createMember(@Valid @RequestBody MemberCreateDto createDto) {
        MemberDto member = memberService.createMember(createDto);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY')")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @Valid @RequestBody MemberCreateDto updateDto) {
        MemberDto member = memberService.updateMember(id, updateDto);
        return ResponseEntity.ok(member);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(new MessageResponse("Member soft-deleted successfully."));
    }

    @PostMapping("/{id}/link-family")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARY')")
    public ResponseEntity<MemberDto> linkFamily(
            @PathVariable Long id,
            @RequestParam(required = false) Long familyId,
            @RequestParam(required = false) FamilyRole familyRole) {
        
        MemberDto member = memberService.linkFamily(id, familyId, familyRole);
        return ResponseEntity.ok(member);
    }
}
