package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.entities.Tag;
import com.example.catalog.persistence.entities.Track;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.catalog.utilities.CalendarUtil.CALENDAR_PICKER_PATTERN_TIMESTAMP;

@Data
@NoArgsConstructor
public class TagDTO {

    private Long id;

    private String value;

    public TagDTO(final Tag tag) {
        this(tag, true);
    }

    public TagDTO(final Tag tag, final boolean createChildren) {
        this.id = tag.getId();
        this.value = tag.getValue();

    }
}
