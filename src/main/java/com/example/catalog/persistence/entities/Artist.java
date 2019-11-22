package com.example.catalog.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="artist")
public class Artist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "artist_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "music_label", nullable = false)
    private String label;


}
