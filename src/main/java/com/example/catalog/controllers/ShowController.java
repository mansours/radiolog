package com.example.catalog.controllers;

import com.example.catalog.dto.AlbumDTO;
import com.example.catalog.dto.ShowDTO;
import com.example.catalog.dto.TrackDTO;
import com.example.catalog.dto.UserMessageDTO;
import com.example.catalog.dto.search.AlbumSearchDTO;
import com.example.catalog.dto.search.ShowSearchDTO;
import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.entities.Track;
import com.example.catalog.persistence.services.AlbumService;
import com.example.catalog.persistence.services.ShowService;
import com.example.catalog.persistence.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;

import static com.example.catalog.persistence.services.AlbumService.Column.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public class ShowController {
    private final ShowService showService;
    private final TrackService trackService;

    @Autowired
    public ShowController(ShowService showService, TrackService trackService){
        this.showService = showService;
        this.trackService = trackService;
    }

    @RequestMapping(method = {GET, POST}, value = "/album")
    public String showList(final ModelMap model, final ShowSearchDTO search) {

        //Get navigation and search parameters
        Sort.Direction dir = StringUtils.hasLength(search.getOrderDir()) ? Sort.Direction.valueOf(search.getOrderDir()) : Sort.Direction.ASC;
        ShowService.Column sort = StringUtils.hasLength(search.getSortBy()) ? ShowService.Column.valueOf(search.getSortBy()) : ShowService.Column.TIME;

        HashMap<ShowService.Column, Object> filters = new HashMap<>();
        if (StringUtils.hasText(search.getTitle())) filters.put(ShowService.Column.TITLE, search.getTitle());
        if (StringUtils.hasText(search.getProgrammer())) filters.put(ShowService.Column.PROGRAMMER, search.getProgrammer());

//        Page<AlbumDTO> list = showService.get(filters, sort, dir, search.getPage(), search.getPageSize()).map(ShowDTO::new);
//
//        search.setOrderDir(dir.name());
//        search.setSortBy(sort.name());
//        search.setPageCount(list.getTotalPages());
//        model.addAttribute("search", search);
//
//        model.addAttribute("list", list);

        return "/showList";
    }
    @RequestMapping(method = GET, value = "/show/{id}")
    public String editShow(final ModelMap model, @PathVariable("id") final Long id){
        if(model.get("showDTO") == null){
            Show show = showService.get(id);
            if(show == null)
                show = new Show();
            model.addAttribute("showDTO", new ShowDTO(show));
        }
        return "/showEdit";
    }
    @RequestMapping(method = POST, value = "/show/save")
    public String saveShow(final RedirectAttributes redirectAttributes, @Valid final ShowDTO showDTO,
                            final BindingResult bindingResult){
        Show dbShow = (showDTO.getId() == null) ? new Show() : showService.get(showDTO.getId());

        showDTO.mergeInto(dbShow);

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("trackDTO", new TrackDTO(dbTrack));
            redirectAttributes.addFlashAttribute("org.springframework.validation.Binding.trackDTO", bindingResult);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Track not saved. Correct the errors", UserMessageDTO.SEVERITY_ERROR));
        }
        else {
            dbTrack = trackService.save(dbTrack);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Successfully saved track", UserMessageDTO.SEVERITY_SUCCESS));

        }
        return "redirect:/track/" + (dbTrack.getId() == null ? -1 : dbTrack.getId());
    }



}
