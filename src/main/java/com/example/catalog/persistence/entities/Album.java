package com.example.catalog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "app_album")
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "album_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "release_date")
    @Temporal(TemporalType.DATE)
    private Calendar releaseDate;

    @Column(name = "genre")
    private String genre;

    @Column(name = "no_of_tracks", nullable = false)
    private Long numberOfTracks;

    @Column(name = "download_count")
    private Long downloadCount;

    @Column(name = "music_label", nullable = false)
    private String label;

    //For more info read about Many-to-Many here: https://vladmihalcea.com/2015/03/05/a-beginners-guide-to-jpa-and-hibernate-cascade-types/
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "artist_album_rel",
            joinColumns = {
                    @JoinColumn(name = "album_id", referencedColumnName = "album_id", nullable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "artist_id", referencedColumnName = "artist_id", nullable = false)
            }
    )
    private Set<Artist> artists = new HashSet<>(0);
}
