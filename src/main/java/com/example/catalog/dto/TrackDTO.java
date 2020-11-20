package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.entities.Tag;
import com.example.catalog.persistence.entities.Track;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class TrackDTO {

    //track_id, show_id, track_start, track_end, artist, album, label, title, language, tags
    private Long id;

    private ShowDTO show;
    private Long showId;

    private String artist;

    private String album;

    private String label;

    @NotNull(message = "Title for this track is required.")
    // @Size(min = 1, max = 128, message = "Track title is required and must be less than 128 characters long")
    private String title;

    @NotNull(message = "Language of this track is required.")
    private String language;

    // TODO: what do i use as pattern
    @DateTimeFormat(pattern = "")
    private Calendar trackStart;

    @DateTimeFormat(pattern = "")
    private Calendar trackEnd;

    private Set<Tag> tags = new HashSet<>(0);

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
        this.tags = track.getTags();

        if (createChildren) {
            this.show = new ShowDTO(track.getShow(), false);
            this.showId = track.getShow().getId();
        }
    }

    public void mergeInto(final Track track, final Show show) {
        track.setTitle(this.title);
        track.setArtist(this.artist);
        track.setAlbum(this.album);
        track.setLabel(this.label);
        track.setLanguage(this.language);
        track.setTrackStart(this.trackStart);
        track.setTrackEnd(this.trackEnd);
        track.setTags(this.tags);
        track.setShow(show);
    }

}
