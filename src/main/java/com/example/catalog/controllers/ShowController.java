package com.example.catalog.controllers;

import com.example.catalog.dto.ShowDTO;
import com.example.catalog.dto.UserMessageDTO;
import com.example.catalog.dto.search.AlbumSearchDTO;
import com.example.catalog.dto.search.ShowSearchDTO;
import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.services.ShowService;
import com.example.catalog.persistence.services.TrackService;
import com.example.catalog.service.MusicBrainzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/show")
public class ShowController {
    private final ShowService showService;
    private final TrackService trackService;
    private final MusicBrainzService musicBrainzService;

    @Autowired
    public ShowController(ShowService showService, TrackService trackService, MusicBrainzService musicBrainzService) {
        this.showService = showService;
        this.trackService = trackService;
        this.musicBrainzService = musicBrainzService;
    }

    @RequestMapping("/list")
    public String showList(final ModelMap model,
                           final ShowSearchDTO search) {
        //Get navigation and search parameters
        Sort.Direction dir = StringUtils.hasLength(search.getOrderDir()) ? Sort.Direction.valueOf(search.getOrderDir()) : Sort.Direction.ASC;
        ShowService.Column sort = StringUtils.hasLength(search.getSortBy()) ? ShowService.Column.valueOf(search.getSortBy()) : ShowService.Column.TIME;

        HashMap<ShowService.Column, Object> filters = new HashMap<>();
        if (StringUtils.hasText(search.getTitle())) filters.put(ShowService.Column.TITLE, search.getTitle());
        if (StringUtils.hasText(search.getProgrammer()))
            filters.put(ShowService.Column.PROGRAMMER, search.getProgrammer());

        Page<ShowDTO> list = showService.get(filters, sort, dir, search.getPage(), search.getPageSize()).map(ShowDTO::new);

        search.setOrderDir(dir.name());
        search.setSortBy(sort.name());
        search.setPageCount(list.getTotalPages());
        model.addAttribute("search", search);

        model.addAttribute("list", list);

        return "show/list";
    }

    @GetMapping("/view/{id}")
    public String editShow(final ModelMap model,
                           @PathVariable final Long id) {
        if (model.get("showDTO") == null) {
            Show show = showService.get(id);
            if (show == null)
                show = new Show();
            model.addAttribute("showDTO", new ShowDTO(show));
        }
        return "show/view";
    }

    @PostMapping("/save")
    public String saveShow(final RedirectAttributes redirectAttributes,
                           @Valid final ShowDTO showDTO,
                           final BindingResult bindingResult) {
        Show dbShow = (showDTO.getId() == null) ? new Show() : showService.get(showDTO.getId());

        showDTO.mergeInto(dbShow);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("showDTO", new ShowDTO(dbShow));
            redirectAttributes.addFlashAttribute("org.springframework.validation.Binding.showDTO", bindingResult);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Show not saved. Correct the errors", UserMessageDTO.SEVERITY_ERROR));
        } else {
            dbShow = showService.save(dbShow);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Successfully saved show", UserMessageDTO.SEVERITY_SUCCESS));
        }
        return "redirect:/show/view/" + (dbShow.getId() == null ? -1 : dbShow.getId());
    }

    @GetMapping("/test")
    public String editShow(final ModelMap model, String title) {
        Map<String,String> demo = musicBrainzService.searchArtist(title);
        model.addAttribute("hi", demo.keySet());
        return "show/test";
    }



//    @PostMapping("/test")
//    public String showShow(final ModelMap model, String percentage){
//
//    }



}
