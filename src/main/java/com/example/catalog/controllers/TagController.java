package com.example.catalog.controllers;

import com.example.catalog.dto.select2.SelectOptionDTO;
import com.example.catalog.persistence.entities.Tag;
import com.example.catalog.persistence.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.catalog.persistence.services.TagService.Column.VALUE;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Controller
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @ResponseBody
    @GetMapping("/search")
    public List<SelectOptionDTO> find(@RequestParam(required = false) String q) {

        List<Tag> tags = q == null ?
                tagService.get(Map.of(), VALUE, ASC) :
                tagService.get(Map.of(VALUE, q), VALUE, ASC);

        return tags.stream()
                .map(tag -> new SelectOptionDTO(tag.getId().toString(), tag.getValue()))
                .collect(Collectors.toList());
    }

}
