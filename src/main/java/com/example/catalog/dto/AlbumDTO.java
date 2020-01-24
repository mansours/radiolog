package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Album;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
//import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
public class AlbumDTO {

    private Long id;

    @NotNull(message = "Album name is required.")
    @Size(min = 1, max = 100, message = "Album name is required and must be less than twelve characters long.")
    private String name;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Calendar releaseDate;

    private String genre;

    @NotNull(message = "Number of tracks is required")
    @Min(value = 1, message = "Number of tracks required and must be at least one song")
    private Long numberOfTracks;

    private Long downloadCount;

    @NotNull(message = "Album label is required.")
    @Size(min = 1, max = 12, message = "Album label is required and must be less than twelve characters long.")
    private String label;

    private List<ArtistDTO> artists = new ArrayList<>(0);

    public AlbumDTO(final Album album) {
        this(album, true);
    }

    public AlbumDTO(final Album album, final boolean createChildren) {
        this.id = album.getId();
        this.name = album.getName();
        this.releaseDate = album.getReleaseDate();
        this.genre = album.getGenre();
        this.numberOfTracks = album.getNumberOfTracks();
        this.downloadCount = album.getDownloadCount();
        this.label = album.getLabel();

        if (createChildren) {
            this.artists = album.getArtists().stream()
                    .map(x -> new ArtistDTO(x, false))
                    .collect(Collectors.toList());
        }
    }

    public void mergeInto(final Album album) {
        album.setName(this.name);
        album.setReleaseDate(this.releaseDate);
        album.setGenre(this.genre);
        album.setNumberOfTracks(this.numberOfTracks);
        album.setDownloadCount(this.downloadCount);
        album.setLabel(this.label);
    }
}
