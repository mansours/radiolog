package com.example.catalog.persistence.services;

import com.example.catalog.persistence.entities.Tag;
import com.example.catalog.persistence.entities.Tag_;
import com.example.catalog.persistence.repositories.TagRepository;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TagService {

    public enum Column {ID, VALUE}

    private final TagRepository tagRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public Tag get(Long id) {
        return tagRepository.findById(id).orElse(null);
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public Page<Tag> get(Map<Column, Object> searchAttributes, Column sortBy, Sort.Direction orderDir, Integer currentPage, Integer perPage) {
        return tagRepository.findAll(filterAttributes(searchAttributes, sortBy, orderDir), PageRequest.of(currentPage - 1, perPage));
    }

    @Transactional(readOnly = true)
    public List<Tag> get(Map<Column, Object> searchAttributes, Column sortBy, Sort.Direction orderDir) {
        return tagRepository.findAll(filterAttributes(searchAttributes, sortBy, orderDir));
    }

    private static Specification<Tag> filterAttributes(final Map<Column, Object> searchByAttributes, Column sortBy, Sort.Direction orderDir) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (Column field : searchByAttributes.keySet()) {
                Object val = searchByAttributes.get(field);

                // Predicates
                switch (field) {
                    case VALUE:
                        predicates.add(cb.like(cb.lower(root.get(Tag_.value)), "%" + ((String) val).toLowerCase() + "%"));
                        break;
                    case ID:
                        if (val instanceof Collection) {
                            if (!((Collection) val).isEmpty())
                                predicates.add(root.get(Tag_.id).in((Collection) val));
                        } else
                            predicates.add(cb.equal(root.get(Tag_.id), val));
                        break;
                }
            }

            if (sortBy != null) {
                switch (sortBy) {
                    case VALUE:
                        query.orderBy(orderDir.isAscending() ? cb.asc(root.get(Tag_.value)) : cb.desc(root.get(Tag_.value)));
                        break;
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
