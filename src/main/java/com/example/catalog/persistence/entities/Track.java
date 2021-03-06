package com.example.catalog.persistence.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "track")
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Track {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "track_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @EqualsAndHashCode.Include
    @Column(name = "track_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar trackStart;

    @Column(name = "track_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar trackEnd;

    @EqualsAndHashCode.Include
    @Column(name = "artist")
    private String artist;

    @Column(name = "album")
    private String album;

    @Column(name = "label")
    private String label;

    @EqualsAndHashCode.Include
    @Column(name = "title")
    private String title;

    //TODO: language an object? cause what if bilingual
    @Column(name = "language")
    private String language;

    //TODO: CRTC category string? or object (number, name etc)?

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "track_tag_rel",
            joinColumns = {
                    @JoinColumn(name = "track_id", referencedColumnName = "track_id", nullable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", referencedColumnName = "tag_id", nullable = false)
            }
    )
    private Set<Tag> tags = new HashSet<>(0);

    //<editor-fold desc="Show Manipulation">
    public void setShow(Show show) {
        setShow(show, Boolean.TRUE);
    }

    /**
     * Do not use this method to set objects, use {@link #setShow(Show)} instead, this is only for Hibernate logic.
     *
     * @param show object to set
     * @param add  to stop endless loops
     */
    void setShow(Show show, Boolean add) {
        //What's going on here? https://notesonjava.wordpress.com/2008/11/03/managing-the-bidirectional-relationship/
        this.show = show;
        if (show != null && add) show.addTrack(this, Boolean.FALSE);
    }
    //</editor-fold>
}
