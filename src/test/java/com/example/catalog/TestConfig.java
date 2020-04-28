package com.example.catalog;

import com.example.catalog.persistence.repositories.AlbumRepository;
import com.example.catalog.persistence.repositories.ArtistRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@TestConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TestConfig {

    @MockBean
    private ArtistRepository artistRepository;
    @MockBean
    private AlbumRepository albumRepository;

}
