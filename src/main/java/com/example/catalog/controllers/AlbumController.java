package com.example.catalog.controllers;

import com.example.catalog.dto.AlbumDTO;
import com.example.catalog.dto.UserMessageDTO;
import com.example.catalog.dto.search.AlbumSearchDTO;
import com.example.catalog.persistence.entities.Album;
import com.example.catalog.persistence.entities.Artist;
import com.example.catalog.persistence.services.AlbumService;
import com.example.catalog.persistence.services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;

import static com.example.catalog.dto.UserMessageDTO.SEVERITY_ERROR;
import static com.example.catalog.dto.UserMessageDTO.SEVERITY_SUCCESS;
import static com.example.catalog.persistence.services.AlbumService.Column.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class AlbumController {

    //<editor-fold desc="Controller Services & Autowiring">
    private final AlbumService albumService;
    private final ArtistService artistService;

    @Autowired
    public AlbumController(AlbumService albumService, ArtistService artistService) {
        this.albumService = albumService;
        this.artistService = artistService;
    }
    //</editor-fold>

    @RequestMapping(method = {GET, POST}, value = "/album")
    public String albumList(final ModelMap model, final AlbumSearchDTO search) {

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


    @RequestMapping(method = GET, value = "/album/{id}")
    public String editAlbum(final ModelMap model,
                            @PathVariable("id") final Long id) {

        // If the save controller method below has a validation failure, it will redirect to this method with the model populated with corrections already.
        if (model.get("albumDTO") == null) {
            Album album = albumService.get(id);
            if (album == null)
                album = new Album(); //Negative or non existent ids will Create New

            model.addAttribute("albumDTO", new AlbumDTO(album));
        }

        return "admin/albumEdit";
    }

    @RequestMapping(method = POST, value = "/album/save")
    public String saveAlbum(final RedirectAttributes redirectAttributes,
                            @Valid final AlbumDTO albumDTO,
                            final BindingResult bindingResult) {
        Album dbAlbum = (albumDTO.getId() == null)
                ? new Album() //New
                : albumService.get(albumDTO.getId()); //Existing

        albumDTO.mergeInto(dbAlbum);

        if (bindingResult.hasErrors()) { //Send it back to view with errors
            redirectAttributes.addFlashAttribute("albumDTO", new AlbumDTO(dbAlbum));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.albumDTO", bindingResult);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Album not saved, you must correct the errors below.", SEVERITY_ERROR));
        } else { //Save and return
            dbAlbum = albumService.save(dbAlbum);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Successfully saved the Album.", SEVERITY_SUCCESS));
        }

        return "redirect:/album/" + (dbAlbum.getId() == null ? -1 : dbAlbum.getId());
    }

    @RequestMapping(method = POST, value = "/album/add/artist")
    public String addArtist(final ModelMap model,
                            @RequestParam(value = "albumId") final Long albumId,
                            @RequestParam(value = "artistId") final Long artistId) {
        Album album = albumService.get(albumId);
        Artist artist = artistService.get(artistId);

        if (album == null || artist == null) {
            model.addAttribute("userMessageDTO", new UserMessageDTO("Artist or Album does not exist.", SEVERITY_ERROR));
        } else {
            album.getArtists().add(artist);
            albumService.save(album);
        }

        return "redirect:/album/" + albumId;
    }

}
