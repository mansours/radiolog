package com.example.catalog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "app_artist")
public class Artist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "artist_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "music_label", nullable = false, length = 128)
    private String label;

    @Column(name = "birth_dt")
    @Temporal(TemporalType.DATE)
    private Calendar dateOfBirth;

    @Column(name = "gender", length = 16)
    private String gender;

    @Lob
    @Column(name = "portrait")
    @Basic(fetch = FetchType.LAZY)
    private byte[] portrait;

    //For more info read about Many-to-Many here: https://vladmihalcea.com/2015/03/05/a-beginners-guide-to-jpa-and-hibernate-cascade-types/
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "artist_album_rel",
            joinColumns = {
                    @JoinColumn(name = "artist_id", referencedColumnName = "artist_id", nullable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "album_id", referencedColumnName = "album_id", nullable = false)
            }
    )
    private Set<Album> albums = new HashSet<>(0);
}
