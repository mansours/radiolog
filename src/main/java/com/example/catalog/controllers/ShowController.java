package com.example.catalog.controllers;

import com.example.catalog.dto.ShowDTO;
import com.example.catalog.dto.UserMessageDTO;
import com.example.catalog.dto.search.ShowSearchDTO;
import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.entities.Track;
import com.example.catalog.persistence.services.ShowService;
import com.example.catalog.service.MusicBrainzService;
import com.example.catalog.service.dto.MusicBrainzRecordingDTO;
import com.example.catalog.service.response.MusicBrainzRecordingResp;
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
import java.util.HashMap;

@Controller
@RequestMapping("/show")
public class ShowController {
    private final ShowService showService;
    private final MusicBrainzService musicBrainzService;

    @Autowired
    public ShowController(ShowService showService, MusicBrainzService musicBrainzService) {
        this.showService = showService;
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

    @PostMapping("/addTrack/fromMusicBrainz")
    public String addTrackFromMusicBrainz(final RedirectAttributes redirectAttributes,
                                          @RequestParam final Long showId,
                                          @RequestParam final String recordingId) {
        Show dbShow = showService.get(showId);
        MusicBrainzRecordingDTO recording = musicBrainzService.getRecording(recordingId);

        if (dbShow == null || recording == null) {
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Track could not be added. Either Show Id or Recording Id is missing.", UserMessageDTO.SEVERITY_ERROR));
        } else {
            //TODO: Get the ReleaseGroup for album
            //MusicBrainzReleaseGroupDTO releaseGroup = musicBrainzService.getReleaseGroup(recordingId);

            Track track = new Track();
            //track.setArtist();
            //track.setAlbum(); //put release group title here.

            //other attributes for track here.

            dbShow.addTrack(track);
            dbShow = showService.save(dbShow);
            redirectAttributes.addFlashAttribute("userMessageDTO", new UserMessageDTO("Successfully added track to show", UserMessageDTO.SEVERITY_SUCCESS));
        }
        return "redirect:/show/view/" + (dbShow.getId() == null ? -1 : dbShow.getId());
    }


}
