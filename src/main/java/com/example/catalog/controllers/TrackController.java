package com.example.catalog.controllers;

import com.example.catalog.dto.TrackDTO;
import com.example.catalog.dto.UserMessageDTO;
import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.entities.Track;
import com.example.catalog.persistence.services.ShowService;
import com.example.catalog.persistence.services.TrackService;
import com.example.catalog.service.MusicBrainzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/track")
public class TrackController {

    private final TrackService trackService;
    private final ShowService showService;
    private final MusicBrainzService musicBrainzService;

    @Autowired
    public TrackController(TrackService trackService, ShowService showService, MusicBrainzService musicBrainzService) {
        this.trackService = trackService;
        this.showService = showService;
        this.musicBrainzService = musicBrainzService;
    }

    @GetMapping("/view/{id}")
    public String editTrack(final ModelMap model, @PathVariable final Long id) {
        if (model.get("trackDTO") == null) {
            Track track = trackService.get(id);
            if (track == null)
                track = new Track();
            model.addAttribute("trackDTO", new TrackDTO(track));
        }
        return "/track/view";
    }

    @PostMapping("/save")
    public String saveTrack(final RedirectAttributes redirectAttributes,
                            @Valid final TrackDTO trackDTO,
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
        return "redirect:/show/view/" + show.getId();
    }

    //TODO: Method to add track to show using id.

    //TODO: Angela search track by artist, or figure out how to narrow down to a track
    @ResponseBody
    @GetMapping("/query")
    public Map<String,String> searchMBz(@RequestParam final String query) {
        //http://localhost/track/query?query=kesha
        //https://musicbrainz.org/ws/2/artist/?query=kesha&fmt=json
        //https://musicbrainz.org/doc/Development
        //https://musicbrainz.org/doc/MusicBrainz_API
        Map<String,String> demo = musicBrainzService.searchArtist(query);
        return demo;
    }

}
