package com.example.catalog.controllers;

import com.example.catalog.dto.TrackDTO;
import com.example.catalog.dto.UserMessageDTO;
import com.example.catalog.dto.search.TrackSearchDTO;

import com.example.catalog.persistence.entities.Track;

import com.example.catalog.persistence.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


import java.util.HashMap;


import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class TrackController {

    private final TrackService trackService;

    @Autowired
    public TrackController (TrackService trackService){
        this.trackService = trackService;
    }

    @RequestMapping(method = {GET,POST}, value = "/track")
    public String trackList(final ModelMap model, final TrackSearchDTO search){
        Sort.Direction dir = StringUtils.hasLength(search.getOrderDir()) ? Sort.Direction.valueOf(search.getOrderDir()) : Sort.Direction.ASC;
        TrackService.Column sort = StringUtils.hasLength(search.getSortBy()) ? TrackService.Column.valueOf(search.getSortBy()) : TrackService.Column.TRACKSTART;

        HashMap<TrackService.Column, Object> filters = new HashMap<>();
        if (StringUtils.hasText(search.getTitle())) filters.put(TrackService.Column.TITLE, search.getTitle());
        if (StringUtils.hasText(search.getArtist())) filters.put(TrackService.Column.ARTIST, search.getArtist());
        if (StringUtils.hasText(search.getLabel())) filters.put(TrackService.Column.LABEL, search.getLabel());

      /* Page<TrackDTO> list = trackService.get(filters, sort, dir, search.getPage(), search.getPageSize()).map(TrackDTO::new);
        search.setOrderDir(dir.name());
        search.setSortBy(sort.name());
        search.setPageCount(list.getTotalPages());
        model.addAttribute("search", search);

        model.addAttribute("list", list);*/

        return "/trackList";
    }

    @RequestMapping(method = GET, value = "/track/{id}")
    public String editTrack(final ModelMap model, @PathVariable("id") final Long id){
        if(model.get("trackDTO") == null){
            Track track = trackService.get(id);
            if(track == null)
                track = new Track();
            model.addAttribute("trackDTO", new TrackDTO(track));
        }
        return "/trackEdit";
    }
    @RequestMapping(method = POST, value = "/track/save")
    public String saveTrack(final RedirectAttributes redirectAttributes, @Valid final TrackDTO trackDTO,
                            final BindingResult bindingResult){
        Track dbTrack = (trackDTO.getId() == null) ? new Track() : trackService.get(trackDTO.getId());

        trackDTO.mergeInto(dbTrack);

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
