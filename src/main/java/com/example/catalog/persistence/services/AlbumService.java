package com.example.catalog.persistence.services;

import com.example.catalog.persistence.entities.Album;
import com.example.catalog.persistence.repositories.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Album get(Long id){
        return albumRepository.findById(id).orElse(null);
    }
}
