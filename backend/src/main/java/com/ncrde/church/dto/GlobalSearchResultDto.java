package com.ncrde.church.dto;

import com.ncrde.church.entity.Document;
import com.ncrde.church.entity.Event;
import com.ncrde.church.entity.Sermon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GlobalSearchResultDto {
    private List<MemberDto> members = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Sermon> sermons = new ArrayList<>();
    private List<Document> documents = new ArrayList<>();
}
