package com.example.catalog.controller;

import com.example.catalog.TestConfig;
import com.example.catalog.controllers.ArtistController;
import com.example.catalog.dto.ArtistDTO;
import com.example.catalog.persistence.entities.Artist;
import com.example.catalog.persistence.services.ArtistService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static com.example.catalog.TestConstants.ARTISTS_ALL;
import static com.example.catalog.TestConstants.ARTIST_ADELE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@RunWith(SpringRunner.class)
@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {

    //<editor-fold desc="Mocked Beans & Configuration">
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ArtistService artistService;
    //</editor-fold>

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        ARTISTS_ALL.forEach(
                artist -> given(artistService.get(artist.getId()))
                        .willReturn(artist));

        given(artistService.get(any(), any(), any(), any(), any()))
                .willReturn(new PageImpl<>(ARTISTS_ALL));

        given(artistService.get(any(), any(), any()))
                .willReturn(new ArrayList<>(ARTISTS_ALL));
    }

    //<editor-fold desc="View List">
    @Test
    @WithMockUser
    public void list_loggedIn() throws Exception {
        mockMvc.perform(get("/artist")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    public void list_notLoggedIn() throws Exception {
        mockMvc.perform(get("/artist")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isUnauthorized());
    }
    //</editor-fold>

    //<editor-fold desc="View Artist">
    @Test
    @WithMockUser
    public void get_loggedIn() throws Exception {
        Artist adele = ARTIST_ADELE();

        mockMvc.perform(get("/artist/" + adele.getId().toString())
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("artistDTO"))
                .andExpect(model().attribute("artistDTO", new ArtistDTO(adele)));
    }

    @Test
    @WithMockUser
    public void get_loggedIn_NonExistentArtist() throws Exception {
        mockMvc.perform(get("/artist/4123894705987")
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("artistDTO"))
                .andExpect(model().attribute("artistDTO", new ArtistDTO(new Artist())));
    }
    //</editor-fold>

}
