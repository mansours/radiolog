package com.example.catalog.persistence.services;

import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.repositories.ShowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Service
public class ShowService {

    public enum Column {TITLE, PROGRAMMER, EMAIL, TIME, CODE, GUESTS, LANGUAGE}

    private final ShowRepository showRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ShowService(ShowRepository showRepository){
        this.showRepository = showRepository;
    }

    public Show get(Long id){
        return showRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Show save(Show show) {

        return showRepository.save(show);
    }
}
