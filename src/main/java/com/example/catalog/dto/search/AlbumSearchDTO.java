package com.example.catalog.dto.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumSearchDTO extends SearchDTO {

    private String name;
    private String genre;
    private String label;
}
