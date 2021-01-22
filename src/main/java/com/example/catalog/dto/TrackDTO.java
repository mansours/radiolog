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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.catalog.utilities.CalendarUtil.CALENDAR_PICKER_PATTERN_TIMESTAMP;

@Data
@NoArgsConstructor
public class TrackDTO {

    private Long id;

    @JsonIgnore
    private ShowDTO show;

    private Long showId; //For forms

    private String artist;

    private String album;

    private String label;

    @NotNull(message = "Title for this track is required.")
    // @Size(min = 1, max = 128, message = "Track title is required and must be less than 128 characters long")
    private String title;

    @NotNull(message = "Language of this track is required.")
    private String language;

    @DateTimeFormat(pattern = CALENDAR_PICKER_PATTERN_TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CALENDAR_PICKER_PATTERN_TIMESTAMP, timezone = "America/Toronto")
    private Calendar trackStart;

    @DateTimeFormat(pattern = CALENDAR_PICKER_PATTERN_TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CALENDAR_PICKER_PATTERN_TIMESTAMP, timezone = "America/Toronto")
    private Calendar trackEnd;

    private List<TagDTO> tags = new ArrayList<>(0);

    @JsonIgnore
    private List<Long> tagIds = new ArrayList<>(0); //Input from the edit track form

    public TrackDTO(final Track track) {
        this(track, true);
    }

    public TrackDTO(final Track track, final boolean createChildren) {
        this.id = track.getId();
        this.artist = track.getArtist();
        this.album = track.getAlbum();
        this.label = track.getLabel();
        this.title = track.getTitle();
        this.language = track.getLanguage();
        this.trackStart = track.getTrackStart();
        this.trackEnd = track.getTrackEnd();

        if (track.getShow() != null) this.showId = track.getShow().getId();
        if (track.getTags() != null)
            this.tags = track.getTags().stream()
                    .map(TagDTO::new)
                    .collect(Collectors.toList());

        if (createChildren) {
            if (track.getShow() != null)
                this.show = new ShowDTO(track.getShow(), false);
        }

    }

    public void mergeInto(final Track track, final Show show, final List<Tag> tags) {
        track.setTitle(this.title);
        track.setArtist(this.artist);
        track.setAlbum(this.album);
        track.setLabel(this.label);
        track.setLanguage(this.language);
        track.setTrackStart(this.trackStart);
        track.setTrackEnd(this.trackEnd);

        //Update Show
        track.setShow(show);

        //Update Tags
        track.getTags().retainAll(tags); //Intersect sets (delete ones not provided)
        track.getTags().addAll(tags); // Union sets (add ones from new set)
    }

    @JsonIgnore
    public String getTagsAsString() {
        if (tags == null) return "";

        return tags.stream()
                .map(TagDTO::getValue)
                .sorted()
                .collect(Collectors.joining(", "));
    }

}
