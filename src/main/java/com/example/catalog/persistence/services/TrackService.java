package com.example.catalog.persistence.services;

import com.example.catalog.persistence.entities.Album;
import com.example.catalog.persistence.entities.Track;
import com.example.catalog.persistence.repositories.TrackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Service
public class TrackService {
    public enum Column { TRACKSTART, TRACKEND, ARTIST, ALBUM, LABEL, TITLE, LANGUAGE }
    private final TrackRepository trackRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TrackService(TrackRepository trackRepository){
        this.trackRepository = trackRepository;
    }
    public Track get(Long id){
        return trackRepository.findById(id).orElse(null);
    }
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Track save(Track track) {

        return trackRepository.save(track);
    }
}
