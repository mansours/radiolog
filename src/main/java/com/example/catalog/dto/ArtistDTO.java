package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Artist;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class ArtistDTO {
    private Long id;

    @NotNull
    @Size(max = 12, message = "Artist name must be less than twelve characters long.")
    private String name;

    @Size(max = 12, message = "Artist label must be less than twelve characters long.")
    private String label;

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
        this.label = artist.getLabel();
    }

    public void mergeInto(Artist artist) {
        artist.setName(this.name);
        artist.setLabel(this.label);
    }
}
