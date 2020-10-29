package com.example.catalog.controllers;

import com.example.catalog.persistence.services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ShowController {

    //<editor-fold desc="Controller Services & Autowiring">
    private final ShowService showService;

    @Autowired
    public ShowController(ShowService showService) {
        this.showService = showService;
    }
    //</editor-fold>

    //TODO: Angela can you complete the ShowController
    // list shows
    // view/edit show
    // save show
    // Add track manually
    // Add track from Musicbrainz, don't focus on this.

    //TODO: Create a track controller
    // Add track for a show
    // remove track
    // edit track details
    // add/update tags

}
