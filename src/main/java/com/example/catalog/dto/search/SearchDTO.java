package com.example.catalog.dto.search;

import lombok.Data;

@Data
public class SearchDTO {

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private Integer page = DEFAULT_PAGE;
    private Integer pageSize = DEFAULT_PAGE_SIZE;
    private Integer pageCount;
    private String orderDir;
    private String sortBy;
}
