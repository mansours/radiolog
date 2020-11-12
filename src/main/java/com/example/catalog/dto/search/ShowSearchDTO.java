package com.example.catalog.dto.search;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShowSearchDTO extends SearchDTO {

    private String title;
    private String programmer;
    private String email;
    private String guests;
    private String code;
    private Calendar showTimestamp;
//    private Show show;



}
