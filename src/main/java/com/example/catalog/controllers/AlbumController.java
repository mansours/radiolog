package com.example.catalog.controllers;

import com.example.catalog.dto.AlbumDTO;
//import com.example.catalog.dto.ArtistDTO;
import com.example.catalog.dto.search.AlbumSearchDTO;
import com.example.catalog.persistence.services.AlbumService;
//import com.example.catalog.persistence.services.ArtistService;
//import com.example.catalog.persistence.services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

import static com.example.catalog.persistence.services.AlbumService.Column.LABEL;
import static com.example.catalog.persistence.services.AlbumService.Column.GENRE;
import static com.example.catalog.persistence.services.AlbumService.Column.NAME;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class AlbumController {

    //<editor-fold desc="Controller Services & Autowiring">
    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }
    //</editor-fold>

    @RequestMapping(method = {GET,POST}, value = "/album")
    public String album(final ModelMap model, final AlbumSearchDTO search) {

        //Get navigation and search parameters
        Sort.Direction dir = StringUtils.hasLength(search.getOrderDir()) ? Sort.Direction.valueOf(search.getOrderDir()) : Sort.Direction.ASC;
        AlbumService.Column sort = StringUtils.hasLength(search.getSortBy()) ? AlbumService.Column.valueOf(search.getSortBy()) : NAME;

        HashMap<AlbumService.Column, Object> filters = new HashMap<>();
        if (StringUtils.hasText(search.getName())) filters.put(NAME, search.getName());
        if (StringUtils.hasText(search.getGenre())) filters.put(GENRE, search.getGenre());
        if (StringUtils.hasText(search.getLabel())) filters.put(LABEL, search.getLabel());

        Page<AlbumDTO> list = albumService.get(filters, sort, dir, search.getPage(), search.getPageSize()).map(AlbumDTO::new);

        search.setOrderDir(dir.name());
        search.setSortBy(sort.name());
        search.setPageCount(list.getTotalPages());
        model.addAttribute("search", search);

        model.addAttribute("list", list);

        return "admin/albumList";


    }


}
