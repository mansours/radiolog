package com.example.catalog.dto;

import com.example.catalog.persistence.entities.Artist;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Calendar;

@Data
@NoArgsConstructor
public class ArtistDTO {

    private Long id;

    @NotNull(message = "Artist name is required.")
    @Size(min = 1, max = 12, message = "Artist name is required and must be less than twelve characters long.")
    private String name;

    @NotNull(message = "Artist label is required.")
    @Size(min = 1, max = 12, message = "Artist label is required and must be less than twelve characters long.")
    private String label;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Calendar dateOfBirth;

    @Size(max = 16, message = "Artist gender must be sixteen or less characters long.")
    private String gender;

    public ArtistDTO(final Artist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
        this.label = artist.getLabel();
        this.gender = artist.getGender();
        this.dateOfBirth = artist.getDateOfBirth();
    }

    public void mergeInto(final Artist artist) {
        artist.setName(this.name);
        artist.setLabel(this.label);
        artist.setDateOfBirth(this.dateOfBirth);
        artist.setGender(this.gender);
    }
}
