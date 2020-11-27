package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Show;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.catalog.utilities.CalendarUtil.CALENDAR_PICKER_PATTERN_TIMESTAMP;

@Data
@NoArgsConstructor
public class ShowDTO {

    private Long id;

    @NotNull(message = "Show title is required")
    private String title;

    private String programmer;

    private String email;

    private String guests;

    @NotNull(message = "Language of this show is required.")
    private String language;

    private String code;

    @DateTimeFormat(pattern = CALENDAR_PICKER_PATTERN_TIMESTAMP)
    private Calendar showTimestamp;

    private List<TrackDTO> tracks = new ArrayList<>(0);

    public ShowDTO(final Show show) {
        this(show, true);
    }

    public ShowDTO(final Show show, final boolean createChildren) {
        this.id = show.getId();
        this.title = show.getTitle();
        this.programmer = show.getProgrammer();
        this.email = show.getEmail();
        this.showTimestamp = show.getShowTimestamp();
        this.code = show.getCode();
        this.guests = show.getGuests();
        this.language = show.getLanguage();

        if (createChildren) {
            this.tracks = show.getTracks().stream()
                    .map(x -> new TrackDTO(x, false))
                    .collect(Collectors.toList());
        }

    }

    public void mergeInto(final Show show) {
        show.setId(this.id);
        show.setTitle(this.title);
        show.setProgrammer(this.programmer);
        show.setEmail(this.email);
        show.setShowTimestamp(this.showTimestamp);
        show.setCode(this.code);
        show.setGuests(this.guests);
        show.setLanguage(this.language);

    }


}
