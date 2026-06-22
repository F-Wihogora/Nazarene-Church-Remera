package com.ncrde.church.controller;

import com.ncrde.church.dto.GlobalSearchResultDto;
import com.ncrde.church.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GlobalSearchResultDto> globalSearch(@RequestParam String q) {
        GlobalSearchResultDto result = searchService.searchAll(q);
        return ResponseEntity.ok(result);
    }
}
