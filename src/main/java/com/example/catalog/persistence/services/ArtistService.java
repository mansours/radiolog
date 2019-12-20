package com.example.catalog.persistence.services;

import com.example.catalog.persistence.entities.Artist;
import com.example.catalog.persistence.entities.Artist_;
import com.example.catalog.persistence.repositories.ArtistRepository;
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
public class ArtistService {

    public enum Column {NAME, LABEL}

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

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Artist save(Artist artist) {
        return artistRepository.save(artist);
    }

    @Transactional(readOnly = true)
    public Page<Artist> get(Map<Column, Object> searchAttributes, Column sortBy, Sort.Direction orderDir, Integer currentPage, Integer perPage) {
        return artistRepository.findAll(filterAttributes(searchAttributes, sortBy, orderDir), PageRequest.of(currentPage - 1, perPage));
    }

    @Transactional(readOnly = true)
    public List<Artist> get(Map<Column, Object> searchAttributes, Column sortBy, Sort.Direction orderDir) {
        return artistRepository.findAll(filterAttributes(searchAttributes, sortBy, orderDir));
    }

    private static Specification<Artist> filterAttributes(final Map<Column, Object> searchByAttributes, Column sortBy, Sort.Direction orderDir) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

//            Join<Artist, Option> familyJoin = searchByAttributes.containsKey(Column.FAMILY_ID)
//                    ? root.join(Artist_.family, JoinType.LEFT) : null;

            for (Column field : searchByAttributes.keySet()) {
                Object val = searchByAttributes.get(field);

                // Predicates
                switch (field) {
                    case NAME:
                        predicates.add(cb.like(cb.lower(root.get(Artist_.name)), "%" + ((String) val).toLowerCase() + "%"));
                        break;
                    case LABEL:
                        predicates.add(cb.like(cb.lower(root.get(Artist_.label)), "%" + ((String) val).toLowerCase() + "%"));
                        break;
                }
            }

            if (sortBy != null) {
                switch (sortBy) {
                    case NAME:
                        query.orderBy(orderDir.isAscending() ? cb.asc(root.get(Artist_.name)) : cb.desc(root.get(Artist_.name)));
                        break;
                    case LABEL:
                        query.orderBy(orderDir.isAscending() ? cb.asc(root.get(Artist_.label)) : cb.desc(root.get(Artist_.label)));
                        break;
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
