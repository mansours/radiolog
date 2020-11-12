package com.example.catalog.dto.search;

import com.example.catalog.persistence.entities.Show;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;

@Data
@EqualsAndHashCode(callSuper = true)
public class TrackSearchDTO extends SearchDTO {

    private String title;
    private String artist;
    private String album;
    private String label;
    private Calendar trackStart;
//    private Show show;



}

