package com.example.catalog.persistence.services;

import com.example.catalog.persistence.entities.Show;
import com.example.catalog.persistence.entities.Show_;
import com.example.catalog.persistence.repositories.ShowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ShowService {

    public enum Column {TITLE, PROGRAMMER, EMAIL, TIME, CODE, GUESTS, LANGUAGE}

    private final ShowRepository showRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ShowService(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    public Show get(Long id) {
        return showRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Show save(Show show) {
        return showRepository.save(show);
    }

    @Transactional(readOnly = true)
    public Page<Show> get(Map<ShowService.Column, Object> searchAttributes, ShowService.Column sortBy, Sort.Direction orderDir, Integer currentPage, Integer perPage) {
        return showRepository.findAll(filterAttributes(searchAttributes, sortBy, orderDir), PageRequest.of(currentPage - 1, perPage));
    }

    @Transactional(readOnly = true)
    public List<Show> get(Map<ShowService.Column, Object> searchAttributes, ShowService.Column sortBy, Sort.Direction orderDir) {
        return showRepository.findAll(filterAttributes(searchAttributes, sortBy, orderDir));
    }

    private static Specification<Show> filterAttributes(final Map<ShowService.Column, Object> searchByAttributes, ShowService.Column sortBy, Sort.Direction orderDir) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (ShowService.Column field : searchByAttributes.keySet()) {
                Object val = searchByAttributes.get(field);

                // Predicates
                switch (field) {
                    case TITLE:
                        predicates.add(cb.like(cb.lower(root.get(Show_.title)), "%" + ((String) val).toLowerCase() + "%"));
                        break;
                    case PROGRAMMER:
                        predicates.add(cb.like(cb.lower(root.get(Show_.programmer)), "%" + ((String) val).toLowerCase() + "%"));
                        break;
                }
            }

            if (sortBy != null) {
                switch (sortBy) {
                    case TITLE:
                        query.orderBy(orderDir.isAscending() ? cb.asc(root.get(Show_.title)) : cb.desc(root.get(Show_.title)));
                        break;
                    case TIME:
                        query.orderBy(orderDir.isAscending() ? cb.asc(root.get(Show_.showTimestamp)) : cb.desc(root.get(Show_.showTimestamp)));
                        break;
                    case PROGRAMMER:
                        query.orderBy(orderDir.isAscending() ? cb.asc(root.get(Show_.programmer)) : cb.desc(root.get(Show_.programmer)));
                        break;
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
