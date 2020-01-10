package com.example.catalog.controllers;

import com.example.catalog.dto.ArtistDTO;
import com.example.catalog.dto.UserMessageDTO;
import com.example.catalog.dto.search.ArtistSearchDTO;
import com.example.catalog.dto.select2.SelectOptionDTO;
import com.example.catalog.persistence.entities.Artist;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.catalog.dto.UserMessageDTO.SEVERITY_ERROR;
import static com.example.catalog.dto.UserMessageDTO.SEVERITY_SUCCESS;
import static com.example.catalog.persistence.services.ArtistService.Column.LABEL;
import static com.example.catalog.persistence.services.ArtistService.Column.NAME;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ArtistController {

    //<editor-fold desc="Controller Services & Autowiring">
    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }
    //</editor-fold>

    @RequestMapping(method = {GET, POST}, value = "/artist")
    public String artistList(final ModelMap model,
                             final ArtistSearchDTO search) {

        //Get navigation and search parameters
        Sort.Direction dir = StringUtils.hasLength(search.getOrderDir()) ? Sort.Direction.valueOf(search.getOrderDir()) : Sort.Direction.ASC;
        ArtistService.Column sort = StringUtils.hasLength(search.getSortBy()) ? ArtistService.Column.valueOf(search.getSortBy()) : NAME;

        HashMap<ArtistService.Column, Object> filters = new HashMap<>();
        if (StringUtils.hasText(search.getName())) filters.put(NAME, search.getName());
        if (StringUtils.hasText(search.getLabel())) filters.put(LABEL, search.getLabel());

        Page<ArtistDTO> list = artistService.get(filters, sort, dir, search.getPage(), search.getPageSize()).map(ArtistDTO::new);

        search.setOrderDir(dir.name());
        search.setSortBy(sort.name());
        search.setPageCount(list.getTotalPages());
        model.addAttribute("search", search);

        model.addAttribute("list", list);

        return "admin/artistList";
    }

    @RequestMapping(method = GET, value = "/artist/{id}")
    public String editArtist(final ModelMap model,
                             @PathVariable("id") final Long id) {

        // If the save controller method below has a validation failure, it will redirect to this method with the model populated with corrections already.
        if (model.get("artistDTO") == null) {
            Artist artist = artistService.get(id);
            if (artist == null)
                artist = new Artist(); //Negative or non existent ids will Create New

            model.addAttribute("artistDTO", new ArtistDTO(artist));
        }

        return "admin/artistEdit";
    }

    @RequestMapping(method = POST, value = "/artist/save")
    public String saveArtist(final RedirectAttributes redirectAttributes,
                             @Valid final ArtistDTO artistDTO,
                             final BindingResult bindingResult) {
        Artist dbArtist = (artistDTO.getId() == null)
                ? new Artist() //New
                : artistService.get(artistDTO.getId()); //Existing

        artistDTO.mergeInto(dbArtist);

        if (bindingResult.hasErrors()) { //Send it back to view with errors
            redirectAttributes.addFlashAttribute("artistDTO", new ArtistDTO(dbArtist));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.artistDTO", bindingResult);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Artist not saved, you must correct the errors below.", SEVERITY_ERROR));
        } else { //Save and return
            dbArtist = artistService.save(dbArtist);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Successfully saved the Artist.", SEVERITY_SUCCESS));
        }

        return "redirect:/artist/" + (dbArtist.getId() == null ? -1 : dbArtist.getId());
    }

    @ResponseBody
    @RequestMapping(method = GET, value = "/artist/search")
    public List<SelectOptionDTO> searchArtist(final RedirectAttributes redirectAttributes,
                                              @RequestParam(value = "q") final String q) {
        if (!StringUtils.hasText(q) && q.length() > 2) return Collections.emptyList();

        return artistService.get(Map.of(NAME, q), NAME, Sort.Direction.ASC).stream()
                .map(artist -> new SelectOptionDTO(artist.getId().toString(), artist.getName()))
                .collect(Collectors.toList());
    }

}