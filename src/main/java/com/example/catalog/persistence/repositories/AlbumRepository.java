package com.example.catalog.persistence.repositories;

import com.example.catalog.persistence.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {
    Optional<Album> findById(Long id);
}
