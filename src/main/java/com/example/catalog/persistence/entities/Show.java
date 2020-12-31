package com.example.catalog.persistence.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "show")
public class Show implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "show_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "programmer", nullable = false)
    private String programmer;

    @Column(name = "email", nullable = false)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "show_ts", nullable = false)
    private Calendar showTimestamp;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "guests")
    private String guests;

    @Column(name = "language", nullable = false)
    private String language;

    @Setter(AccessLevel.NONE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "show")
    private Set<Track> tracks = new HashSet<>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    //<editor-fold desc="Track Manipulation">

    /**
     * Use this method to add to the set, in order to for Hibernate to function properly.
     *
     * @param track object to add
     */
    public void addTrack(Track track) {
        addTrack(track, Boolean.TRUE);
    }

    /**
     * Do not use this method to add to the set, use {@link #addTrack(Track)} instead, this is only for Hibernate logic.
     *
     * @param track object to be added
     * @param set   stops endless loop
     */
    void addTrack(Track track, Boolean set) {
        //What's going on here? https://notesonjava.wordpress.com/2008/11/03/managing-the-bidirectional-relationship/
        if (track != null) {
            if (this.tracks.contains(track)) {
                this.tracks.remove(track);
                this.tracks.add(track);
            } else
                this.tracks.add(track);

            if (set) track.setShow(this, Boolean.FALSE);
        }
    }

    /**
     * Use this method to remove from the set, in order to for Hibernate to function properly.
     *
     * @param track object to remove
     */
    public void removeTrack(Track track) {
        this.tracks.remove(track);
        track.setShow(null);
    }
    //</editor-fold>
}
