package com.example.catalog.service.dto;

import com.example.catalog.service.response.MusicBrainzArtistResp;
import lombok.Data;

@Data
public class MusicBrainzArtistDTO {

    private String id;
    private String name;

    public MusicBrainzArtistDTO(MusicBrainzArtistResp.MusicBrainzArtist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
    }
}
