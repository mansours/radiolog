package com.example.catalog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="app_album")
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "album_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name= "release_date", nullable = false)
    private Long date;

    @Column (name= "genre", nullable = false)
    private String genre;

    @Column(name = "no_of_tracks", nullable = false)
    private Long numberOfTracks;

    @Column(name = "download_count", nullable = false)
    private Long downloadCount;

    @Column(name = "music_label", nullable = false)
    private String label;


}
