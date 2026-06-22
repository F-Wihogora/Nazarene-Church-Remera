package com.ncrde.church.service;

import com.ncrde.church.dto.GlobalSearchResultDto;
import com.ncrde.church.dto.MemberDto;
import com.ncrde.church.entity.Document;
import com.ncrde.church.entity.Event;
import com.ncrde.church.entity.Member;
import com.ncrde.church.entity.Sermon;
import com.ncrde.church.repository.DocumentRepository;
import com.ncrde.church.repository.EventRepository;
import com.ncrde.church.repository.MemberRepository;
import com.ncrde.church.repository.SermonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SermonRepository sermonRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private MemberService memberService;

    @Transactional(readOnly = true)
    public GlobalSearchResultDto searchAll(String query) {
        GlobalSearchResultDto result = new GlobalSearchResultDto();
        if (query == null || query.isBlank()) {
            return result;
        }

        // Search Members (matching name or email)
        List<Member> matchedMembers = memberRepository.findAll((root, q, cb) -> {
            String pattern = "%" + query.toLowerCase() + "%";
            return cb.and(
                cb.equal(root.get("deleted"), false),
                cb.or(
                    cb.like(cb.lower(root.get("firstName")), pattern),
                    cb.like(cb.lower(root.get("lastName")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
                )
            );
        });
        List<MemberDto> memberDtos = matchedMembers.stream()
                .map(memberService::convertToDto)
                .collect(Collectors.toList());
        result.setMembers(memberDtos);

        // Search Events (matching title or description)
        List<Event> matchedEvents = eventRepository.findAll().stream()
                .filter(e -> !e.isDeleted())
                .filter(e -> e.getTitle().toLowerCase().contains(query.toLowerCase()) 
                          || (e.getDescription() != null && e.getDescription().toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toList());
        result.setEvents(matchedEvents);

        // Search Sermons
        List<Sermon> matchedSermons = sermonRepository
                .findByTitleContainingIgnoreCaseOrPreacherContainingIgnoreCaseOrScriptureContainingIgnoreCase(
                        query, query, query);
        result.setSermons(matchedSermons);

        // Search Documents
        List<Document> matchedDocs = documentRepository.findByTitleContainingIgnoreCase(query);
        result.setDocuments(matchedDocs);

        return result;
    }
}
