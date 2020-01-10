package com.example.catalog.persistence.services;

import com.example.catalog.persistence.entities.Album;
import com.example.catalog.persistence.entities.Album_;
import com.example.catalog.persistence.repositories.AlbumRepository;
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
public class AlbumService {

    public enum Column {NAME, GENRE, LABEL}

    private final AlbumRepository albumRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Album get(Long id) {
        return albumRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Album save(Album album) {
        return albumRepository.save(album);
    }

    @Transactional(readOnly = true)
    public Page<Album> get(Map<Column, Object> searchAttributes, Column sortBy, Sort.Direction orderDir, Integer currentPage, Integer perPage) {
        return albumRepository.findAll(filterAttributes(searchAttributes, sortBy, orderDir), PageRequest.of(currentPage - 1, perPage));
    }

    @Transactional(readOnly = true)
    public List<Album> get(Map<Column, Object> searchAttributes, Column sortBy, Sort.Direction orderDir) {
        return albumRepository.findAll(filterAttributes(searchAttributes, sortBy, orderDir));
    }

    private static Specification<Album> filterAttributes(final Map<Column, Object> searchByAttributes, Column sortBy, Sort.Direction orderDir) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

//            Join<Album, Option> familyJoin = searchByAttributes.containsKey(Column.FAMILY_ID)
//                    ? root.join(Album_.family, JoinType.LEFT) : null;

            for (Column field : searchByAttributes.keySet()) {
                Object val = searchByAttributes.get(field);

                // Predicates
                switch (field) {
                    case NAME:
                        predicates.add(cb.like(cb.lower(root.get(Album_.name)), "%" + ((String) val).toLowerCase() + "%"));
                        break;
                    case GENRE:
                        predicates.add(cb.like(cb.lower(root.get(Album_.genre)), "%" + ((String) val).toLowerCase() + "%"));
                        break;
                    case LABEL:
                        predicates.add(cb.like(cb.lower(root.get(Album_.label)), "%" + ((String) val).toLowerCase() + "%"));
                        break;
                }
            }

            if (sortBy != null) {
                switch (sortBy) {
                    case NAME:
                        query.orderBy(orderDir.isAscending() ? cb.asc(root.get(Album_.name)) : cb.desc(root.get(Album_.name)));
                        break;
                    case GENRE:
                        query.orderBy(orderDir.isAscending() ? cb.asc(root.get(Album_.genre)) : cb.desc(root.get(Album_.genre)));
                        break;
                    case LABEL:
                        query.orderBy(orderDir.isAscending() ? cb.asc(root.get(Album_.label)) : cb.desc(root.get(Album_.label)));
                        break;
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
