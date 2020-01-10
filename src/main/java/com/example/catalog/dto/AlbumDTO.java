package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Album;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Calendar;
//import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
public class AlbumDTO {

    private Long id;

    @NotNull(message = "Album name is required.")
    @Size(min = 1, max = 100, message = "Album name is required and must be less than twelve characters long.")
    private String name;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Calendar date;

    private String genre;

    @NotNull (message = "Number of tracks is required")
    @Size (min = 1, message = "Number of tracks required and must be at least one song")
    private Long numberOfTracks;

    private Long downloadCount;

    @NotNull(message = "Album label is required.")
    @Size(min = 1, max = 12, message = "Album label is required and must be less than twelve characters long.")
    private String label;

    public AlbumDTO(final Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.date = album.getDate();
        this.genre = album.getGenre();
        this.numberOfTracks = album.getNumberOfTracks();
        this.downloadCount = album.getDownloadCount();
        this.label = album.getLabel();
    }

    public void mergeInto(final Album album) {
        album.setName(this.name);
        album.setDate(this.date);
        album.setGenre(this.genre);
        album.setNumberOfTracks(this.numberOfTracks);
        album.setDownloadCount(this.downloadCount);
        album.setLabel(this.label);
    }
}
