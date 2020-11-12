package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.entities.Track;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ShowDTO {

    private Long id;

    @NotNull(message = "Show title is required")
    private String title ;

    private String programmer;

    private String email;

    private String guests;

    @NotNull(message = "Language of this show is required.")
    private String language;

    private String code;

    private Calendar showTimestamp;

    private Set<Track> tracks = new HashSet<>(0);

    public ShowDTO(final Show show, final boolean createChildren){
        this.id = show.getId();
        this.title = show.getTitle();
        this.programmer = show.getProgrammer();
        this.email = show.getEmail();
        this.showTimestamp = show.getShowTimestamp();
        this.code = show.getCode();
        this.guests = show.getGuests();
        this.language = show.getLanguage();
        this.tracks = show.getTracks();

        if (createChildren) {
            this.tracks = show.getTracks().stream()
                    .map(x -> new TrackDTO(x, false))
                    .collect(Collectors.toList());
        }

    }
    public void mergeInto(final Show show){
        show.setId(this.id);
        show.setTitle(this.title);
        show.setProgrammer(this.programmer);
        show.setEmail(this.email);
        show.setShowTimestamp(this.showTimestamp);
        show.setCode(this.code);
        show.setGuests(this.guests);
        show.setLanguage(this.language);
        show.setTracks;

    }


}
