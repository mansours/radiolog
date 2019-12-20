package com.example.catalog.dto.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArtistSearchDTO extends SearchDTO {

    private String label;
    private String name;
}
