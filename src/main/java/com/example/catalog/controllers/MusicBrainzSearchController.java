package com.example.catalog.controllers;

import com.example.catalog.dto.select2.SelectOptionDTO;
import com.example.catalog.service.MusicBrainzService;
import com.example.catalog.service.dto.MusicBrainzArtistDTO;
import com.example.catalog.service.dto.MusicBrainzRecordingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/musicBrainz/search")
public class MusicBrainzSearchController {

    private final MusicBrainzService musicBrainzService;

    @Autowired
    public MusicBrainzSearchController(MusicBrainzService musicBrainzService) {
        this.musicBrainzService = musicBrainzService;
    }

    @ResponseBody
    @GetMapping("/artist")
    public List<SelectOptionDTO> searchArtist(@RequestParam final String q) {
        //http://localhost/musicBrainz/search/artist?q=kesha

        List<MusicBrainzArtistDTO> artists = musicBrainzService.searchArtist(q);

        return artists.stream()
                .map(x -> new SelectOptionDTO(x.getId(), x.getName()))
                .collect(Collectors.toList());
    }

    @ResponseBody
    @GetMapping("/artistTracks")
    public List<SelectOptionDTO> searchArtistTracks(@RequestParam final String artistId,
                                                    @RequestParam final String q) {
        List<MusicBrainzRecordingDTO> artists = musicBrainzService.searchRecordings(artistId, q);

        return artists.stream()
                .map(x -> new SelectOptionDTO(x.getId(), x.getTitle()))
                .collect(Collectors.toList());
    }

}
