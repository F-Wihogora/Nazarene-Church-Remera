package com.ncrde.church.service;

import com.ncrde.church.dto.MemberCreateDto;
import com.ncrde.church.dto.MemberDto;
import com.ncrde.church.entity.*;
import com.ncrde.church.repository.CampusRepository;
import com.ncrde.church.repository.FamilyRepository;
import com.ncrde.church.repository.MemberRepository;
import com.ncrde.church.repository.MemberSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Transactional(readOnly = true)
    public List<MemberDto> getAllMembers(String search, MemberStatus status, MemberCategory category, String gender, String campusCode) {
        Specification<Member> spec = Specification.where(MemberSpecifications.isNotDeleted())
                .and(MemberSpecifications.hasStatus(status))
                .and(MemberSpecifications.hasCategory(category))
                .and(MemberSpecifications.hasGender(gender))
                .and(MemberSpecifications.hasCampusCode(campusCode))
                .and(MemberSpecifications.searchByNameOrEmail(search));

        return memberRepository.findAll(spec).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));
        return convertToDto(member);
    }

    @Transactional
    public MemberDto createMember(MemberCreateDto createDto) {
        if (createDto.getEmail() != null && memberRepository.existsByEmail(createDto.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

        Campus campus = campusRepository.findByCode(createDto.getCampusCode().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Campus not found with code: " + createDto.getCampusCode()));

        Member member = new Member();
        member.setFirstName(createDto.getFirstName());
        member.setLastName(createDto.getLastName());
        member.setEmail(createDto.getEmail());
        member.setPhone(createDto.getPhone());
        member.setGender(createDto.getGender());
        member.setDob(createDto.getDob());
        member.setAddress(createDto.getAddress());
        member.setStatus(createDto.getStatus());
        member.setCategory(createDto.getCategory());
        member.setJoinedDate(createDto.getJoinedDate() != null ? createDto.getJoinedDate() : java.time.LocalDate.now());
        member.setCampus(campus);

        if (createDto.getFamilyId() != null) {
            Family family = familyRepository.findById(createDto.getFamilyId())
                    .orElseThrow(() -> new RuntimeException("Family not found with ID: " + createDto.getFamilyId()));
            member.setFamily(family);
            member.setFamilyRole(createDto.getFamilyRole());
        }

        Member savedMember = memberRepository.save(member);
        return convertToDto(savedMember);
    }

    @Transactional
    public MemberDto updateMember(Long id, MemberCreateDto updateDto) {
        Member member = memberRepository.findById(id)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));

        if (updateDto.getEmail() != null && !updateDto.getEmail().equalsIgnoreCase(member.getEmail())
                && memberRepository.existsByEmail(updateDto.getEmail())) {
            throw new RuntimeException("Email is already registered by another member!");
        }

        Campus campus = campusRepository.findByCode(updateDto.getCampusCode().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Campus not found with code: " + updateDto.getCampusCode()));

        member.setFirstName(updateDto.getFirstName());
        member.setLastName(updateDto.getLastName());
        member.setEmail(updateDto.getEmail());
        member.setPhone(updateDto.getPhone());
        member.setGender(updateDto.getGender());
        member.setDob(updateDto.getDob());
        member.setAddress(updateDto.getAddress());
        member.setStatus(updateDto.getStatus());
        member.setCategory(updateDto.getCategory());
        member.setCampus(campus);

        if (updateDto.getFamilyId() != null) {
            Family family = familyRepository.findById(updateDto.getFamilyId())
                    .orElseThrow(() -> new RuntimeException("Family not found with ID: " + updateDto.getFamilyId()));
            member.setFamily(family);
            member.setFamilyRole(updateDto.getFamilyRole());
        } else {
            member.setFamily(null);
            member.setFamilyRole(null);
        }

        Member updatedMember = memberRepository.save(member);
        return convertToDto(updatedMember);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + id));

        member.setDeleted(true);
        member.setDeletedAt(LocalDateTime.now());
        
        // If a linked user credentials account exists, disable it as well
        if (member.getUser() != null) {
            member.getUser().setEnabled(false);
        }
        
        memberRepository.save(member);
    }

    @Transactional
    public MemberDto linkFamily(Long memberId, Long familyId, FamilyRole familyRole) {
        Member member = memberRepository.findById(memberId)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));

        if (familyId == null) {
            member.setFamily(null);
            member.setFamilyRole(null);
        } else {
            Family family = familyRepository.findById(familyId)
                    .orElseThrow(() -> new RuntimeException("Family not found with ID: " + familyId));
            member.setFamily(family);
            member.setFamilyRole(familyRole);
        }

        return convertToDto(memberRepository.save(member));
    }

    public MemberDto convertToDto(Member member) {
        MemberDto dto = new MemberDto();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setGender(member.getGender());
        dto.setDob(member.getDob());
        dto.setAddress(member.getAddress());
        dto.setStatus(member.getStatus());
        dto.setCategory(member.getCategory());
        dto.setJoinedDate(member.getJoinedDate());

        if (member.getUser() != null) {
            dto.setUsername(member.getUser().getUsername());
            dto.setUserId(member.getUser().getId());
        }

        if (member.getFamily() != null) {
            dto.setFamilyId(member.getFamily().getId());
            dto.setFamilyName(member.getFamily().getName());
            dto.setFamilyRole(member.getFamilyRole());
        }

        if (member.getCampus() != null) {
            dto.setCampusCode(member.getCampus().getCode());
            dto.setCampusName(member.getCampus().getName());
        }

        return dto;
    }
}
