package com.example.catalog.persistence.services;

import com.example.catalog.persistence.entities.Artist;
import com.example.catalog.persistence.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Artist get(Long id) {
        return artistRepository.findById(id).orElse(null);
    }
}
