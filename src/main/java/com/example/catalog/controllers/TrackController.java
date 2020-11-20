package com.example.catalog.controllers;

import com.example.catalog.dto.TrackDTO;
import com.example.catalog.dto.UserMessageDTO;
import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.entities.Track;
import com.example.catalog.persistence.services.ShowService;
import com.example.catalog.persistence.services.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller("/track")
public class TrackController {

    private final TrackService trackService;
    private final ShowService showService;

    @Autowired
    public TrackController(TrackService trackService, ShowService showService) {
        this.trackService = trackService;
        this.showService = showService;
    }

    @GetMapping("/view/{id}")
    public String editTrack(final ModelMap model, @PathVariable("id") final Long id) {
        if (model.get("trackDTO") == null) {
            Track track = trackService.get(id);
            if (track == null)
                track = new Track();
            model.addAttribute("trackDTO", new TrackDTO(track));
        }
        return "/trackEdit";
    }

    @PostMapping("/save")
    public String saveTrack(final RedirectAttributes redirectAttributes, @Valid final TrackDTO trackDTO,
                            final BindingResult bindingResult) {
        Track dbTrack = (trackDTO.getId() == null) ? new Track() : trackService.get(trackDTO.getId());

        Show show = showService.get(trackDTO.getShowId());
        trackDTO.mergeInto(dbTrack, show);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("trackDTO", new TrackDTO(dbTrack));
            redirectAttributes.addFlashAttribute("org.springframework.validation.Binding.trackDTO", bindingResult);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Track not saved. Correct the errors", UserMessageDTO.SEVERITY_ERROR));
        } else {
            dbTrack = trackService.save(dbTrack);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Successfully saved track", UserMessageDTO.SEVERITY_SUCCESS));

        }
        return "redirect:/track/" + (dbTrack.getId() == null ? -1 : dbTrack.getId());
    }

    //TODO: Method to add track to show using id.

    //TODO: Angela search track by artist, or figure out how to narrow down to a track

}
